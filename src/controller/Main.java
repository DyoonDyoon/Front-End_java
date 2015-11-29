package controller;

import model.Student;

public class Main {
	public static void main(String[] args) throws Exception {
		NetworkManager manager = new NetworkManager();
		Student stu;
		if ((stu = (Student) manager.login("2014112025", "gjsl")) != null) {
			System.out.println("id : " + stu.getId());
			System.out.println("name : " + stu.getName());
			System.out.println("major : " + stu.getMajor());
		}
	}
}
