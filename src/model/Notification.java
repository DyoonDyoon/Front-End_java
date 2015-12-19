/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

public class Notification {
	private int notificationId; // 공지 번호
	private String lectureId; // 강의 번호
	public String title; // 공지 제목
	public String description; // 공지 내용
		
	//공지 번호를 반환하는 함수
	public int getNotificationId(){
		return notificationId;
	}
	
	//공지 번호를 지정하는 함수
	public void setNotificationId(int notificationId){
		this.notificationId = notificationId;
	}
	
	// 강의 번호를 지정하는 함수
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	// 강의 번호를 반환하는 함수
	public String getLectureId(){
		return this.lectureId;
	}
	
	//사용자 지정 toString 함수
	public String toString() {
		return "{\n\t'lectureId' : " + lectureId + ",\n\t'title' : " + title + ",\n\t'description' : " + description +"\n}";
	}
}

