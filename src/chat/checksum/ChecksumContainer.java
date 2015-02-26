package chat.checksum;

import java.util.ArrayList;
import java.util.List;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class ChecksumContainer {

	@Expose
	private Integer rootChecksum;
	@Expose
	List<Integer> resPoolChecksums;

	public List<Integer> getResPoolChecksums() {
		return resPoolChecksums == null ? resPoolChecksums = new ArrayList<Integer>()
				: resPoolChecksums;
	}

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

	public void addRespoolChecksum(int checksum) {
		getResPoolChecksums().add(checksum);
	}

}
