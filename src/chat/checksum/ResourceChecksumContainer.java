package chat.checksum;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import serialisation.CustomFieldEncoder;
import serialisation.Deserializable;
import serialisation.Expose;
import serialisation.ObjectEncoder;
import chat.resources.Resource;
import chat.resources.ResourcePool;
import static utils.BinaryUtils.*;

@Deserializable
public class ResourceChecksumContainer {

	public static class resourceChecksumsCustomEncoder implements CustomFieldEncoder {

		@Override
		public Object encodeField(ObjectEncoder<?> encoder, Field f, Object o) {
			List<byte[]> result = new ArrayList<byte[]>();
			if (o == null) {
				result.add(toBytesIncludeNull(-1));
				return result;
			}
			List<IdChecksumPair> icpl = ((ChecksumHolder)o).resourceChecksums;
			int lastId = -1;
			int contains = 0;
			List<byte[]> nextChunck = null;
			for (IdChecksumPair icp : icpl) {
				int distance = icp.id - lastId;
				if (distance > 1) {
					if( nextChunck != null){
						result.add(toBytesIncludeNull(contains));
						result.addAll(nextChunck);
					}
					result.add(toBytesIncludeNull(distance));
					nextChunck = new ArrayList<byte[]>();
					contains = 0;
				}
				nextChunck.add(toBytes(icp.checksum));
				contains++;
				lastId = icp.id;
			}
			result.add(toBytesIncludeNull(contains));
			if( nextChunck != null){
				result.addAll(nextChunck);
			}
			result.add(toBytesIncludeNull(null));
			return result;
		}

		@Override
		public Object decodeField(ObjectEncoder<?> encoder, Field f,
				ByteBuffer o) {
			byte[] dst = new byte[4];
			int id = 0;
			ChecksumHolder ch = new ChecksumHolder();
			ch.resourceChecksums = new ArrayList<IdChecksumPair>();
			while(true){
				Integer offset = asIntegerOrNull(o);
				if (offset == null || id < 0)
					break;
				if(offset < 0)
					return null;
				Integer chunckSize = asIntegerOrNull(o);
				if (chunckSize == null || chunckSize < 0)
					break;
				id += offset;
				while (chunckSize-- > 0) {
					o.get(dst, 0, 4);
					int checksum = asInt(dst, 0);
					IdChecksumPair icp = new IdChecksumPair();
					icp.id = id;
					icp.checksum = checksum;
					ch.resourceChecksums.add(icp);
					id++;
				}
			}
			return ch;
		}
	}
	
	public static class IdChecksumPair implements Comparable<IdChecksumPair> {
		public Integer id;
		public Integer checksum;

		@Override
		public int compareTo(IdChecksumPair that) {
			return id - that.id;
		}

		@Override
		public String toString() {
			return "[" + id + "|" + checksum + "]";
		}

	}
	
	public static class ChecksumHolder {
		private List<IdChecksumPair> resourceChecksums;

		@Override
		public String toString() {
			return "ChecksumHolder [resourceChecksums=" + resourceChecksums
					+ "]";
		}
		
	}
	
	// Helper, allows using a custom encoder
	@Expose(position=1,customFieldEncoder=resourceChecksumsCustomEncoder.class)
	public ChecksumHolder checksumHolder = new ChecksumHolder();
	
	public void setChecksums(ResourcePool<? extends Resource> resourcePool) {
		checksumHolder.resourceChecksums = new ArrayList<IdChecksumPair>();
		for (Resource resource : resourcePool.getResources()) {
			IdChecksumPair icp = new IdChecksumPair();
			icp.id = resource.getId();
			icp.checksum = resource.getChecksum();
			checksumHolder.resourceChecksums.add(icp);
		}
		Collections.sort( checksumHolder.resourceChecksums );
	}

	@Override
	public String toString() {
		return "ResourceChecksumContainer [checksumHolder=" + checksumHolder
				+ "]";
	}

	public List<IdChecksumPair> getChecksums() {
		return checksumHolder.resourceChecksums;
	}

}
