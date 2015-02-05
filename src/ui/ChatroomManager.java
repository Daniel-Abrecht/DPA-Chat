package ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import chat.resources.ChatRoom;

@SuppressWarnings("serial")
public class ChatroomManager extends Frame {
	private VScrollList vScrollList = new VScrollList();
	private ChatRoomEditor chatRoomEditor = new ChatRoomEditor();
	public ChatRoom selectedChatroom;
	private ArrayList<ChatroomItem> chatRoomItems = new ArrayList<ChatroomItem>();
	private static ChatroomManager chatroomManager;

	public static ChatroomManager getInstance() {
		if (chatroomManager != null)
			return chatroomManager;
		return chatroomManager = new ChatroomManager();
	}

	private ChatroomManager() {

		super();

		setTitle("Chatroom Manager");
		setSize(400, 600);
		addWindowListener(new ChatroomManagerListener());
		vScrollList.setBackground(new Color(0, 0, 0));
		setLayout(new BorderLayout());

		add(vScrollList, BorderLayout.CENTER);

		Panel buttons = new Panel();
		buttons.setLayout(new GridLayout(1, 0));

		Button newRoom = new Button("Neu");
		Button editRoom = new Button("Umbenennen");

		buttons.add(editRoom);
		editRoom.setEnabled(false);
		buttons.add(newRoom);
		buttons.setPreferredSize(new Dimension(0, 50));

		add(buttons, BorderLayout.SOUTH);

		newRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chatRoomEditor.setTarget(new ChatRoom());
				chatRoomEditor.setVisible(true);
			}
		});

		editRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("rename");
			}
		});

	}

	static class ChatroomManagerListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
		}
	}

	private class ChatroomItem extends DefaultListItem {
		private Label label = new Label();
		private ChatRoom chatRoom;

		public ChatroomItem(ChatRoom chatRoom) {
			setChatRoom(chatRoom);
			setLayout(new BorderLayout(50, 50));
			add(label, BorderLayout.CENTER);
			setPreferredSize(new Dimension(0, 50));
			label.addMouseListener(this);
		}

		public void setName(String name) {
			chatRoom.setName(name);
			label.setText(name);
		}

		@Override
		public void onSelect() {
			chatRoom.display();
		}

		@Override
		public void onActive(boolean b) {
			selectedChatroom = chatRoom;
		}

		public ChatRoom getChatRoom() {
			return chatRoom;
		}

		public void setChatRoom(ChatRoom chatRoom) {
			this.chatRoom = chatRoom;
			updateContent();
		}

		private void updateContent() {
			setName(chatRoom.getName());
		}

	};

	public void update(ChatRoom chatRoom) {
		Integer id = chatRoom.getId();
		int i;
		for (i = chatRoomItems.size(); i-- > 0;) {
			ChatroomItem mv = chatRoomItems.get(i);
			if (mv.getChatRoom().getId() <= id) {
				if (mv.getChatRoom().getId() == id) {
					mv.setChatRoom(chatRoom);
					return;
				} else {
					break;
				}
			}
		}
		if (i < 0)
			i = chatRoomItems.size();
		else
			i++;
		ChatroomItem messageView = new ChatroomItem(chatRoom);
		chatRoomItems.add(i, messageView);
		messageView.addTo(vScrollList, i);
		vScrollList.revalidate();
	}

	public void remove(Integer id) {
		for (Integer i = chatRoomItems.size(); i-- > 0;) {
			if (chatRoomItems.get(i).getChatRoom().getId() == id) {
				chatRoomItems.remove(i);
				vScrollList.remove(i);
				break;
			}
		}
	}

}
