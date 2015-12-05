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
}
