package chat.resources;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class ResourceRequest {
	@Expose(position=0)
	private int respoolIndex = -1;
	@Expose(position = 1)
	private int id = -1;

	public ResourceRequest(){}
	public ResourceRequest(int respoolIndex,int id){
		this.respoolIndex = respoolIndex;
		this.id = id;
	}

	public int getRespoolIndex() {
		return respoolIndex;
	}

	public void setRespoolIndex(int respoolIndex) {
		this.respoolIndex = respoolIndex;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
