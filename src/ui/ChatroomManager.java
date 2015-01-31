package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import chat.manager.EndpointManager;
import chat.manager.UserManager;
import chat.resources.ChatRoom;
import chat.resources.Resource;
import chat.resources.ResourceHandler;
import chat.resources.ResourcePool;

@SuppressWarnings("serial")
public class ChatroomManager extends Frame {
	private VScrollList vScrollList = new VScrollList();

	public ChatroomManager() {
		super();
		setTitle("Chatroom Manager");
		setSize(400, 600);
		addWindowListener(new ChatroomManagerListener());
		vScrollList.setBackground(new Color(0,0,0));
		add(vScrollList);
	}

	static class ChatroomManagerListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
		}
	}

	private static class ChatroomItem extends DefaultListItem {
		private String name;
		private Label label = new Label();		

		public ChatroomItem() {
			root.setLayout(new BorderLayout(50,50));
			root.add(label,BorderLayout.CENTER);
			root.setPreferredSize(new Dimension(0,50));
			label.addMouseListener(this);
		}

		public void setName(String name) {
			this.name = name;
			label.setText(name);
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			System.out.println(name);
		}
	};

	public void update() {
		vScrollList.removeAll();
		Resource.every(ChatRoom.class, new ResourceHandler<ChatRoom>(){
			@Override
			public void handler(EndpointManager endpointManager,
					UserManager userManager,
					ResourcePool<ChatRoom> resourcePool, ChatRoom chatRoom) {
				String name = chatRoom.getName();
				ChatroomItem btn = new ChatroomItem();
				btn.setName(name);
				btn.addTo(vScrollList);				
			}
		});
		vScrollList.revalidate();
	}
}
