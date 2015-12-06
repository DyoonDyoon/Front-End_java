/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

public class Question {
	private String questionId;
	private String lectureId;
	private String studentId;
	public String content;
	
	public String getQuestionId(){
		return questionId;
	}
	
	public void setQuestionId(String questionId){
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
