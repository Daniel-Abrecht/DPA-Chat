package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import chat.Chat;
import chat.resources.Profil;

@SuppressWarnings("serial")
public class ProfilManager extends JFrame {
	private VScrollList vScrollList = new VScrollList();
	private ProfilEditor profilEditor = new ProfilEditor();
	public Profil selectedProfil;
	private ArrayList<ProfilItem> profilItems = new ArrayList<ProfilItem>();
	private static ProfilManager profilManager;

	public static ProfilManager getInstance() {
		if (profilManager != null)
			return profilManager;
		return profilManager = new ProfilManager();
	}

	private ProfilManager() {

		super();

		setTitle("User manager");
		setSize(400, 600);
		addWindowListener(new UserManagerListener());
		setLayout(new BorderLayout());

		add(vScrollList, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 0));

		JButton newRoom = new JButton("Neu");
		JButton editRoom = new JButton("Bearbeiten");

		buttons.add(editRoom);
		editRoom.setEnabled(false);
		buttons.add(newRoom);
		buttons.setPreferredSize(new Dimension(0, 50));

		add(buttons, BorderLayout.SOUTH);

		newRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				profilEditor.setTarget(new Profil());
				profilEditor.setVisible(true);
			}
		});

		editRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("rename");
			}
		});

	}

	static class UserManagerListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
		}
	}

	private class ProfilItem extends DefaultListItem {
		private Label label = new Label();
		private Profil profil;

		public ProfilItem(Profil profil) {
			setProfil(profil);
			setLayout(new BorderLayout(50, 50));
			add(label, BorderLayout.CENTER);
			setPreferredSize(new Dimension(0, 50));
			label.addMouseListener(this);
		}

		@Override
		public void onSelect() {
			Chat.currentProfil = profil;
			ChatroomManager.getInstance().setVisible(true);
		}

		@Override
		public void onActive(boolean b) {
			if (b) {
				selectedProfil = profil;
			} else {
				selectedProfil = null;
			}
		}

		public Profil getProfil() {
			return profil;
		}

		public void setProfil(Profil profil) {
			this.profil = profil;
			updateContent();
		}

		private void updateContent() {
			label.setText(profil.getName());
		}

	};

	public void update(Profil profil) {
		Integer id = profil.getId();
		int i;
		for (i = profilItems.size(); i-- > 0;) {
			ProfilItem mv = profilItems.get(i);
			if (mv.getProfil().getId() <= id) {
				if (mv.getProfil().getId() == id) {
					mv.setProfil(profil);
					return;
				} else {
					break;
				}
			}
		}
		if (i < 0)
			i = profilItems.size();
		else
			i++;
		ProfilItem messageView = new ProfilItem(profil);
		profilItems.add(i, messageView);
		messageView.addTo(vScrollList, i);
		vScrollList.revalidate();
	}

	public void remove(Integer id) {
		for (Integer i = profilItems.size(); i-- > 0;) {
			if (profilItems.get(i).getProfil().getId() == id) {
				profilItems.remove(i);
				vScrollList.remove(i);
				break;
			}
		}
	}

}
