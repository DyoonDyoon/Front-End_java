/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Submit {
	private int submitId; // 제출 번호
	private String lectureId; // 학수 번호
	private int assignId; // 과제 번호
	private String studentId; // 학번
	private String filePath; // 파일 경로
	
	public Submit() {
		//default constructor
		this.submitId = 0; // 제출 번호 초기화
		this.lectureId = null; // 학수 번호 초기화
		this.assignId = 0; // 과제 번호 초기화
		this.studentId = null; // 학번 초기화
		this.filePath = null; // 파일 경로 초기화
	}
	
	//json을 이용한 생성자
	public Submit(JsonObject json) {
		if (!json.get("submitId").isJsonNull()) // 제출 번호가 존재할 경우
			this.submitId = json.get("submitId").getAsInt(); // 해당 값으로 초기화
		if (!json.get("lectureId").isJsonNull()) // 학수 번호가 존재할 경우
			this.lectureId = json.get("lectureId").getAsString(); // 해당 값으로 초기화
		if (!json.get("assignId").isJsonNull()) // 과제 번호가 존재할 경우
			this.assignId = json.get("assignId").getAsInt(); // 해당 값으로 초기화
		if (!json.get("stuId").isJsonNull()) // 학번이 존재할 경우
			this.studentId = json.get("stuId").getAsString(); // 해당 값으로 초기화
		if (!json.get("filePath").isJsonNull()) // 파일 경로가 존재할 경우
			this.filePath = json.get("filePath").getAsString(); // 해당 값으로 초기화
 	}
	
	// 제출 번호를 반환하는 함수
	public int getSubmitId(){
		return submitId;
	}
	
	// 제출 번호를 지정하는 함수
	public void setSubmitId(int submitId){
		this.submitId = submitId;
	}
	
	// 학수 번호를 반환하는 함수
	public String getLectureId() {
		return lectureId;
	}
	
	// 학수 번호를 지정하는 함수
	public void setLectureId(String lectureId) {
		this.lectureId = lectureId;
	}
	
	// 과제 번호를 반환하는 함수
	public int getAssignId(){
		return assignId;
	}
	
	// 과제 번호를 지정하는 함수
	public void setAssignId(int assignId){
		this.assignId = assignId;
	}
	
	// 학번을 반환하는 함수
	public String getStudentId(){
		return studentId;
	}
	
	// 학번을 지정하는 함수
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	
	// 파일 경로를 반환하는 함수
	public String getFilePath(){
		return filePath;
	}
	
	// 파일 경로를 지정하는 함수
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	
	public String toString() {
		return "{\n\t'submitId' : " + submitId + ",\n\t'assignId' : " + assignId + 
				",\n\t'studentId' : " + studentId + ",\n\t'filePath' : " + filePath +"\n}";
	}
}
