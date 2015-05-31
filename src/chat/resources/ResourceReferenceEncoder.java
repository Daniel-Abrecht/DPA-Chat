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

/**
 * Custom serializer für BinaryEncoder, um ausschlieslich
 * aufgrund der Id einer Ressource dessen Referenz der Instanz im ResourcePool zu Ermitteln
 * 
 * @author Daniel Abrecht
 */
public class ResourceReferenceEncoder implements CustomFieldEncoder {

	/**
	 * Hilfsklasse zur (de)serialisierung
	 * 
	 * @author Daniel Abrecht
	 */
	@Deserializable
	public static class ResourceReference {
		@Expose(position = 0)
		public int resourceId = -1;
		@Expose(position = 0)
		public InetAddress endpointIp;
	};

	/**
	 * Serializer
	 * @see serialisation.CustomFieldEncoder
	 */
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

	/**
	 * Deserializer
	 * @see serialisation.CustomFieldEncoder
	 */
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
