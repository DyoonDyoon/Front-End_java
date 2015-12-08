package view;

import javax.swing.JFrame;

public class ClassAssignment extends JFrame{
	public ClassAssignment(){
		setTitle("과제 확인"); // 객체의 제목 설정
		setSize(450,450); // 객체  Size 설정
		setLocation(820,20); // 창이 뜰 위치를 설정
		setLayout(null); // 객체의 Layout을 Absolute로 설정
		setResizable(false); // JFrame의 사이즈를 조정하지 못하도록함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X키를 누르면 프로그램이 종료하도록함
		setVisible(true);
	}
}
