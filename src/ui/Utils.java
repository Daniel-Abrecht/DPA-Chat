package ui;

import java.awt.Window;

/**
 * Hilfreiche Funktionen mit Bezug auf UI
 * 
 * @author Daniel Abrecht
 */
public class Utils {
	static int VisibleWindowCount() {
		int n = 0;
		Window[] wins = Window.getOwnerlessWindows();
		for (Window w : wins)
			if (w.isVisible())
				n++;
		return n;
	}
}
