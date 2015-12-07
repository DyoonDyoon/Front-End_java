/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Question {
	private int questionId;
	private String lectureId;
	private String studentId;
	public String content;
	
	public Question() {
		//default constructor
		this.questionId = 0;
		this.lectureId = null;
		this.studentId = null;
		this.content = null;
	}
	public Question(JsonObject json) {
		if (!json.get("questionId").isJsonNull())
			this.questionId = json.get("questionId").getAsInt();
		if (!json.get("lectureId").isJsonNull())
			this.lectureId = json.get("lectureId").getAsString();
		if (!json.get("stuId").isJsonNull())
			this.studentId = json.get("stuId").getAsString();
		if (!json.get("content").isJsonNull())
			this.content = json.get("content").getAsString();
	}
	
	public int getQuestionId(){
		return questionId;
	}
	
	public void setQuestionId(int questionId){
		this.questionId = questionId;
	}
	
	public String getLectureId(){
		return lectureId;
	}
	
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	public String getStudentId(){
		return studentId;
	}
	
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	
	public String toString() {
		return "{\n\t'questionId' : " + questionId + "\n\t'lectureId' : " + lectureId + ",\n\t'studentId' : " + studentId + ",\n\t'content' : " + content + "\n}";
	}
}
