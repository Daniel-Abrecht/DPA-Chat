package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 * Klasse für Listeneinträge
 * 
 * @author Daniel Abrecht
 */
@SuppressWarnings("serial")
public abstract class DefaultListItem extends JPanel implements MouseListener {
	private Color cNormal = new Color(200, 200, 255);
	private Color cHover = new Color(255, 255, 255);
	private Color cActive = new Color(255, 127, 0);
	private boolean active = false;
	private VScrollList slist;

	public DefaultListItem() {
		super();
		this.setOpaque(true);
		setBackground(cNormal);
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		addMouseListener(this);
	}

	@Override
	public Dimension getPreferredSize() {
		Insets ins = getParent().getInsets();
		return new Dimension(getParent().getSize().width - ins.left - ins.right,
				super.getPreferredSize().height);
	}

	void setActive(boolean b) {
		if (active == b)
			return;
		onActive(b);
		active = b;
		setBackground(active ? cActive : cNormal);
		repaint();
		revalidate();
	}

	public void addTo(VScrollList sc, int i) {
		this.slist = sc;
		sc.add(this, i);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		setBackground(active ? cActive : cHover);
		repaint();
		revalidate();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		setBackground(active ? cActive : cNormal);
		repaint();
		revalidate();
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
