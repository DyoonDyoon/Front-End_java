package model;

public class Professor extends User {
	private String major;

	public void setMajor(String major) {
		this.major = major;
	}
	public String getMajor() {
		return major;
	}
}
