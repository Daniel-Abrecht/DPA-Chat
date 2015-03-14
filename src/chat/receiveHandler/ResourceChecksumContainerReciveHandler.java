package chat.receiveHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import chat.Chat;
import chat.checksum.ResourceChecksumContainer;
import chat.checksum.ResourceChecksumContainer.IdChecksumPair;
import chat.resources.Resource;
import chat.resources.ResourceRequest;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;
import static chat.manager.EndpointMap.endpointMap;

public class ResourceChecksumContainerReciveHandler implements ReceiveHandler {
	@Override
	public Class<?> getHandledClass() {
		return ResourceChecksumContainer.class;
	}

	@Override
	public void onReceive(Container container, Endpoint e) {
		ResourceChecksumContainer res = (ResourceChecksumContainer) container
				.getObject();
		List<IdChecksumPair> checksums = res.getChecksums();
		System.err.println(checksums);
		// Requires a copy of the list
		@SuppressWarnings("unchecked")
		List<Resource> list = new ArrayList<Resource>((Collection<Resource>) endpointMap.sync(e)
				.getResourcePoolList().get(res.getRespoolIndex())
				.getResources());
		Collections.sort(list, new Comparator<Resource>() {
			@Override
			public int compare(Resource a, Resource b) {
				return a.compareIdentifier(b);
			}
		});
		Iterator<IdChecksumPair> it1 = checksums.iterator();
		Iterator<Resource> it2 = list.iterator();
		IdChecksumPair c = null;
		Resource r = null;
		int id = 0;
		while (it1.hasNext() || it2.hasNext()) {
			if (c == null && it1.hasNext())
				c = it1.next();
			if (r == null && it2.hasNext()){
				r = it2.next();
				id = r.getId();
			}
			if (r != null && (c == null || id < c.id)) { // Object shouldn't be there
				System.out.println("Unexpected existence of resource '" + r + "' detected");
				r.deregister();
				r = null;
				continue;
			}
			if (c != null && (r == null || id > c.id)) { // Object missing
				System.out.println("Missing resource with id " + c.id + " detected");
				requestResource(c.id,res.getRespoolIndex(),e);
				c = null;
				continue;
			}
			if (c.id == id) { // same object
				if (c.checksum != r.getChecksum()){
					System.out.println("Difference between local copy of resource '" + r + "' and original detected.");
					requestResource(c.id, res.getRespoolIndex(), e);
				}
				c = null;
				r = null;
				continue;
			}
		}
	}

	private void requestResource(int id, int respoolIndex, Endpoint e) {
		System.out.println("New copy of resource " + respoolIndex + ":" + id + " requested");
		Chat.connectionManager.send(e, new ResourceRequest(respoolIndex,id));
	}
}
