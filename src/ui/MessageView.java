package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;

import javax.swing.JTextArea;

import chat.resources.Message;

@SuppressWarnings("serial")
public class MessageView extends Panel {
	private Message message;
	private JTextArea textArea = new JTextArea();
	
	public MessageView(Message message) {
		setMessage(message);
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		add(textArea,BorderLayout.CENTER);
	}

	public void setMessage(Message message) {
		this.message = message;
		textArea.setText(message.getContent());
	}

	public Message getMessage() {
		return message;
	}

}
