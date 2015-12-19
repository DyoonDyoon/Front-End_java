/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Lecture {
	private  String lectureId; // 강의 Id 선언
	private  String userId; // 유저 Id 선언
	
	public Lecture() {	//default constructor
		this.lectureId = null; // 강의 Id 초기화
		this.userId = null; // 유저 Id 초기화
	}
	
	public Lecture(JsonObject json) {	// constructor by json
		this();
		
		if (!json.get("lectureId").isJsonNull()) // 강의 Id가 있을 경우
			this.lectureId = json.get("lectureId").getAsString(); // 해당 값으로 초기화
		if (!json.get("userId").isJsonNull()) // 유저 Id가 있을 경우
			this.userId = json.get("userId").getAsString(); // 해당 값으로 초기화
	}
	
	//private형 변수 lectureId를 반환하는 함수
	public String getLectureId(){
		return lectureId;
	}
	
	//private형 변수 lectureId를 지정해주는 함수
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	//private형 변수 userId를 반환하는 함수
	public String getUserId(){
		return userId;
	}
	
	//private형 변수 userId를 지정해주는 함수
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	//사용자 지정 toString 함수
	public String toString() {
		return "{\n\t'lectureId' : " + lectureId + ",\n\t'userId' : " + userId + "\n}";
	}
}
