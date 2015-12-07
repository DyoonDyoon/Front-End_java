/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Submit {
	private int submitId;
	private String lectureId;
	private int assignId;
	private String studentId;
	private String filePath;
	
	public Submit() {}
	public Submit(JsonObject json) {
		this.submitId = json.get("submitId").getAsInt();
		this.lectureId = json.get("lectureId").getAsString();
		this.assignId = json.get("assignId").getAsInt();
		this.studentId = json.get("studentId").getAsString();
		this.filePath = json.get("filePath").getAsString();
	}
	
	public int getSubmitId(){
		return submitId;
	}
	
	public void setSubmitId(int submitId){
		this.submitId = submitId;
	}
	
	public String getLectureId() {
		return lectureId;
	}
	public void setLectureId(String lectureId) {
		this.lectureId = lectureId;
	}
	
	public int getAssignId(){
		return assignId;
	}
	
	public void setAssignId(int assignId){
		this.assignId = assignId;
	}
	
	public String getStudentId(){
		return studentId;
	}
	
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	
	public String getFilePath(){
		return filePath;
	}
	
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	
	public String toString() {
		return "{\n\t'submitId' : " + submitId + ",\n\t'assignId' : " + assignId + 
				",\n\t'studentId' : " + studentId + ",\n\t'filePath' : " + filePath +"\n}";
	}
}
