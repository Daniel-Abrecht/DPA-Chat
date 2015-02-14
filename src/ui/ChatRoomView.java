package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import chat.Chat;
import chat.resources.ChatRoom;
import chat.resources.Message;

@SuppressWarnings("serial")
public class ChatRoomView extends JFrame {
	private VScrollList messageList = new VScrollList();
	private ArrayList<MessageView> messageViews = new ArrayList<MessageView>();
	private ChatRoom chatRoom;
	private JPanel postFormPanel = new JPanel();
	private JTextArea postFormTextArea = new JTextArea();
	private JButton sendButton = new JButton("▶");

	public ChatRoomView(ChatRoom chatRoom) {
		messageList.setBackground(Color.BLACK);
		setChatRoom(chatRoom);
		setSize(800, 600);
		setLayout(new BorderLayout());
		add(messageList, BorderLayout.CENTER);
		add(postFormPanel, BorderLayout.SOUTH);
		postFormPanel.setPreferredSize(new Dimension(0, 200));
		postFormPanel.setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(postFormTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		postFormTextArea.setLineWrap(true);
		postFormTextArea.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,18));
		postFormPanel.add(scroll, BorderLayout.CENTER);
		postFormPanel.add(sendButton, BorderLayout.EAST);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendMessage();
			}
		});
	}

	protected void sendMessage() {
		String text = postFormTextArea.getText();
		postFormTextArea.setText("");
		Message msg = new Message();
		msg.register(Chat.currentProfil.getUserManager());
		msg.setChatRoom(chatRoom);
		msg.setContent(text);
		msg.updateRemote();
	}

	private void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
		updateUI();
	}

	private void updateUI() {
		setTitle("Chatroom " + chatRoom.getName());
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
