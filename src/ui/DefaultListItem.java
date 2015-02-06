package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Panel;

@SuppressWarnings("serial")
public abstract class DefaultListItem extends Panel implements MouseListener {
	private Color cNormal = new Color(200, 200, 255);
	private Color cHover = new Color(255, 255, 255);
	private Color cActive = new Color(255, 127, 0);
	private boolean active = false;
	private VScrollList slist;

	public DefaultListItem() {
		super();
		setBackground(cNormal);
		setFont(new Font(Font.SANS_SERIF,Font.PLAIN,18));
		addMouseListener(this);
	}

	void setActive(boolean b) {
		if (active == b)
			return;
		onActive(b);
		active = b;
		setBackground(active ? cActive : cNormal);
	}

	public void addTo(VScrollList sc, int i) {
		this.slist = sc;
		sc.add(this, i);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		setBackground(active ? cActive : cHover);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		setBackground(active ? cActive : cNormal);
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
