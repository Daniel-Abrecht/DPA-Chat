package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import chat.resources.Message;

@SuppressWarnings("serial")
public class MessageView extends JPanel {
	private Message message;
	private JTextArea textArea = new JTextArea();
	private ProfilView profileView = new ProfilView();

	public MessageView(Message message) {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		profileView.setPreferredSize(new Dimension(100,0));
		add(textArea, BorderLayout.CENTER);
		add(profileView , BorderLayout.WEST);
		setMessage(message);
	}

	@Override
	public Dimension getPreferredSize() {
		Insets ins = getParent().getInsets();
		Dimension dim = new Dimension(getParent().getSize().width - ins.left
				- ins.right, super.getPreferredSize().height);
		return dim;
	}

	public void setMessage(Message message) {
		this.message = message;
		textArea.setText(message.getContent());
		profileView.setProfil(message.getProfil());
	}

	public Message getMessage() {
		return message;
	}

}
