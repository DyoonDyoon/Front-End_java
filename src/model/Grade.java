/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Grade {
	private int gradeId; // 성적 Id 선언
	private String lectureId; // 강의 Id 선언
	private int submitId; // 제출 Id 선언
	private String studentId; // 학번 선언
	private double score; // 성적 점수 선언
	
	//Grade 객체 기본 생성자
	public Grade() {
		this.gradeId = 0; // 성적 Id 초기화
		this.lectureId = null; // 강의 번호 초기화
		this.submitId = 0; // 제출 Id 초기화
		this.studentId = null; // 학번 초기화
		this.score = 0; // 점수 초기화
	}
	
	public Grade(JsonObject json) {
		this();
		if(!json.get("gradeId").isJsonNull()) // 성적 Id가 있을 경우
			this.gradeId = json.get("gradeId").getAsInt(); // 해당 값으로 초기화
		if(!json.get("lectureId").isJsonNull()) // 강의 Id가 있을 경우
			this.lectureId = json.get("lectureId").getAsString(); // 해당 값으로 초기화
		if(!json.get("submitId").isJsonNull()) // 제출 Id가 있을 경우
			this.submitId = json.get("submitId").getAsInt(); // 해당 값으로 초기화
		if(!json.get("stuId").isJsonNull()) // 학번이 있을 경우
			this.studentId = json.get("stuId").getAsString(); // 해당 값으로 초기화
		if(!json.get("score").isJsonNull()) // 점수가 있을 경우 
			this.score = json.get("score").getAsDouble(); // 해당 값으로 초기화
	}
	
	//private형 변수 gradeId를 반환하는 함수
	public int getGradeId(){
		return gradeId;
	}
	
	//private형 변수 gradeId를 지정해주는 함수
	public void setGradeId(int gradeId){
		this.gradeId = gradeId;
	}
	
	//private형 변수  lectureId를 반환하는 함수
	public String getLectureId(){
		return lectureId;
	}
	
	//private형 변수 lectureID를  지정해주는 함수
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	//private형 변수 submitId를 반환하는 함수
	public int getSubmitId(){
		return submitId;
	}
	
	//private형 변수 submitId를 지정해주는 함수
	public void setSubmitId(int submitId){
		this.submitId = submitId;
	}
	
	//private형 변수 studentId를 반환하는 함수
	public String getStudentId(){
		return studentId;
	}
		
	//private형 변수 studentId를 지정해주는 함수
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	
	//private형 변수 score를 반환하는 함수
	public double getScore(){
		return score;
	}
	
	//private형 변수 score를 지정해주는 함수
	public void setScore(double score){
		this.score = score;
	}

	//사용자 설정 toString 함수
	public String toString() {
		return "{\n\t'gradeId' : " + gradeId + ",\n\t'lectureId' : " + lectureId + 
				"\n\t'submitId' : " + submitId + ",\n\t'student' : " + studentId +
				",\n\t'score' : " + score + "\n}";
	}
}
