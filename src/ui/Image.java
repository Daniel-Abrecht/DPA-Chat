package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
class Image extends Component {

	BufferedImage img;

	public void paint(Graphics g) {
		Dimension area = getSize();
		Rectangle imgSize = new Rectangle(img.getWidth(), img.getHeight());
		if (area.width == 0 || area.height == 0 || imgSize.isEmpty())
			return;
		if (imgSize.width / imgSize.height < area.width / area.height) {
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

	public void loadImage(String path) {
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Image(String path) {
		loadImage(path);
	}

	public Dimension getPreferredSize() {
		if (img == null) {
			return new Dimension(100, 100);
		} else {
			Dimension area = getSize();
			Rectangle imgSize = new Rectangle(img.getWidth(), img.getHeight());
			if (area.width == 0 || area.height == 0 || imgSize.isEmpty())
				return new Dimension(100, 100);
			if (imgSize.width / imgSize.height > area.height / area.width) {
				return new Dimension(area.width, imgSize.height * area.width
						/ imgSize.width);
			} else {
				return new Dimension(imgSize.width * area.height
						/ imgSize.height, area.height);
			}
		}
	}
}