package proj1;

import java.awt.*;
import javax.swing.*;

public class ScorePanel extends JPanel {
	private static ScorePanel scorePanel = null;

	private int score = 0;
	private int level = 0;
	private JLabel scoreText = new JLabel("SCORE");
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	private Image img;
	private JLabel levelText = new JLabel("LEVEL");
	private JLabel levelLabel = new JLabel("1");
	
	// 생성자 얻기
	public static ScorePanel getScorePanel() {
		if(scorePanel == null) {
			scorePanel = new ScorePanel();
		}
		return scorePanel;
	}
	
	public void setLevelLabel(String level) {
		levelLabel.setText(level);
	}
	
	public ScorePanel() {
		score = 0;
	    ImageIcon icon = new ImageIcon("media/back.jpg");
	    img = icon.getImage();
		
		setLayout(null);
		scoreText.setSize(50, 30);
		scoreText.setLocation(30, 20);
		scoreText.setFont(new Font("Algerian", Font.PLAIN, 15));
		add(scoreText);
		
		scoreLabel.setSize(100, 30);
		scoreLabel.setLocation(100, 20);
		scoreLabel.setFont(new Font("휴먼둥근헤드라인", Font.BOLD, 15));
		scoreLabel.setForeground(Color.ORANGE);
		add(scoreLabel);
		
		levelText.setSize(50, 30);
		levelText.setLocation(30, 50);
		levelText.setFont(new Font("Algerian", Font.PLAIN, 15));
		add(levelText);
		
	    levelLabel.setSize(100, 30);
	    levelLabel.setLocation(100, 50);
	    levelLabel.setFont(new Font("휴먼둥근헤드라인", Font.BOLD, 15));
	    levelLabel.setForeground(Color.ORANGE);
		add(levelLabel);
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
     }
	
	public int getScore() {
		return score;
	}
	public void initAll() {
		score = 0;
		level = 1;
		scoreLabel.setText(Integer.toString(score));
		levelLabel.setText(Integer.toString(level));
	}
	public void scoreUp() {
		score += 10; //10점씩 높아짐
		scoreLabel.setText(Integer.toString(score));
	}
}
