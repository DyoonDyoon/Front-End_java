/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

public class Submit {
	private String submitId;
	private String assignId;
	private String studentId;
	private String filePath;
	
	public String getSubmitId(){
		return submitId;
	}
	
	public void setSubmitId(String submitId){
		this.submitId = submitId;
	}
	
	public String getAssignId(){
		return assignId;
	}
	
	public void setAssignId(String assignId){
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
