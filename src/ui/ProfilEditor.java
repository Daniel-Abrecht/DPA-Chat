package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import chat.Chat;
import chat.resources.Profil;
import chat.utils.ImageTools;

@SuppressWarnings("serial")
class ProfilEditor extends JFrame {
	public JTextField nameField = new JTextField();
	public JTextField descField = new JTextField();
	private Image img = new Image();
	protected Profil profil;

	public ProfilEditor() {
		super();
		setTitle("Profil Editor");
		setSize(300, 300);
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
				if (nameField.getText().length() == 0)
					return;
				setVisible(false);
				save();
			}
		});
		btns.add(abord);
		btns.add(change);
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(0, 2));
		content.add(new Label("Name"));
		content.add(nameField);
		content.add(new Label("Beschreibung"));
		content.add(descField);
		nameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		descField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		img.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						img.setImg(ImageTools.mergeIntoViewport(ImageIO.read(file),300,300));
						img.revalidate();
						img.repaint();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0){}
		});
		add(img, BorderLayout.CENTER);
		JPanel x = new JPanel();
		x.setLayout(new BorderLayout());
		x.add(btns, BorderLayout.SOUTH);
		x.add(content, BorderLayout.NORTH);
		add(x,BorderLayout.SOUTH);
	}

	protected void save() {
		profil.setImage(img.getImg());
		profil.setName(nameField.getText());
		profil.setDescription(descField.getText());
		if (!profil.isRegistred()) {
			profil = (Profil) profil.update(Chat.connectionManager.getLocalEndpointManager());
		}
		profil = (Profil) profil.updateRemote();
	}

	public void setTarget(Profil profil) {
		img.setImg(profil.getImage());
		nameField.setText(profil.getName());
		descField.setText(profil.getDescription());
		this.profil = profil;
	}
}
