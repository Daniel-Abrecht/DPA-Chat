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
import chat.resources.Profil;

@SuppressWarnings("serial")
class ProfilEditor extends JFrame {
	public JTextField nameField = new JTextField();
	public JTextField descField = new JTextField();
	protected Profil profil;

	public ProfilEditor() {
		super();
		setTitle("Profil Editor");
		setSize(300, 140);
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
		content.add(new Label("Beschreibung"));
		content.add(descField);
		nameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		descField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(content, BorderLayout.CENTER);
	}

	protected void save() {
		profil.setName(nameField.getText());
		profil.setDescription(descField.getText());
		profil.updateRemote();
	}

	public void setTarget(Profil profil) {
		if (!profil.isRegistred()) {
			profil.register(Chat.connectionManager.getLocalEndpointManager());
		}
		nameField.setText(profil.getName());
		descField.setText(profil.getDescription());
		this.profil = profil;
	}
}
