/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

public class Assignment {
	private int assignId; // 과제 Id 선언
	private String lectureId; // 강의 Id 선언
	public String title; // 과제 제목 선언
	public String description; // 과제 내용 선언
	public String filePath; // 파일 경로 선언 
	public String startDate; // 시작 날짜 선언
	public String endDate; // 끝나는 날짜 선언
	
	//private형 변수 assignId를 반환하는 함수
	public int getAssignId(){
		return assignId;
	}
	
	//private형 변수 assignId를 지정해주는 함수
	public void setAssignId(int assignId){
		this.assignId = assignId;
	}
	
	//private형 변수 lectureId를 반환하는 함수
	public String getLectureId(){
		return lectureId;
	}
	
	//private형 변수 lectureId를 지정해주는 함수
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	//사용자 지정 toString 함수
	public String toString() {
		return "{\n\t'assignId' : " + assignId + ",\n\t'lectureId' : " + lectureId + 
				"\n\t'description' : " + description + ",\n\t'filePath' : " + filePath +
				",\n\t'startDate' : " + startDate + ",\n\t'endDate' : " + endDate + "\n}";
	}
	
}
