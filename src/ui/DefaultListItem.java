package ui;

import java.awt.Color;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class DefaultListItem implements MouseListener {
	protected Panel root = new Panel();

	private Color cNormal = new Color(200, 200, 255);
	private Color cHover = new Color(255, 255, 255);
	private Color cActive = new Color(255, 127, 0);
	private boolean active = false;
	private VScrollList slist;

	void setActive(boolean b) {
		if(active==b)
			return;
		onActive(b);
		active = b;
		root.setBackground(active ? cActive : cNormal);
	}

	public DefaultListItem() {
		root.setBackground(cNormal);
		root.addMouseListener(this);
	}

	public void addTo(VScrollList sc) {
		this.slist = sc;
		sc.add(root);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		root.setBackground(active ? cActive : cHover);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		root.setBackground(active ? cActive : cNormal);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	public abstract void onSelect();
	public abstract void onActive(boolean b);

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (active) {
			onSelect();
		} else {
			slist.setActive(this);
		}
	}

};
