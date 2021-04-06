package proj1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class GameFrame extends JFrame{
	private GamePanel gamePanel = GamePanel.getGamePanel();
	private	ScorePanel scorePanel = ScorePanel.getScorePanel();
	private EditPanel editPanel = new EditPanel();
	private LifePanel lifePanel = new LifePanel();
	private Clip clip;
	
	public GameFrame() {
		setTitle("스머프 구하기");
		setSize(900,600);
		setResizable(false); //창의 크기 조절 불가능
		splitPane();
		setVisible(true);
		
		makeMenuBar();
		makeToolBar();
		
		loadAudio("media/bgm.wav");
		clip.start();
	}

	private void loadAudio(String pathName) {
		try {
			clip = AudioSystem.getClip();
			File audioFile = new File(pathName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip.open(audioStream);
		}
		catch (LineUnavailableException e) { e.printStackTrace(); }
		catch (UnsupportedAudioFileException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
	}

	private void splitPane() {
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);		
		hPane.setDividerLocation(700);
		hPane.setEnabled(false); // split bar를 움직일 수 없도록 하기 위해
		hPane.setLeftComponent(gamePanel);
		
		JSplitPane pPane = new JSplitPane();
		hPane.setRightComponent(pPane);
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(100);
		pPane.setEnabled(false);
		pPane.setTopComponent(scorePanel);
		
		JSplitPane pPane2 = new JSplitPane();
		pPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);	
		pPane2.setDividerLocation(100);
		pPane2.setEnabled(false);
		pPane.setBottomComponent(pPane2);
		pPane2.setTopComponent(editPanel);
		pPane2.setBottomComponent(lifePanel);
	}
	
	private void makeMenuBar() {
		JMenuBar mb = new JMenuBar();
		this.setJMenuBar(mb);
		
		JMenu fileMenu = new JMenu("Typing Game");
		fileMenu.setFont(new Font("Algerian", Font.BOLD, 18));
		
		JMenuItem item[] = new JMenuItem[2];
		item[0] = new JMenuItem("개발자 정보");
		item[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "1891280 이현정", "Message", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		item[1] = new JMenuItem("게임 방법");
		item[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "텍스트를 맞춰 스머프 마을을 구해주세요!", "Message", JOptionPane.INFORMATION_MESSAGE);
			}
		});
				
		fileMenu.add(item[0]);
		fileMenu.add(item[1]);
		
		mb.add(fileMenu);
	}
	
	private void makeToolBar() {
		JToolBar tb = new JToolBar();
		getContentPane().add(tb, BorderLayout.NORTH);
		
		JButton startButton = new JButton("start");
		startButton.setFont(new Font("Algerian", Font.PLAIN, 15));
		tb.add(startButton);
		JButton stopButton = new JButton("stop");
		stopButton.setFont(new Font("Algerian", Font.PLAIN, 15));
		tb.add(stopButton);
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!gamePanel.getIsPlay()) {
					gamePanel.getJGameGroundPanel().initGame();
				}}
		});
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gamePanel.getIsPlay()) {
					JButton b = (JButton)e.getSource();
					if(b.getText().contentEquals("stop")) {		// 멈추기
						b.setText("continue");
						gamePanel.getJGameGroundPanel().stopGame();
					}
					else {		// 계속하기
						b.setText("stop");
						gamePanel.getJGameGroundPanel().continueGame();
					}
				}
			}
		});	
	}

}
