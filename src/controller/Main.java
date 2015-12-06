package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Lecture;
import model.LectureOutline;
import model.Notification;
import model.Student;
import view.LoginPage;

public class Main {
	public static String ID;
	public static String PW;
	public static ActionListener e;
	public static void main(String[] args) throws IOException{
		NetworkManager manager = new NetworkManager();
		DataManager dataManager = new DataManager();
		
		//ArrayList<LectureOutline> lectures = manager.getLectureOutline();
		//LectureOutline lectureOutline = lectures.get(0);
		//String key = lectureOutline.getLectureId();
		//lectureOutline.professorName = "hello";
		//dataManager.updateLectureOutlineDB(lectureOutline, key);

		/*
		int version = manager.needsUpdateLectureOutline();
		if (version != -1) {
			dataManager.openDB();
			dataManager.insertLectureOutlineDB(manager.getLectureOutline(version));
			dataManager.closeDB();
		}
		 */
	
		/*select할때 사용
		ArrayList<Grade> lec = new ArrayList<Grade>();
		dataManager.openDB();
		lec = dataManager.selectGradeDB("1");
		for(int num=0; num<lec.size(); ++num){
			System.out.println(lec.get(num));
		}
		dataManager.closeDB();
		*/
		
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
