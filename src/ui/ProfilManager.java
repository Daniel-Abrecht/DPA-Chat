package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import chat.Chat;
import chat.resources.Message;
import chat.resources.Profil;
import chat.resources.ResourcePool;

@SuppressWarnings("serial")
public class ProfilManager extends JFrame {

	private VScrollList vScrollList = new VScrollList();
	private ProfilEditor profilEditor = new ProfilEditor();
	public Profil selectedProfil;
	private ArrayList<ProfilItem> profilItems = new ArrayList<ProfilItem>();
	private JButton editProfil;
	private JButton removeProfil;
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
		addWindowListener(new DefaultWindowCloseListener(){
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				e.getWindow().setVisible(false);
			}
		});
		setLayout(new BorderLayout());

		JLabel l = new JLabel("Profile");
		l.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		add(l, BorderLayout.NORTH);
		add(vScrollList, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 0));

		removeProfil = new JButton("Löschen");
		removeProfil.setEnabled(false);
		JButton newProfil = new JButton("Neu");
		editProfil = new JButton("Bearbeiten");

		buttons.add(removeProfil);
		buttons.add(editProfil);
		editProfil.setEnabled(false);
		buttons.add(newProfil);
		buttons.setPreferredSize(new Dimension(0, 50));

		add(buttons, BorderLayout.SOUTH);

		newProfil.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				profilEditor.setTarget(new Profil());
				profilEditor.setVisible(true);
			}
		});

		editProfil.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				profilEditor.setTarget(selectedProfil);
				profilEditor.setVisible(true);
			}
		});

		removeProfil.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedProfil.deregister();
				removeProfil.setEnabled(false);
				selectedProfil = null;
			}
		});

	}

	private class ProfilItem extends DefaultListItem {
		private JLabel label = new JLabel();
		private Profil profil;

		public ProfilItem(Profil profil) {
			setProfil(profil);
			setLayout(new BorderLayout(50, 50));
			add(label, BorderLayout.CENTER);
			label.setFont(getFont());
			setPreferredSize(new Dimension(0, 50));
			label.addMouseListener(this);
		}

		@Override
		public void onSelect() {
			Chat.setCurrentProfil(profil);
			ChatroomManager.getInstance().setVisible(true);
			ProfilManager.this.setVisible(false);
		}

		@Override
		public void onActive(boolean b) {
			if (b) {
				selectedProfil = profil;
				editProfil.setEnabled(true);
				boolean referenced = profil
						.getEndpointManager()
						.getResourcePool(Message.class)
						.queryTruth(Message.class,
								new ResourcePool.BooleanQuery<Message>() {
									public boolean checkTruth(Message message) {
										return message.getProfil() == profil;
									}
								});
				removeProfil.setEnabled(!referenced);
			} else {
				selectedProfil = null;
				editProfil.setEnabled(false);
				removeProfil.setEnabled(false);
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
		int i = profilItems.size();
		while (i-- > 0) {
			ProfilItem mv = profilItems.get(i);
			Profil profilToCompare = mv.getProfil();
			// speedup & sort
			if (profilToCompare.compareIdentifier(profil) < 0)
				break;
			if (!profilToCompare.hasSameIdentifierAs(profil))
				continue;
			mv.setProfil(profil);
			return;
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
		for (int i = profilItems.size(); i-- > 0;) {
			if (profilItems.get(i).getProfil().getId() == id) {
				System.err.println("remove entry at index " + i);
				vScrollList.remove(i); // JPanel seams to have a bug
//				vScrollList.remove(profilItems.get(i)); // This doesn't work too, damn it swing!
				profilItems.remove(i);
				vScrollList.revalidate();
				vScrollList.repaint();				
				break;
			}
		}
		// bugfix //
		vScrollList.removeAll();
		for(ProfilItem item : profilItems){
			vScrollList.add(item);
		}
		vScrollList.revalidate();
		vScrollList.repaint();				
		////////////
	}

}
