package ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class DefaultWindowCloseListener extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		if (Utils.VisibleWindowCount() <= 1)
			System.exit(0);
	}
}