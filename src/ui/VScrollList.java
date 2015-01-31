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
	public VScrollList() {
		super(ScrollPane.SCROLLBARS_AS_NEEDED);
		listPanel.setLayout(new GridLayout(0, 1));
		listHelperPanel.setLayout(new BorderLayout());
		listHelperPanel.add(listPanel, BorderLayout.NORTH);
		super.add(listHelperPanel);
	}
	@Override
	public Component add(Component comp){
		return listPanel.add(comp);
	}
	@Override
	public void revalidate(){
		listPanel.revalidate();
	}
	@Override
	public void removeAll(){
		listPanel.removeAll();
	}
}
