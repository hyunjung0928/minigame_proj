package proj1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditPanel extends JPanel {

	private static EditPanel editPanel = null;
	private WordManager wordManager = WordManager.getWordManager();

	private JTextField textField = new JTextField(15);
	private JButton addButton = new JButton("add");
	private JButton saveButton = new JButton("save");
	private Image img;

	// 생성자 얻기
	public static EditPanel getEditPanel() {
		if (editPanel == null) {
			editPanel = new EditPanel();
		}
		return editPanel;
	}

	public EditPanel() {

		setLayout(new FlowLayout());
		add(textField);
		add(addButton);
		add(saveButton);

		addButton.setFont(new Font("Algerian", Font.PLAIN, 15));
		saveButton.setFont(new Font("Algerian", Font.PLAIN, 15));

		addButton.setBackground(Color.LIGHT_GRAY);
		saveButton.setBackground(Color.LIGHT_GRAY);

		addWord();
		saveWord();
		
		ImageIcon icon = new ImageIcon("media/back.jpg");
		img = icon.getImage();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

	public void addWord() {
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField t = (JTextField)e.getSource();
				String newWord = t.getText();
				
				wordManager.vectorWrite(newWord);
				textField.setText("");
			}
		});
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newWord = textField.getText();
				
				wordManager.vectorWrite(newWord);
				textField.setText("");
			}
		});
	}

	public void saveWord() {
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newWord = textField.getText();
				
				wordManager.fileWrite(newWord);
				textField.setText("");
			}
		});
	}
}
