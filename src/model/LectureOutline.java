/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class LectureOutline{
	private String lectureId; // 학수번호
	public String professorName; // 교수명
	public String title; // 강의명
	public String curriculum; // 교과과정
	private int point; // 학점

	public LectureOutline() {
		// default constructor
		this.lectureId = null; // 학수 번호 초기화
		this.professorName = null; // 교수명 초기화
		this.title = null; // 강의 명 초기화
		this.curriculum = null; // 교과과정 초기화
		this.point = 0; // 학점 초기화
	}
	
	//json을 이용한 생성자
	public LectureOutline(JsonObject json) {
		this();
		
		if (!json.get("lecture_id").isJsonNull()) // 학수 번호가 존재할 경우
			this.lectureId = json.get("lecture_id").getAsString(); // 해당 값으로 초기화
		if (!json.get("professor_name").isJsonNull()) // 교수명이 존재할 경우
			this.professorName = json.get("professor_name").getAsString(); // 해당 값으로 초기화
		if (!json.get("title").isJsonNull()) // 강의 명이 존재할 경우
			this.title = json.get("title").getAsString(); // 해당 값으로 초기화
		if (!json.get("curriculum").isJsonNull()) // 교과과정이 존재할 경우
			this.curriculum = json.get("curriculum").getAsString(); // 해당 값으로 초기화
		String pointStr = "0"; 
		if (!json.get("point").isJsonNull()) // 해당 학점이 존재할 경우
			pointStr = json.get("point").getAsString(); // 해당 값을 변수에 저장
		this.point = Integer.parseInt(pointStr); // 해당 값으로 초기화
	}
	
	//다른 매개변수를 이용한 생성자
	public LectureOutline(String lectureId, String professorName, String title, String curriculum, int point) {
		//모든 매개변수를 통해 필드를 모두 초기화
		this.lectureId = lectureId;
		this.professorName = professorName;
		this.title = title;
		this.curriculum = curriculum;
		this.point = point;
	}
	
	//학수 번호 값을 반환하는 함수
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	//학수 번호 값을 지정하는 함수
	public String getLectureId(){
		return this.lectureId;
	}

	//학점을 반환하는 함수
	public int getPoint(){
		return this.point;
	}
	
	//학점을 지정하는 함수
	public boolean setPoint(int point){
		this.point = point;
		return true;
	}
	
	//사용자 지정 toString 함수
	public String toString() {
		return "{\n\t'lectureId' : " + lectureId + ",\n\t'professorName' : " + professorName +
				",\n\t'title' : " + title + ",\n\t'curriculum' : " + curriculum + ",\n\t'point' : "
				+ point + "\n}";
	}
}