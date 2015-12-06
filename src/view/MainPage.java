package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import model.Student;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainPage extends JFrame{
	public JPanel contentPane;
	
	Student stu = null;
	
	JPanel inform;
	JLabel StId;
	JLabel StName;
	JLabel StMajor;

	JPanel MenuAll;
	public JButton CheckAssign;
	public JButton CheckNoti;
	public JButton CheckGrade;
	
	JPanel SubjectPanel;
	JList SubjectList;
	JScrollPane scrollPane;
	public JButton EnterSubject;
	Vector<String> subject;
	
	
	public MainPage(){
		setTitle("Mini E-class");
		setSize(800,450);
		setLocation(20,20);
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	}
	
	public void setStudentObject(Student stu){
		this.stu = stu;
		setSubjectList();
		setInform();
		setMenu();
	}

	public void setInform(){
		inform = new JPanel();
		inform.setLocation(12,10);
		inform.setSize(114, 100);
		inform.setBorder(BorderFactory.createLineBorder(Color.black));
		
		StId = new JLabel(stu.getId());
		StId.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		StId.setBounds(12, 35, 34, 15);
		StId.setVisible(true);
		inform.add(StId);
		
		StName = new JLabel(stu.getName());
		StName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		StName.setVisible(true);
		StName.setBounds(12, 10, 57, 15);
		inform.add(StName);
		
		StMajor = new JLabel(stu.getMajor());
		StMajor.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		StMajor.setVisible(true);
		StMajor.setBounds(12, 60, 57, 15);
		inform.add(StMajor);
		
		inform.setVisible(true);
		inform.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "개인 정보", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(inform);
	}
	
	public void setMenu(){
		MenuAll = new JPanel();
		MenuAll.setBounds(12, 170, 114, 209);
		MenuAll.setLayout(null);
		
		CheckAssign = new JButton("과제 확인");
		CheckAssign.setBounds(0, 76, 114, 57);
		CheckAssign.setActionCommand("CheckAssign");
		MenuAll.add(CheckAssign);
		
		CheckNoti = new JButton("공지 확인");
		CheckNoti.setBounds(0, 0, 114, 57);
		CheckNoti.setActionCommand("CheckNoti");
		MenuAll.add(CheckNoti);
		
		CheckGrade = new JButton("성적 확인");
		CheckGrade.setBounds(0, 152, 114, 57);
		CheckGrade.setActionCommand("CheckGrade");
		MenuAll.add(CheckGrade);
		
		contentPane.add(MenuAll);
	}
	
	public void setSubjectList(){
		SubjectPanel = new JPanel();
		SubjectPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "수강 과목", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		SubjectPanel.setBounds(191, 10, 188, 309);
		SubjectPanel.setLayout(null);
		
		//★★★★★★★★★★★★for문을 통해 강의 제목을 가져오도록하는 구문 필요
		subject = new Vector<String>();
		for(int i=0; i<50;i++){
			subject.add("왜안되냐 씨발");
		}
		
		SubjectList = new JList<String>(subject);
		SubjectList.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		SubjectList.setAutoscrolls(true);
		SubjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		SubjectList.setBackground(SystemColor.control);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 21, 164, 235);
		scrollPane.setViewportView(SubjectList);
		SubjectPanel.add(scrollPane);
		
		EnterSubject = new JButton("강의실 입장");
		EnterSubject.setBounds(22, 266, 143, 33);
		EnterSubject.setActionCommand("EnterSubject");
		SubjectPanel.add(EnterSubject);
		
		contentPane.add(SubjectPanel);
	}
	
	public void setActionListener(ActionListener e){
		CheckAssign.addActionListener(e);
		CheckNoti.addActionListener(e);
		CheckGrade.addActionListener(e);
		EnterSubject.addActionListener(e);
	}
}
