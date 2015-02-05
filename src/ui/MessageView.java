package ui;

import java.awt.Panel;

import chat.resources.Message;

@SuppressWarnings("serial")
public class MessageView extends Panel {
	private Message message;

	public MessageView(Message message) {
		this.message = message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

}
