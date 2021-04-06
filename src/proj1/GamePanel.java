package proj1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.*;

public class GamePanel extends JPanel {

	private static GamePanel gamePanel = null;

	private ScorePanel scorePanel = ScorePanel.getScorePanel();
	private WordManager wordManager = WordManager.getWordManager();
	private Vector<WordThread> wordList = new Vector<WordThread>();
	private LifePanel lifePanel = LifePanel.getLifePanel();

	private JGameGroundPanel jGameGroundPanel = new JGameGroundPanel();
	private JInputPanel jInputPanel = new JInputPanel();

	// width = 600, height = 430
	private int n = 10; // ȭ�鿡 �ߴ� �ܾ� ����
	private int delay;
	private int life = 0;
	private int level;
	private boolean isPlay = false; // play ������ Ȯ��
	private boolean isStop = false; // stop �ߴ��� Ȯ�� :: true=���� false=������

	ExThread exThread = null;

	// ������ ���
	public static GamePanel getGamePanel() {
		if (gamePanel == null) {
			gamePanel = new GamePanel();
		}
		return gamePanel;
	}

	public JGameGroundPanel getJGameGroundPanel() {
		return jGameGroundPanel;
	}

	// GamePanel ������
	public GamePanel() {
		setLayout(new BorderLayout());
		add(jGameGroundPanel, BorderLayout.CENTER);
		add(jInputPanel, BorderLayout.SOUTH);
	}

	public boolean getIsPlay() {
		return isPlay;
	}

//////////
	// �ܾ� Thread �� n(20)�� �����Ͽ� wordList ���Ϳ� �ְ� ������ :: ���� ó�� �ܾ� label 20�� ����� ������ ���
	class ExThread extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < n; i++) {
				WordThread th = new WordThread();
				wordList.add(th);
				th.start();

				try {
					Thread.sleep(1500); // 1.5�ʵ��� ��
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}

//////////
	class WordThread extends Thread {
		private int x, y;
		private JLabel wordLabel;
		private String comWord;

		public WordThread() {
			this.x = (int) (Math.random() * 550 + 10);
			this.y = 0;

			ImageIcon icon = new ImageIcon("media/thunder.png");
			wordLabel = new JLabel(icon);
			wordLabel.setHorizontalTextPosition(JLabel.CENTER);
			wordLabel.setVerticalTextPosition(JLabel.CENTER);
			jGameGroundPanel.add(wordLabel); // ȭ�����
			wordLabel.setSize(200, 30);
			wordLabel.setFont(new Font("Lucida Console", Font.PLAIN, 14));

			newWord(); // �ܾ� ���� ������
		}

		public void run() {
			while(true) {
				try {
					Thread.sleep(delay);	// 0.5�� ��
					
					if((y+10) > 430) {	// y ���� 430 �̻��̸� �ܾ� ���� ������
						lifePanel.lifeUp(life);
						life++;
						
						if(life > 2) {	// life 3�� �� ���
							if(exThread.isAlive()) {
								exThread.interrupt();
							}
							jGameGroundPanel.endGame();
							
							return;
						} else
							newWord();
					} else {				// �׷��� ������ ��� ������ ������
						y += 10;
						wordLabel.setLocation(x, y);
					}		
				} catch (InterruptedException e) {
					return;
				}
			}
		}

		// �ܾ ���� ������ �� (�ܾ� ������ ���� ��� ��ġ�� �ٽ� ��)
		private void newWord() {
			this.x = (int) (Math.random() * 550 + 10);
			this.y = 0;

			comWord = wordManager.getWord();
			wordLabel.setText(comWord);
			wordLabel.setLocation(x, y);
		}

		public String getComWord() {
			return comWord;
		}
	}

//////////
	class JGameGroundPanel extends JPanel {
		private Image img;

		public JGameGroundPanel() {
			ImageIcon icon = new ImageIcon("media/back1.jpg");
			img = icon.getImage();
			setLayout(null);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		}

		public void initGame() { // ���� �ʱ�ȭ
			removeAll(); // JLabel ���ֱ�
			repaint(); // �ٽ� �׸���

			delay = 100;
			level = 1;
			lifePanel.initAll();
			scorePanel.initAll();

			isPlay = true;
			isStop = false;

			exThread = new ExThread();
			exThread.start();
		}

		public void endGame() { // ���� ����
			isPlay = false;

			for (int i = 0; i < wordList.size(); i++) {
				WordThread th = wordList.get(i);
				wordList.remove(i);
				i--;
				th.interrupt();
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int result = JOptionPane.showConfirmDialog(this, scorePanel.getScore() + "���Դϴ�! �ٽ� �÷����ұ��?", "���� ����",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				initGame();
			}
		}

		public void stopGame() { // ���� ���߱�
			isStop = true;

			if (exThread.isAlive()) {
				exThread.suspend();
			}

			for (int i = 0; i < wordList.size(); i++) {
				WordThread th = wordList.get(i);
				th.suspend();
			}
		}

		public void continueGame() { // �̾��ϱ�
			isStop = false;

			if (exThread.isAlive()) {
				exThread.resume();
			}

			for (int i = 0; i < wordList.size(); i++) {
				WordThread th = wordList.get(i);
				th.resume();
			}
		}

		// restart()

	}

//////////
	class JInputPanel extends JPanel {
		private JTextField textField = new JTextField(30);
		private Image img;

		public JInputPanel() {
			ImageIcon icon = new ImageIcon("media/back.jpg");
			img = icon.getImage();

			setLayout(new FlowLayout());
			add(textField);
			JButton enter = new JButton("ENTER");
			enter.setFont(new Font("Algerian", Font.PLAIN, 15));
			enter.setBackground(Color.LIGHT_GRAY);
			add(enter);

			// enterŰ ��������
			enter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isStop) {
						matchWord();
					}
				}
			});
			// ����ڰ� �Է��� ���� �Ǵ�
			textField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isStop) {
						matchWord();
					}
				}
			});
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		}

		public JTextField getTextField() {
			return textField;
		}

		public void matchWord() {
			String usrWord = textField.getText();

			for (int i = 0; i < wordList.size(); i++) { // wordList�� 20���� ä ��������� ������ �Է¹��� �� �ֵ��� �� ���� 20�� �ƴ�
														// wordList.size()�� ��
				WordThread th = wordList.get(i); // wordList���� thread�� �ϳ��� �ҷ��� usrWord�� ����

				String comWord = th.getComWord();

				if (usrWord.equals(comWord)) { // �ܾ ������ �ܾ� ���� �ٲٰ� �ٽ� ��ġ, textField ���� ���� �ø���
					th.newWord();

					textField.setText(""); // textField ���
					scorePanel.scoreUp();

					if ((int) (scorePanel.getScore() / 50) + 1 != level) {
						levelUp();
					}
				} else {
					textField.setText("");
				}
			}
		}

		public void levelUp() {
			level++;
			scorePanel.setLevelLabel(Integer.toString(level));
			if (delay > 100) { // �ӵ��� 100 �̸����δ� �� ��������
				delay -= 30;
			}
		}

	}
}
