package chat.utils;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import serialisation.CustomFieldEncoder;
import serialisation.ObjectEncoder;
import utils.BinaryUtils;

public class ImageBufferEncoder implements CustomFieldEncoder {

	@Override
	public Object encodeField(ObjectEncoder<?> encoder, Field f, Object o) {
		if(o==null)
			return new byte[]{0,0,0,0,0,0,0,0};
		BufferedImage imgbuf = (BufferedImage) o;
		int w = imgbuf.getWidth();
		int h = imgbuf.getHeight();
		byte imgDatas[] = new byte[8 + w * h * 4];
		imgDatas[0] = (byte) (w >> 24);
		imgDatas[1] = (byte) (w >> 16);
		imgDatas[2] = (byte) (w >> 8);
		imgDatas[3] = (byte) w;
		imgDatas[4] = (byte) (h >> 24);
		imgDatas[5] = (byte) (h >> 16);
		imgDatas[6] = (byte) (h >> 8);
		imgDatas[7] = (byte) h;
		for (int i = w * h; i-- > 0;) {
			int rgb = imgbuf.getRGB(i % w, i / w);
			imgDatas[8 + i * 4 + 0] = (byte) (rgb >> 24); // a
			imgDatas[8 + i * 4 + 1] = (byte) (rgb >> 16); // r
			imgDatas[8 + i * 4 + 2] = (byte) (rgb >> 8); // g
			imgDatas[8 + i * 4 + 3] = (byte) rgb; // b
		}
		return imgDatas;
	}

	@Override
	public Object decodeField(ObjectEncoder<?> encoder, Field f, ByteBuffer o) {
		byte buf[] = new byte[4];
		o.get(buf, 0, 4);
		int w = BinaryUtils.asInt(buf, 0);
		o.get(buf, 0, 4);
		int h = BinaryUtils.asInt(buf, 0);
		if(w==0||h==0)
			return null;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		for (int i = w * h; i-- > 0;) {
			o.get(buf, 0, 4);
			int rgb = BinaryUtils.asInt(buf, 0);
			img.setRGB(i % w, i / w, rgb);
		}
		return img;
	}

}
