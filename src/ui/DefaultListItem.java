package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class DefaultListItem implements MouseListener {
	protected Panel root = new Panel();
	
	private Color cNormal = new Color(200,200,255);
	private Color cHover = new Color(255,255,255);
	private Color cActive = new Color(255,127,0);

	public DefaultListItem() {
		root.setBackground(cNormal);
		root.addMouseListener(this);
	}

	public void addTo(Container c) {
		c.add(root);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		root.setBackground(cHover);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		root.setBackground(cNormal);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		root.setBackground(cActive);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		root.setBackground(cHover);
	}
};
