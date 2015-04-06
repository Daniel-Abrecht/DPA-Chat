package chat.resources;

import java.awt.image.BufferedImage;

import chat.utils.ImageBufferEncoder;
import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class Profil extends Resource {
	@Expose(position=0)
	private String name;
	@Expose(position=1)
	private String description;
	@Expose(position=2,customFieldEncoder=ImageBufferEncoder.class)
	private BufferedImage image;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Profil [name=" + name + ", description=" + description + "]";
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
