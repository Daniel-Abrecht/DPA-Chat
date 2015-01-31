package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.TextField;

@SuppressWarnings("serial")
public class ProfileManager extends Frame {
	private TextField name = new TextField();
	private Image img = new Image("resources/defaultProfileImage.png");
	public ProfileManager() {
		setLayout(new BorderLayout());
		add(name,BorderLayout.NORTH);
		add(img,BorderLayout.WEST);
		setSize(300, 200);
		name.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
		name.setText("Benutzername");
	}
}
