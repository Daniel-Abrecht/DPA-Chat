package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

@SuppressWarnings("serial")
class Image extends JComponent {

	BufferedImage img;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(img==null)
			return;
		Dimension area = getSize();
		Rectangle imgSize = new Rectangle(img.getWidth(), img.getHeight());
		if (area.width == 0 || area.height == 0 || imgSize.isEmpty())
			return;
		if (imgSize.width * area.height / imgSize.height <  area.width) {
			g.drawImage(
					img.getScaledInstance(imgSize.width * area.height
							/ imgSize.height, area.height, 0),
					(area.width - imgSize.width * area.height / imgSize.height) / 2,
					0, null);
		} else {
			g.drawImage(
					img.getScaledInstance(area.width, imgSize.height
							* area.width / imgSize.width, 0),
					0,
					(area.height - imgSize.height * area.width / imgSize.width) / 2,
					null);
		}
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
		revalidate();
		repaint();
	}
	
}