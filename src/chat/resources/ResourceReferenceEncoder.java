package chat.resources;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import chat.Chat;
import chat.manager.EndpointManager;
import serialisation.CustomFieldEncoder;
import serialisation.Deserializable;
import serialisation.Expose;
import serialisation.ObjectEncoder;

public class ResourceReferenceEncoder implements CustomFieldEncoder {

	@Deserializable
	public static class ResourceReference {
		@Expose(position = 0)
		public int resourceId = -1;
		@Expose(position = 0)
		public InetAddress endpointIp;
	};

	@Override
	public ResourceReference encodeField(ObjectEncoder<?> encoder, Field f,
			Object o) {
		ResourceReference ref = new ResourceReference();
		if (!(o instanceof Resource))
			return ref;
		Resource r = (Resource) o;
		ref.resourceId = r.getId();
		if (!r.isLocal())
			ref.endpointIp = r.getEndpointManager().getEndpoint().getAddress();
		return ref;
	}

	@Override
	public Object decodeField(ObjectEncoder<?> encoder, Field f, ByteBuffer o) {
		ResourceReference ref = encoder.decode(o, ResourceReference.class);
		if (ref.resourceId < 0)
			return null;
		if( ref.endpointIp == null ){
			ref.endpointIp = (InetAddress)encoder.getParameter("endpointIp");
		}
		System.out.println(ref.endpointIp);
		EndpointManager em = Chat.connectionManager
				.getEndpointManager(ref.endpointIp);
		@SuppressWarnings("unchecked")
		Class<? extends Resource> c = (Class<? extends Resource>) f.getType();
		return em.getResourcePool(c).getOrCreateResource(ref.resourceId, c);
	}

}
