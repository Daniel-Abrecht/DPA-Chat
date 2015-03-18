package ui;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import static chat.Chat.*;

@SuppressWarnings("serial")
class AboutDialog extends JFrame {
	private static AboutDialog instance;

	private AboutDialog() {
		super();
		setTitle("Über DPA-Chat");
		setSize(600, 400);
		setResizable(false);
		JTextArea infotext = new JTextArea(
				appName + " v" +version + "\n"
			+	"Von " + developer + "\n\n\n"
			+ licence
		);
		infotext.setEditable(false);
		add(infotext);
	}

	public static AboutDialog getInstance() {
		if (instance != null)
			return instance;
		return instance = new AboutDialog();
	}
}
