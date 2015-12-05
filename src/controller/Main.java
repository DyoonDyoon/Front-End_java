package controller;

import model.Student;

public class Main {
	public static void main(String[] args) throws Exception {
		NetworkManager manager = new NetworkManager();
		DataManager dataManager = new DataManager();
		dataManager.openDB();
		dataManager.insertLectureOutlineDB(manager.getLectureOutline());
		dataManager.closeDB();
		
		
		
		Student stu;
		if ((stu = (Student) manager.login("2014112025", "gjsl")) != null) {
			System.out.println("id : " + stu.getId());
			System.out.println("name : " + stu.getName());
			System.out.println("major : " + stu.getMajor());
		}	
	}
}
