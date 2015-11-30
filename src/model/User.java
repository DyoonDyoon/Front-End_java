/**
 * Created by kweonminjun on 2015. 11. 30..
 */
package model;

public abstract class User {
	protected String id;
	private String name;
	
	// Setter
	public void setName(String name) {
		this.name = name;
	}
	
	// Getter
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
}
