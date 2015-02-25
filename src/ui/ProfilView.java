package ui;

import java.awt.BorderLayout;
import java.awt.Label;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import chat.resources.Profil;

@SuppressWarnings("serial")
public class ProfilView extends JPanel {
	private static List<ProfilView> profilViews = new ArrayList<ProfilView>();
	private Label name = new Label();
	private Profil profil;

	public ProfilView() {
		setLayout(new BorderLayout());
		add(name, BorderLayout.NORTH);
		profilViews.add(this);
	}

	public Profil getProfil() {
		return profil;
	}

	public void setProfil(Profil profil) {
		this.profil = profil;
		updateContents();
	}

	private void updateContents() {
		if (profil == null)
			return;
		name.setText(profil.getName());
	}

	public static void updateViewsWithProfile(Profil profil) {
		for (Iterator<ProfilView> iterator = profilViews.iterator(); iterator
				.hasNext();) {
			ProfilView view = iterator.next();
			if (view.profil == null)
				continue;
			if (!view.profil.getId().equals(profil.getId()))
				continue;
			if (view.profil.getEndpointManager() != profil.getEndpointManager())
				continue;
			view.setProfil(profil);
		}
	}
}
