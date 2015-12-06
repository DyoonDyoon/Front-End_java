package model;

public class Notification {
	private String notificationId;
	private String lectureId;
	public String title;
	public String description;
	
	public boolean modify(String title, String description){
		try{
			this.title = title;
			this.description = description;
		}
		catch(Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	        return false;
		}
		return true;
	}
	
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
}

