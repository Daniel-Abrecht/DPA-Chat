package chat.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Hilfsklasse zur Manipulation von BufferedImage objekten
 * 
 * @author Daniel Abrecht
 */
public class ImageTools {

	/**
	 * Image nach BufferedImage konvertieren
	 * @param img
	 * @return das Image als BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	/**
	 * Skalieren eines BufferedImage unter berücksichtigung des AspectRatio
	 * @param img Das Bild
	 * @param vw Maximale Breite
	 * @param vh Maximale Höhe
	 * @return Skaliertes bild
	 */
	public static BufferedImage mergeIntoViewport(BufferedImage img, int vw, int vh) {
		int w = img.getWidth();
		int h = img.getHeight();
		if (w < vw && h < vh)
			return img;
		if (w * vh / h < vw) {
			w = w * vh / h;
			h = vh;
		} else {
			h = h * vw / w;
			w = vw;
		}
		System.out.println(w+":"+h);
		return toBufferedImage(img.getScaledInstance(w,h,BufferedImage.SCALE_SMOOTH));
	}

	/**
	 * Berechnet den Abstand zum Rand eines Zentrierten Bildes
	 * @param img Grösse des Bildes
	 * @param viewport grösse des Sichtfelds
	 */
	public static Dimension calculateCenterMarigin(Dimension img, Dimension viewport) {
		return new Dimension(
			(viewport.width - img.width) / 2,
			(viewport.height - img.height) / 2
		);
	}
}
