/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Question {
	private int questionId; // 질문 번호
	private String lectureId; // 강의 번호
	private String studentId; // 학번
	public String content; // 내용
	
	public Question() {
		//default constructor
		this.questionId = 0; // 질문 번호 초기화
		this.lectureId = null; // 강의 번호 초기화
		this.studentId = null; // 학번 초기화
		this.content = null; // 내용 초기화 
	}
	
	//json을 이용한 생성자
	public Question(JsonObject json) {
		if (!json.get("questionId").isJsonNull()) // 질문 번호가 존재하는 경우
			this.questionId = json.get("questionId").getAsInt(); // 해당 값으로 초기화
		if (!json.get("lectureId").isJsonNull()) // 강의 번호가 존재하는 경우
			this.lectureId = json.get("lectureId").getAsString(); // 해당 값으로 초기화
		if (!json.get("stuId").isJsonNull()) // 학번이 존재할 경우
			this.studentId = json.get("stuId").getAsString(); // 해당 값으로 초기화
		if (!json.get("content").isJsonNull()) // 내용이 존재할 경우
			this.content = json.get("content").getAsString(); // 해당 값으로 초기화
	}
	
	// 질문 번호를 반환하는 함수
	public int getQuestionId(){
		return questionId;
	}
	
	// 질문 번호를 지정하는 함수
	public void setQuestionId(int questionId){
		this.questionId = questionId;
	}
	
	// 강의 번호를 지정하는 함수
	public String getLectureId(){
		return lectureId;
	}
	
	// 강의 번호를 반환하는 함수
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	// 학번을 반환하는 함수
	public String getStudentId(){
		return studentId;
	}
	
	// 학번을 지정하는 함수
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	
	// 사용자 지정 toString 함수
	public String toString() {
		return "{\n\t'questionId' : " + questionId + "\n\t'lectureId' : " + lectureId + ",\n\t'studentId' : " + studentId + ",\n\t'content' : " + content + "\n}";
	}
}
