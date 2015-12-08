package model;

import com.google.gson.JsonObject;

public class LectureOutline{
	private String lectureId; //학수번호
	public String professorName; //교수명
	public String title; //강의명
	public String curriculum; //교과과정
	private int point; //학점

	public LectureOutline() {
		// default constructor
		this.lectureId = null;
		this.professorName = null;
		this.title = null;
		this.curriculum = null;
		this.point = 0;
	}
	
	public LectureOutline(JsonObject json) {
		this();
		
		if (!json.get("lecture_id").isJsonNull())
			this.lectureId = json.get("lecture_id").getAsString();
		if (!json.get("professor_name").isJsonNull())
			this.professorName = json.get("professor_name").getAsString();
		if (!json.get("title").isJsonNull())
			this.title = json.get("title").getAsString();
		if (!json.get("curriculum").isJsonNull())
			this.curriculum = json.get("curriculum").getAsString();
		String pointStr = "0"; 
		if (!json.get("point").isJsonNull())
			pointStr = json.get("point").getAsString();
		this.point = Integer.parseInt(pointStr);
	}
	
	public LectureOutline(String lectureId, String professorName, String title, String curriculum, int point) {
		this.lectureId = lectureId;
		this.professorName = professorName;
		this.title = title;
		this.curriculum = curriculum;
		this.point = point;
	}
	
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	public String getLectureId(){
		return this.lectureId;
	}

	public boolean setPoint(int point){
		this.point = point;
		return true;
	}
	
	public int getPoint(){
		return this.point;
	}
	
	public String toString() {
		return "{\n\t'lectureId' : " + lectureId + ",\n\t'professorName' : " + professorName +
				",\n\t'title' : " + title + ",\n\t'curriculum' : " + curriculum + ",\n\t'point' : "
				+ point + "\n}";
	}
}