/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Grade {
	private int gradeId;
	private String lectureId;
	private int submitId;
	private String studentId;
	private double score;
	
	public Grade() {
		this.gradeId = 0;
		this.lectureId = null;
		this.submitId = 0;
		this.studentId = null;
		this.score = 0;
	}
	
	public Grade(JsonObject json) {
		this();
		if(!json.get("gradeId").isJsonNull())
			this.gradeId = json.get("gradeId").getAsInt();
		if(!json.get("lectureId").isJsonNull())
			this.lectureId = json.get("lectureId").getAsString();
		if(!json.get("submitId").isJsonNull())
			this.submitId = json.get("submitId").getAsInt();
		if(!json.get("stuId").isJsonNull())
			this.studentId = json.get("stuId").getAsString();
		if(!json.get("score").isJsonNull())
			this.score = json.get("score").getAsDouble();
	}
	
	public int getGradeId(){
		return gradeId;
	}
	
	public void setGradeId(int gradeId){
		this.gradeId = gradeId;
	}
	
	public String getLectureId(){
		return lectureId;
	}
	
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	public int getSubmitId(){
		return submitId;
	}
	
	public void setSubmitId(int submitId){
		this.submitId = submitId;
	}
	
	public String getStudentId(){
		return studentId;
	}
		
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	public double getScore(){
		return score;
	}
	
	public void setScore(double score){
		this.score = score;
	}

	public String toString() {
		return "{\n\t'gradeId' : " + gradeId + ",\n\t'lectureId' : " + lectureId + 
				"\n\t'submitId' : " + submitId + ",\n\t'student' : " + studentId +
				",\n\t'score' : " + score + "\n}";
	}
}
