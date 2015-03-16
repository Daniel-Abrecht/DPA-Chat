package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import chat.resources.ChatRoom;

@SuppressWarnings("serial")
public class ChatroomManager extends Frame {
	private VScrollList vScrollList = new VScrollList();
	private ChatRoomEditor chatRoomEditor = ChatRoomEditor.getInstance();
	public ChatRoom selectedChatroom;
	private ArrayList<ChatroomItem> chatRoomItems = new ArrayList<ChatroomItem>();
	private JButton editRoom;
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
		setLayout(new BorderLayout());

		Label l = new Label("Chatrooms");
		l.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		add(l, BorderLayout.NORTH);
		add(vScrollList, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 0));

		JButton newRoom = new JButton("Neu");
		editRoom = new JButton("Umbenennen");

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
				chatRoomEditor.setTarget(selectedChatroom);
				chatRoomEditor.setVisible(true);
			}
		});

	}

	static class ChatroomManagerListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			if (Utils.VisibleWindowCount() <= 1)
				System.exit(0);
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

		@Override
		public void onSelect() {
			chatRoom.getView().setVisible(true);
		}

		@Override
		public void onActive(boolean b) {
			if (b) {
				selectedChatroom = chatRoom;
				editRoom.setEnabled(true);
			} else {
				selectedChatroom = null;
				editRoom.setEnabled(false);
			}
		}

		public ChatRoom getChatRoom() {
			return chatRoom;
		}

		public void setChatRoom(ChatRoom chatRoom) {
			this.chatRoom = chatRoom;
			updateContent();
		}

		private void updateContent() {
			label.setText(chatRoom.getName());
		}

	};

	public void update(ChatRoom chatRoom) {
		int i = chatRoomItems.size();
		while (i-- > 0) {
			ChatroomItem mv = chatRoomItems.get(i);
			ChatRoom chatRoomToCompare = mv.getChatRoom();
			if (chatRoomToCompare.compareIdentifier(chatRoom) < 0)
				break;
			if (!chatRoomToCompare.hasSameIdentifierAs(chatRoom))
				continue;
			mv.setChatRoom(chatRoom);
			return;
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
			if ((int) chatRoomItems.get(i).getChatRoom().getId() == (int) id) {
				chatRoomItems.remove(i);
				vScrollList.remove(i);
				break;
			}
		}
	}

}
