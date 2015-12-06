package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.LectureOutline;
import model.Student;
import view.LoginPage;

public class Main {
	public static String ID;
	public static String PW;
	public static ActionListener e;
	public static void main(String[] args){
		NetworkManager manager = new NetworkManager();
		DataManager dataManager = new DataManager();

		//dataManager.openDB();
		
		//dataManager.insertLectureOutlineDB(manager.getLectureOutline());
		
		//dataManager.deleteLectureOutlineDB("ACG2008-01");
		
		//ArrayList<LectureOutline> lectures = manager.getLectureOutline();
		//LectureOutline lectureOutline = lectures.get(0);
		//String key = lectureOutline.getLectureId();
		//lectureOutline.professorName = "hello";
		//dataManager.updateLectureOutlineDB(lectureOutline, key);

		//dataManager.selectLectureOutlineDB("ss");
		dataManager.closeDB();
		
		int version = manager.needsUpdateLectureOutline();
		if (version != -1) {
			dataManager.openDB();
			dataManager.insertLectureOutlineDB(manager.getLectureOutline(version));
			dataManager.closeDB();
		}

//		
//		dataManager.selectLectureOutlineDB("ACG4003-02");
//		dataManager.closeDB();
//		
//		int version = manager.needsUpdateLectureOutline();
//		if (version != -1) {
//			dataManager.openDB();
//			dataManager.insertLectureOutlineDB(manager.getLectureOutline(version));
//			dataManager.closeDB();
//		}

		LoginPage loginpage = new LoginPage();
		
		e = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {//ActionListener�� implements�� �Լ�
				switch(e.getActionCommand())//�̺�Ʈ���� ������ ActionCommand�� ������
				{
					case "login"://������ ActionCommand
					ID = loginpage.IDField.getText();
					PW = loginpage.PWField.getText();
					Student stu = null;
					try {
						stu = (Student) manager.login(ID, PW);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						if (stu != null) {
							System.out.println("id : " + stu.getId());
							System.out.println("name : " + stu.getName());
							//System.out.println("major : " + stu.getMajor());
						}
					break;
				}
				}
			};
		loginpage.setActionListener(e);	
	}
	
	
}
