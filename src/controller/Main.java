package controller;

import java.util.ArrayList;

import model.LectureOutline;
import model.Student;

public class Main {
	public static void main(String[] args) throws Exception {
		NetworkManager manager = new NetworkManager();
		ArrayList<LectureOutline> lectureOutlines = manager.getLectureOutline();
		for (LectureOutline lectureOutline : lectureOutlines) {
			System.out.println(lectureOutline);
		}
		Student stu;
		if ((stu = (Student) manager.login("2014112025", "gjsl")) != null) {
			System.out.println("id : " + stu.getId());
			System.out.println("name : " + stu.getName());
			System.out.println("major : " + stu.getMajor());
		}	
	}
}
