/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Lecture {
	private  String lectureId;
	private  String userId;
	
	public Lecture() {}	//default constructor
	
	public Lecture(JsonObject json) {	// constructor by json
		lectureId = json.get("lectureId").getAsString();
		userId = json.get("userId").getAsString();
	}
	
	public String getLectureId(){
		return lectureId;
	}
	
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	public String getUserId(){
		return userId;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String toString() {
		return "{\n\t'lectureId' : " + lectureId + ",\n\t'userId' : " + userId + "\n}";
	}
}
