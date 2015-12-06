package controller;

public class Grade {
	private String gradeId;
	private String lectureId;
	private String submitId;
	private String studentId;
	private double score;
	
	public String getGradeId(){
		return gradeId;
	}
	
	public void setGradeId(String gradeId){
		this.gradeId = gradeId;
	}
	
	public String getLectureId(){
		return lectureId;
	}
	
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	public String getSubmitId(){
		return submitId;
	}
	
	public void setSubmitId(String submitId){
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
