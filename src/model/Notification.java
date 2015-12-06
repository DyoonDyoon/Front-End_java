package model;

public class Notification {
	private String notificationId;
	private String lectureId;
	public String title;
	public String description;
		
	public String getNotification(){
		return notificationId;
	}
	
	public void setNotificationId(String notificationId){
		this.notificationId = notificationId;
	}
	
	public void setLectureId(String lectureId){
		this.lectureId = lectureId;
	}
	
	public String getLectureId(){
		return this.lectureId;
	}
	
	public String toString() {
		return "{\n\t'lectureId' : " + lectureId + ",\n\t'title' : " + title + ",\n\t'description' : " + description +"\n}";
	}
}

