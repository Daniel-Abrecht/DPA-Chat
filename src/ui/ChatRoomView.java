package ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.ArrayList;

import chat.resources.Message;

@SuppressWarnings("serial")
public class ChatRoomView extends Frame {
	private VScrollList messageList = new VScrollList();
	private ArrayList<MessageView> messageViews = new ArrayList<MessageView>();

	public ChatRoomView() {
		setSize(800, 600);
		setLayout(new BorderLayout());
		add(messageList, BorderLayout.CENTER);
	}

	public void update(Message message) {
		Integer id = message.getId();
		int i;
		for (i = messageViews.size(); i-- > 0;) {
			MessageView mv = messageViews.get(i);
			if (mv.getMessage().getId() <= id) {
				if (mv.getMessage().getId() == id) {
					mv.setMessage(message);
					return;
				} else {
					break;
				}
			}
		}
		if (i < 0)
			i = messageViews.size();
		else
			i++;
		MessageView messageView = new MessageView(message);
		messageViews.add(i, messageView);
		messageList.add(messageView, i);
		messageList.revalidate();
	}

	public void remove(Integer id) {
		for (Integer i = messageViews.size(); i-- > 0;) {
			Integer mid = messageViews.get(i).getMessage().getId();
			if (mid == id) {
				messageViews.remove(i);
				messageList.remove(i);
				break;
			}
		}
	}
}
