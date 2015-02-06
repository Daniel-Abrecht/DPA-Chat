package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class VScrollList extends JScrollPane {
	private Panel listPanel = new Panel();
	private Panel listHelperPanel;
	DefaultListItem active;

	public VScrollList(Panel panel) {
		super(panel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listHelperPanel = panel;
		listPanel.setLayout(new GridLayout(0, 1));
		listHelperPanel.setLayout(new BorderLayout());
		listHelperPanel.add(listPanel, BorderLayout.NORTH);
		listHelperPanel.revalidate();
	}

	@Override
	public void setBackground(Color bg){
		if(listHelperPanel!=null)
			listHelperPanel.setBackground(bg);
		super.setBackground(bg);
	}
	
	public VScrollList(){
		this(new Panel());
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
		if(listPanel!=null)
			listPanel.revalidate();
		else super.revalidate();
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
