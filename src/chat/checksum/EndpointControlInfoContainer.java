package chat.checksum;

import serialisation.Deserializable;
import serialisation.Expose;

/**
 * Hilfsklasse zum serialisieren und deserialisieren von informationen über Endpoints
 * 
 * @author Daniel Abrecht
 */
@Deserializable
public class EndpointControlInfoContainer {

	@Expose(position=0)
	private Integer rootChecksum;

	/**
	 * Getter für checksumme
	 * @return die checksume
	 */
	public Integer getRootChecksum() {
		return rootChecksum;
	}

	/**
	 * Setter für checksumme
	 * @param rootChecksum neue checksumme
	 */
	public void setRootChecksum(Integer rootChecksum) {
		this.rootChecksum = rootChecksum;
	}

	@Override
	public String toString() {
		return "ChecksumContainer [rootChecksum=" + rootChecksum + "]";
	}

}
