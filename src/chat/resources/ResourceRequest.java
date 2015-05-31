package chat.resources;

import serialisation.Deserializable;
import serialisation.Expose;

/**
 * Hilfsklasse für RessourceRequests
 * 
 * @author Daniel Abrecht
 */
@Deserializable
public class ResourceRequest {
	@Expose(position=0)
	private int respoolIndex = -1;
	@Expose(position = 1)
	private int id = -1;

	/**
	 * Constructor für Deserializer
	 */
	public ResourceRequest(){}

	/**
	 * Alternativer konstruktor
	 * @param respoolIndex Index des RessourcePools
	 * @param id ID der Anzufordernden Ressource
	 */
	public ResourceRequest(int respoolIndex,int id){
		this.respoolIndex = respoolIndex;
		this.id = id;
	}

	/**
	 * Getter für RessourcpoolIndex
	 * @return Index des RessourcePools
	 */
	public int getRespoolIndex() {
		return respoolIndex;
	}

	/**
	 * Setter für RessourcpoolIndex
	 * @param respoolIndex Index des RessourcePools
	 */
	public void setRespoolIndex(int respoolIndex) {
		this.respoolIndex = respoolIndex;
	}

	/**
	 * Getter für id des ResourcePools
	 * @return Die Id des ResourcePools
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter für id des ResourcePools
	 * @return Die Id des ResourcePools
	 */
	public void setId(int id) {
		this.id = id;
	}
	
}
