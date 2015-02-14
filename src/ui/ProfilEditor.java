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
import connectionManager.User;
import chat.manager.UserManager;
import chat.resources.Profil;

@SuppressWarnings("serial")
class ProfilEditor extends JFrame {
	public JTextField nameField = new JTextField();
	protected Profil profil;

	public ProfilEditor() {
		super();
		setTitle("Profil Editor");
		setSize(300, 120);
		setLayout(new BorderLayout());
		JPanel btns = new JPanel();
		btns.setLayout(new GridLayout(1, 0));
		btns.setPreferredSize(new Dimension(0, 50));
		JButton abord = new JButton("Abbrechen");
		JButton change = new JButton("Übernehmen");
		abord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		change.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				save();
			}
		});
		btns.add(abord);
		btns.add(change);
		add(btns, BorderLayout.SOUTH);
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(0, 2));
		content.add(new Label("Name"));
		content.add(nameField);
		nameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(content, BorderLayout.CENTER);
	}

	protected void save() {
		profil.setName(nameField.getText());
		profil.updateRemote();
	}

	public void setTarget(Profil profil) {
		if (!profil.isRegistred()) {
			UserManager um = new UserManager(new User());
			profil.register(um);
		}
		nameField.setText(profil.getName());
		this.profil = profil;
	}
}
