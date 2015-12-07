/**
 * Created by kweonminjun on 2015. 11. 30..
 */
package model;

import com.google.gson.JsonObject;

public class User {
	private String id;
	public String name;
	public String major;
	private int type;
	
	public User(JsonObject json) {
		this.id = json.get("userId").getAsString();
		this.name = json.get("name").getAsString();
		this.major = json.get("major").getAsString();
		this.type = json.get("type").getAsInt();
	}
	
	public User(String id, String name, String major, int type) {
		this.id = id;
		this.name = name;
		this.major = major;
		this.type = type;
	}
	
	// Getter
	public String getId() {
		return id;
	}
	public int getType() {
		return type;
	}
}
