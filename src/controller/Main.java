package controller;

import java.util.ArrayList;
import model.LectureOutline;
import model.Student;

public class Main {
	public static void main(String[] args) throws Exception {
		NetworkManager manager = new NetworkManager();
		DataManager dataManager = new DataManager();

		dataManager.openDB();
		
		//dataManager.insertLectureOutlineDB(manager.getLectureOutline());
		
		//dataManager.deleteLectureOutlineDB("ACG2008-01");
		
		//ArrayList<LectureOutline> lectures = manager.getLectureOutline();
		//LectureOutline lectureOutline = lectures.get(0);
		//String key = lectureOutline.getLectureId();
		//lectureOutline.professorName = "hello";
		//dataManager.updateLectureOutlineDB(lectureOutline, key);
		
		dataManager.selectLectureOutlineDB("ss");
		dataManager.closeDB();
		
		int version = manager.needsUpdateLectureOutline();
		if (version != -1) {
			dataManager.openDB();
			dataManager.insertLectureOutlineDB(manager.getLectureOutline(version));
			dataManager.closeDB();
		}

		Student stu;
		if ((stu = (Student) manager.login("2014112025", "gjsl")) != null) {
			System.out.println("id : " + stu.getId());
			System.out.println("name : " + stu.getName());
			System.out.println("major : " + stu.getMajor());
		}	
	}
}
