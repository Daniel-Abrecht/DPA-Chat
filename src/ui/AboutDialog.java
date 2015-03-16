package ui;

import javax.swing.JFrame;

@SuppressWarnings("serial")
class AboutDialog extends JFrame {
	private static AboutDialog instance;

	private AboutDialog() {
		super();
		setTitle("Über DPA-Chat");
		setSize(300, 120);
	}

	public static AboutDialog getInstance() {
		if (instance != null)
			return instance;
		return instance = new AboutDialog();
	}
}
