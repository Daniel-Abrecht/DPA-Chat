package ui;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import static chat.Chat.*;

@SuppressWarnings("serial")
class AboutDialog extends JFrame {
	private static AboutDialog instance;

	private AboutDialog() {
		super();
		setTitle("Über DPA-Chat");
		setSize(600, 450);
		setResizable(false);
		addWindowListener(new DefaultWindowCloseListener(){
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				e.getWindow().setVisible(false);
			}
		});
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
