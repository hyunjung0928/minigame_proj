package proj1;

import java.awt.*;
import javax.swing.*;

public class LifePanel extends JPanel {
	private static LifePanel lifePanel = null;

	private int life = 0;
	
	private Image img;

	ImageIcon imageIcon = new ImageIcon("media/smurf1.png");
	Image changedImg = imageIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
	ImageIcon smurf = new ImageIcon(changedImg);
	JLabel label1 = new JLabel(smurf);
	
	ImageIcon imageIcon2 = new ImageIcon("media/smurf2.png");
	Image changedImg2 = imageIcon2.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
	ImageIcon smurf2 = new ImageIcon(changedImg2);
	JLabel label2 = new JLabel(smurf2);
	
	ImageIcon imageIcon3 = new ImageIcon("media/smurf3.png");
	Image changedImg3 = imageIcon3.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
	ImageIcon smurf3 = new ImageIcon(changedImg3);
	JLabel label3 = new JLabel(smurf3);

	ImageIcon imageIcon4 = new ImageIcon("media/back.jpg");
	Image changedImg4 = imageIcon4.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
	ImageIcon noneImg = new ImageIcon(changedImg4);
	JLabel label4 = new JLabel(noneImg);

	// 생성자 얻기
	public static LifePanel getLifePanel() {
		if (lifePanel == null) {
			lifePanel = new LifePanel();
		}
		return lifePanel;
	}

	public LifePanel() {
		ImageIcon icon = new ImageIcon("media/back.jpg");
		img = icon.getImage();

		setLayout(null);

		label1.setLocation(50, 10);
		label1.setSize(70, 70);
		add(label1);
		
		label2.setLocation(50, 90);
		label2.setSize(70, 70);
		add(label2);
		
		label3.setLocation(50, 170);
		label3.setSize(70, 70);
		add(label3);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

	public void initAll() {
		life = 0;
		setLayout(null);

	}

	public void lifeUp(int i) {
		
		setLayout(null);
		
		label4.setLocation(50, 80*i+10);
		label4.setSize(70, 70);
		add(label4);
		System.out.println((i+1) + " die");
		
		lifePanel.revalidate();
		lifePanel.repaint();

	}
}
