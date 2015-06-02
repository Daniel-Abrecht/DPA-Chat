package ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chat.resources.Profil;

/**
 * View eines Profils
 * 
 * @author Daniel Abrecht
 */
@SuppressWarnings("serial")
public class ProfilView extends JPanel {
	private static List<ProfilView> profilViews = new ArrayList<ProfilView>();
	private JLabel name = new JLabel();
	private Image img = new Image();
	private Profil profil;

	public ProfilView() {
		setLayout(new BorderLayout());
		add(img, BorderLayout.CENTER);
		add(name, BorderLayout.SOUTH);
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
		img.setImg(profil.getImage());
		name.setText(profil.getName());
	}

	public static void updateViewsWithProfile(Profil profil) {
		if (profil == null)
			return;
		for (Iterator<ProfilView> iterator = profilViews.iterator(); iterator
				.hasNext();) {
			ProfilView view = iterator.next();
			if (view.profil == null)
				continue;
			if (!view.profil.hasSameIdentifierAs(profil))
				continue;
			view.setProfil(profil);
		}
	}
}
