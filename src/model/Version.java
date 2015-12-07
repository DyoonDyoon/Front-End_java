package model;

public class Version {
	private String lectureId;
	public int notiVersion;
	public int assignVersion;
	
	public String getLectureId(){
		return lectureId;
	}
	
	public void setLectureId(String lectureId) {
		this.lectureId = lectureId;
		
	}
	
	public Version(String lectureId, int notiVersion, int assignVersion){
		this.lectureId = lectureId;
		this.notiVersion = notiVersion;
		this.assignVersion = assignVersion;
	}	
}
