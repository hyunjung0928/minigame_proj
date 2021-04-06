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
	private int n = 10; // 화면에 뜨는 단어 개수
	private int delay;
	private int life = 0;
	private int level;
	private boolean isPlay = false; // play 중인지 확인
	private boolean isStop = false; // stop 했는지 확인 :: true=멈춤 false=실행중

	ExThread exThread = null;

	// 생성자 얻기
	public static GamePanel getGamePanel() {
		if (gamePanel == null) {
			gamePanel = new GamePanel();
		}
		return gamePanel;
	}

	public JGameGroundPanel getJGameGroundPanel() {
		return jGameGroundPanel;
	}

	// GamePanel 생성자
	public GamePanel() {
		setLayout(new BorderLayout());
		add(jGameGroundPanel, BorderLayout.CENTER);
		add(jInputPanel, BorderLayout.SOUTH);
	}

	public boolean getIsPlay() {
		return isPlay;
	}

//////////
	// 단어 Thread 를 n(20)개 생성하여 wordList 벡터에 넣고 실행함 :: 가장 처음 단어 label 20개 만드는 데에만 사용
	class ExThread extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < n; i++) {
				WordThread th = new WordThread();
				wordList.add(th);
				th.start();

				try {
					Thread.sleep(1500); // 1.5초동안 쉼
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
			jGameGroundPanel.add(wordLabel); // 화면출력
			wordLabel.setSize(200, 30);
			wordLabel.setFont(new Font("Lucida Console", Font.PLAIN, 14));

			newWord(); // 단어 새로 떨어짐
		}

		public void run() {
			while(true) {
				try {
					Thread.sleep(delay);	// 0.5초 쉼
					
					if((y+10) > 430) {	// y 값이 430 이상이면 단어 새로 떨어짐
						lifePanel.lifeUp(life);
						life++;
						
						if(life > 2) {	// life 3개 다 닳면
							if(exThread.isAlive()) {
								exThread.interrupt();
							}
							jGameGroundPanel.endGame();
							
							return;
						} else
							newWord();
					} else {				// 그렇지 않으면 계속 밑으로 떨어짐
						y += 10;
						wordLabel.setLocation(x, y);
					}		
				} catch (InterruptedException e) {
					return;
				}
			}
		}

		// 단어를 새로 나오게 함 (단어 내용을 새로 얻고 배치를 다시 함)
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

		public void initGame() { // 게임 초기화
			removeAll(); // JLabel 없애기
			repaint(); // 다시 그리기

			delay = 100;
			level = 1;
			lifePanel.initAll();
			scorePanel.initAll();

			isPlay = true;
			isStop = false;

			exThread = new ExThread();
			exThread.start();
		}

		public void endGame() { // 게임 종료
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

			int result = JOptionPane.showConfirmDialog(this, scorePanel.getScore() + "점입니다! 다시 플레이할까요?", "게임 종료",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				initGame();
			}
		}

		public void stopGame() { // 게임 멈추기
			isStop = true;

			if (exThread.isAlive()) {
				exThread.suspend();
			}

			for (int i = 0; i < wordList.size(); i++) {
				WordThread th = wordList.get(i);
				th.suspend();
			}
		}

		public void continueGame() { // 이어하기
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

			// enter키 눌렀을때
			enter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isStop) {
						matchWord();
					}
				}
			});
			// 사용자가 입력한 글자 판단
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

			for (int i = 0; i < wordList.size(); i++) { // wordList가 20개가 채 만들어지기 전에도 입력받을 수 있도록 총 개수 20이 아닌
														// wordList.size()로 함
				WordThread th = wordList.get(i); // wordList에서 thread를 하나씩 불러와 usrWord와 비교함

				String comWord = th.getComWord();

				if (usrWord.equals(comWord)) { // 단어가 맞으면 단어 내용 바꾸고 다시 배치, textField 비우고 점수 올리기
					th.newWord();

					textField.setText(""); // textField 비움
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
			if (delay > 100) { // 속도가 100 미만으로는 안 내려간다
				delay -= 30;
			}
		}

	}
}
