package chat.resources;

import java.awt.image.BufferedImage;

import chat.utils.ImageBufferEncoder;
import serialisation.Deserializable;
import serialisation.Expose;

/**
 * Hilfsklasse zur (De)serialisierung von Profil Ressourcen,
 * Repräsentiert ein Profil
 * 
 * @author Daniel Abrecht
 * @see chat.resources.Resource
 */
@Deserializable
public class Profil extends Resource {
	@Expose(position=0)
	private String name;
	@Expose(position=1)
	private String description;
	@Expose(position=2,customFieldEncoder=ImageBufferEncoder.class)
	private BufferedImage image;

	/**
	 * Getter für Beschreibung
	 * @return Die Beschreibung
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter für die Beschreibung
	 * @param description die neue Beschreibung
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter für Profilname
	 * @return der Profilname
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter für Profilname
	 * @param name der Profilname
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Profil [name=" + name + ", description=" + description + "]";
	}

	/**
	 * Gettter für Profilbild
	 * @return das Profilbild
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Setter für Profilbild
	 * @param image das Profilbild
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
