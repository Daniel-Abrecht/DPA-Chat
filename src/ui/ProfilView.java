package ui;

import java.awt.BorderLayout;
import java.awt.Label;
import javax.swing.JPanel;
import chat.resources.Profil;

@SuppressWarnings("serial")
public class ProfilView extends JPanel {
	private Label name = new Label();
	private Profil profil;
	public ProfilView() {
		setLayout(new BorderLayout());
		add(name,BorderLayout.CENTER);
	}
	public Profil getProfil() {
		return profil;
	}
	public void setProfil(Profil profil) {
		this.profil = profil;
		updateContents();
	}
	private void updateContents() {
		name.setText(profil.getName());
	}
}
