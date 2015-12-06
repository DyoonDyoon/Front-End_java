package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import model.LectureOutline;
import model.Student;
import view.LoginPage;
import view.MainPage;

public class Main {
	public static String ID;
	public static String PW;
	public static ActionListener e;
	public static void main(String[] args) throws IOException{
		NetworkManager manager = new NetworkManager();
		DataManager dataManager = new DataManager();
		
		//dataManager.insertLectureOutlineDB(manager.getLectureOutline());
		//dataManager.deleteLectureOutlineDB("ACG2008-01");
		
		//ArrayList<LectureOutline> lectures = manager.getLectureOutline();
		//LectureOutline lectureOutline = lectures.get(0);
		//String key = lectureOutline.getLectureId();
		//lectureOutline.professorName = "hello";
		//dataManager.updateLectureOutlineDB(lectureOutline, key);

		//dataManager.selectLectureOutlineDB("ss");

		/*
		int version = manager.needsUpdateLectureOutline();
		if (version != -1) {
			dataManager.openDB();
			dataManager.insertLectureOutlineDB(manager.getLectureOutline(version));
			dataManager.closeDB();
		}
		 */
//		
//		dataManager.openDB();
//		dataManager.deleteQuestionDB("1");
//		dataManager.closeDB();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
	          System.err.println("Look and feel not set.");
	    }
		
		LoginPage loginpage = new LoginPage();
		MainPage mainpage = new MainPage();
		
		e = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {//ActionListener�� implements�� �Լ�
				switch(event.getActionCommand())//�̺�Ʈ���� ������ ActionCommand�� ������
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
						System.out.println("major : " + stu.getMajor());
						JOptionPane.showMessageDialog(null, stu.getName() + "님 안녕하세요!");
						loginpage.setVisible(false);
						mainpage.setStudentObject(stu);
						mainpage.setActionListener(e);
						mainpage.setVisible(true);
					}
					else{
						JOptionPane.showMessageDialog(null, "입력하신 아이디와 비밀번호가 일치하지 않습니다");
					}
					break;
					case "CheckAssign":
						JOptionPane.showMessageDialog(null, "과제확인이요!");
						mainpage.showAssign();
						mainpage.setVisible(true);
						break;
					case "CheckNoti":
						JOptionPane.showMessageDialog(null, "공지확인이요!");
						mainpage.showNoti();
						mainpage.setVisible(true);
						break;
					case "CheckGrade":
						JOptionPane.showMessageDialog(null, "성적확인이요!");
						mainpage.showGrade();
						mainpage.setVisible(true);
						break;
					case "EnterSubject":
						JOptionPane.showMessageDialog(null, "강의실입장이요!");
						mainpage.setVisible(true);
						break;
					case "gotoMain":
						mainpage.gotoMain(e);
						mainpage.setVisible(true);
						break;
					default:
						JOptionPane.showMessageDialog(null, "구현 ㄴㄴ");
					break;
				}
				}
			};
		loginpage.setActionListener(e);	
	}
	
	
}
