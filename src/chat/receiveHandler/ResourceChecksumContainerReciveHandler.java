package chat.receiveHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import chat.checksum.ResourceChecksumContainer;
import chat.checksum.ResourceChecksumContainer.IdChecksumPair;
import chat.manager.EndpointManager;
import chat.resources.Resource;
import chat.resources.ResourcePool;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;
import static chat.Chat.connectionManager;

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
		List<Resource> list = (List<Resource>) connectionManager
				.getLocalEndpointManager().getResourcePoolList()
				.get(res.getRespoolIndex()).getResources();
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
		while (it1.hasNext() && it2.hasNext()) {
			if (c == null)
				c = it1.next();
			if (r == null)
				r = it2.next();
			int id = r.getId();
			if (c.id == id) { // same object
				
				c = null;
				r = null;
				continue;
			}
			if (c.id < id) { // Object shouldn't be there
				
				c = null;
				continue;
			}
			if (id < c.id) { // Object missing
				
				r = null;
				continue;
			}
		}
	}
}
