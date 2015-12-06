/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

public class Assignment {
	private String assignId;
	private String lectureId;
	public String title;
	public String description;
	public String filePath;
	public String startDate;
	public String endDate;
	
	public String getAssignId(){
		return assignId;
	}
	
	public void setAssignId(String assignId){
		this.assignId = assignId;
	}
	
	public String getLectureId(){
		return lectureId;
	}
	
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	public String toString() {
		return "{\n\t'assignId' : " + assignId + ",\n\t'lectureId' : " + lectureId + 
				"\n\t'description' : " + description + ",\n\t'filePath' : " + filePath +
				",\n\t'startDate' : " + startDate + ",\n\t'endDate' : " + endDate + "\n}";
	}
	
}
