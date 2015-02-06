package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import chat.Chat;
import chat.resources.ChatRoom;

@SuppressWarnings("serial")
class ChatRoomEditor extends JFrame  {
	public JTextField nameField = new JTextField();
	private ChatRoom chatRoom;
	public ChatRoomEditor(){
		super();
		setTitle("Chatroom Editor");
		setSize(300,120);
		setLayout(new BorderLayout());
		JPanel btns = new JPanel();
		btns.setLayout(new GridLayout(1,0));
		btns.setPreferredSize(new Dimension(0,50));
		JButton abord = new JButton("Abbrechen");
		JButton change = new JButton("Übernehmen");
		abord.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		change.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				save();
				if(chatRoom.isRegistred()){
					chatRoom.updateRemote();
				}else{
					chatRoom.register(Chat.currentUserManager);
					chatRoom.updateRemote();
				}
			}
		});
		btns.add(abord);
		btns.add(change);
		add(btns,BorderLayout.SOUTH);
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(0,2));
		content.add(new Label("Name"));
		content.add(nameField);
		nameField.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
		add(content,BorderLayout.CENTER);
	}
	
	protected void save() {
		chatRoom.setName(nameField.getText());
	}
	
	public void setTarget(ChatRoom chatRoom) {
		nameField.setText(chatRoom.getName());
		this.chatRoom = chatRoom;
	}
}
