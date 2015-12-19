/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

public class RecommendedLecture {
	private String lectureId; //학수번호
	public String professorName; //교수명
	public String title; //강의명
	public String curriculum; //교과과정
	private int point; //학점

	// 학수 번호를 지정하는 함수
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	// 학수 번호를 반환하는 함수
	public String getLectureId(){
		return this.lectureId;
	}

	// 학점을 지정하는 함수
	public boolean setPoint(int point){
		this.point = point;
		return true;
	}
	
	// 학점을 반환하는 함수
	public int getPoint(){
		return this.point;
	}
	
	// 사용자 지정 toString 함수
	public String toString() {
		return "{\n\t'lectureId' : " + lectureId + ",\n\t'professorName' : " + professorName +
				",\n\t'title' : " + title + ",\n\t'curriculum' : " + curriculum + ",\n\t'point' : "
				+ point + "\n}";
	}
}
