package chat.checksum;

import java.util.ArrayList;
import java.util.List;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class ChecksumContainer {

	@Expose(position=0)
	private Integer rootChecksum;
	@Expose(position=1)
	List<Integer> resPoolChecksums;

	public List<Integer> getResPoolChecksums() {
		return resPoolChecksums == null ? resPoolChecksums = new ArrayList<Integer>()
				: resPoolChecksums;
	}

	public boolean hasResPoolChecksums() {
		return resPoolChecksums != null && resPoolChecksums.size() != 0;
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
