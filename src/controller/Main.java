package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import model.User;
import model.RecommendedLecture;
import view.LoginPage;
import view.MainPage;

public class Main {
	public static String ID;
	public static String PW;
	public static ActionListener e;
	public static void main(String[] args) {
		LoginPage loginpage = new LoginPage();
		MainPage mainpage = new MainPage();
		
		NetworkManager manager = new NetworkManager(loginpage,mainpage);
		DataManager dataManager = new DataManager();

		Thread downloadThread = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int version = manager.needsUpdateLectureOutline();
				if (version != -1) {
					dataManager.openDB();
					dataManager.deleteLectureOutlineDB();
					dataManager.insertLectureOutlineDB(manager.getLectureOutline(version));
					dataManager.closeDB();
					System.out.println("complete download lecture outline!");
				}
			}
		});
		downloadThread.start();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
	          System.err.println("Look and feel not set.");
	    }
		
		e = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {//ActionListener�� implements�� �Լ�
				switch(event.getActionCommand())//�̺�Ʈ���� ������ ActionCommand�� ������
				{
					case "login"://������ ActionCommand
					ID = loginpage.IDField.getText();
					PW = loginpage.PWField.getText();
					loginpage.PWField.setText("");
					User stu = manager.login(ID, PW);
					if (stu != null) {
						JOptionPane.showMessageDialog(null, stu.name + "님 안녕하세요!");
						loginpage.setVisible(false);
						mainpage.setContext(stu, manager, dataManager);
						mainpage.setVisible(true);
					}
					else{
						JOptionPane.showMessageDialog(null, "입력하신 아이디와 비밀번호가 일치하지 않습니다");
					}
					break;
					default:
						JOptionPane.showMessageDialog(null, "구현 ㄴㄴ");
					break;
				}
				}
			};
		loginpage.setActionListener(e);
		loginpage.setVisible(true);
	}
}
