/**
 * Created by kweonminjun on 2015. 11. 30..
 */
package model;

import com.google.gson.JsonObject;

public class User {
	private String id; // 유저 ID
	public String name; // 유저 이름
	public String major; // 유저 전공
	private int type; // 유저 type (0:학생, 1:교수)
	
	// json을 이용한 생성자
	public User(JsonObject json) {
		// json을 이용해 해당 값으로 초기화 시킴
		this.id = json.get("userId").getAsString(); 
		this.name = json.get("name").getAsString();
		this.major = json.get("major").getAsString();
		this.type = json.get("type").getAsInt();
	}
	
	//다른 매개 변수를 통한 생성자
	public User(String id, String name, String major, int type) {
		// 입력받은 매개변수를 가지고 초기화
		this.id = id;
		this.name = name;
		this.major = major;
		this.type = type;
	}
	
	// Getter
	// 유저 Id를 반환하는 함수
	public String getId() {
		return id;
	}
	
	// 유저 type을 반환하는 함수
	public int getType() {
		return type;
	}
}
