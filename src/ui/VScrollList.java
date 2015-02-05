package ui;

import java.awt.Component;
import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;

@SuppressWarnings("serial")
public class VScrollList extends ScrollPane {
	private Panel listPanel = new Panel();
	private Panel listHelperPanel = new Panel();
	DefaultListItem active;

	public VScrollList() {
		super(ScrollPane.SCROLLBARS_AS_NEEDED);
		listPanel.setLayout(new GridLayout(0, 1));
		listHelperPanel.setLayout(new BorderLayout());
		listHelperPanel.add(listPanel, BorderLayout.NORTH);
		super.add(listHelperPanel);
	}

	@Override
	public Component add(Component comp) {
		return listPanel.add(comp);
	}

	@Override
	public  Component add(Component comp, int i) {
		return listPanel.add(comp,i);
	}

	@Override
	public void revalidate() {
		listPanel.revalidate();
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
