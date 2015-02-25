package chat.checksum;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class ChecksumContainer {

	@Expose
	private Integer rootChecksum;

	public Integer getRootChecksum() {
		return rootChecksum;
	}

	public void setRootChecksum(Integer rootChecksum) {
		this.rootChecksum = rootChecksum;		
	}

	@Override
	public String toString() {
		return "ChecksumContainer [rootChecksum=" + rootChecksum + "]";
	}

}
