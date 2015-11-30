package model;

public class Professor extends User {
	private String major;

	public Professor(String id, String name, String major) {
		this.id = id;
		this.setName(name);
		this.setMajor(major);
	}
	
	public void setMajor(String major) {
		this.major = major;
	}
	public String getMajor() {
		return major;
	}
}
