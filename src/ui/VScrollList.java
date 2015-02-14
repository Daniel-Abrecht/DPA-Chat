package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

@SuppressWarnings("serial")
public class VScrollList extends JScrollPane {
	static public class VPanel extends JPanel implements Scrollable {
		public VPanel() {
			super();
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 0;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 0;
		}
	}

	private VPanel listPanel = new VPanel();
	private VPanel listHelperPanel;
	DefaultListItem active;

	public VScrollList(VPanel panel) {
		super(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listHelperPanel = panel;
		listPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		listHelperPanel.setLayout(new BorderLayout());
		listHelperPanel.add(listPanel, BorderLayout.NORTH);
	}

	public VScrollList() {
		this(new VPanel());
	}

	@Override
	public Component add(Component comp) {
		return listPanel.add(comp);
	}

	@Override
	public Component add(Component comp, int i) {
		return listPanel.add(comp, i);
	}

	@Override
	public void revalidate() {
		if (listPanel != null)
			listPanel.revalidate();
		else
			super.revalidate();
	}

	@Override
	public void removeAll() {
		listPanel.removeAll();
	}

	public void setActive(DefaultListItem active) {
		if (this.active != null)
			this.active.setActive(false);
		if (active != null)
			active.setActive(true);
		this.active = active;
	}

	public DefaultListItem getActive() {
		return active;
	}

}
