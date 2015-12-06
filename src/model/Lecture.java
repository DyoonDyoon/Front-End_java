/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

public class Lecture {
	private  String lectureId;
	private  String userId;
	
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
