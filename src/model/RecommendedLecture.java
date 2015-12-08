package model;

public class RecommendedLecture {
	private String lectureId; //학수번호
	public String professorName; //교수명
	public String title; //강의명
	public String curriculum; //교과과정
	private int point; //학점

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
