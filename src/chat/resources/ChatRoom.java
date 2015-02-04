package chat.resources;

import ui.ChatRoomView;

import com.google.gson.annotations.Expose;

import connectionManager.Deserializable;

@Deserializable
public class ChatRoom extends Resource {
	@Expose
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ChatRoom [name=" + name + "]";
	}

	private ChatRoomView chv;
	
	public ChatRoomView getView(){
		if(chv==null)
			chv=new ChatRoomView();
		return chv;
	}
	
	public void display() {
		getView().setVisible(true);
	}

}
