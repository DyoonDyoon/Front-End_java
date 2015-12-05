package model;

public class LectureOutLine{
	private int lecture_id; //학수번호
	public String professor_name; //교수명
	public String title; //강의명
	public String curriculum; //교과과정
	private int point; //학점

	public int getLecture_id(){
		return this.lecture_id;
	}

	public boolean setPoint(int point){
		this.point = point;
		return true;
	}
	
	public int getPoint(){
		return this.point;
	}
}