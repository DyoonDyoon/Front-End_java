package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import model.Student;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainPage extends JFrame{
	//메인 화면의 내용을 저장하는 JPanel 선언
	public JPanel contentPane;
	
	Student stu = null; // 학생 정보를 저장하는 Student 객체 선언
	
	//메인 패널의 좌우 정보를 저장하는 JPanel 선언
	JPanel LeftPanel;
	JPanel RightPanel;
	
	//왼쪽 패널에 학생 정보를 저장하는 부분 구현을 위한 JPanel과 JLabel 선언
	JPanel inform; // 정보를 저장하기 위한 JPanel
	JLabel StId;   // 학번 저장할 JLabel
	JLabel StName; // 학생 이름을 저장할 JLabel
	JLabel StMajor; // 학생 학과를 저장할 JLabel

	//왼쪽 패널의 메뉴 부분을 구현하기 위한 JPanel과 JButton 
	JPanel MenuAll; // 버튼을 저장할 JPanel
	public JButton gotoMain; // 메인으로 돌아가는 버튼
	public JButton CheckAssign; // 과제 확인을 위한 버튼
	public JButton CheckNoti; // 공지사항 확인을 위한 버튼
	public JButton CheckGrade; // 성적 확인을 위한 버튼
	
	//오른쪽 패널에 존재하는 수강중인 강의와 강의실 입장을 구현하기 위한 JPanel과 List, Button
	JPanel SubjectPanel; // 강의실을 나타내는 JPanel
	JList SubjectList; // 수강중인 강의를 저장하는 JList
	JScrollPane scrollPane; // JList는  자체적으로 Scrollbar가 존재하지 않으므로 구현을 위해 필요함
	public JButton EnterSubject; // 강의실 입장을 위한 JButton
	Vector<String> subject; // 수강중인 강의를 저장하도록하는 String형 Vector 배열
	
	//오른쪽 패널에 존재하는 추천 강의 구현을 위한 JTable, JPanel
	JPanel RecSubjectPanel; // 추천 강의 구현을 위한 JPanel
	JScrollPane RecscrollPane; // 위와 동일
	JTable RecSubjectTable; // 추천 강의를 저장하는 JTable
	
	public MainPage(){ // 생성자 구현
		setTitle("Mini E-class"); // 객체의 제목 설정
		setSize(800,450); // 객체  Size 설정
		setLocation(20,20); // 창이 뜰 위치를 설정
		setLayout(null); // 객체의 Layout을 Absolute로 설정
		setResizable(false); // JFrame의 사이즈를 조정하지 못하도록함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X키를 누르면 프로그램이 종료하도록함
		
		//메인 화면의 정보를 저장하는 부분을 구현
		contentPane = new JPanel(); // 생성
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // 가장자리 구현
		setContentPane(contentPane); // 메인 Panel을 설정
		contentPane.setLayout(null); // 위의 레이아웃을 Absolute로 설정
		
		//왼쪽 부분을 구현하도록하는 Panel 구현
		LeftPanel = new JPanel(); // 생성
		LeftPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2)); // 가장자리 구현
		LeftPanel.setBounds(0, 0, 143, 420); // 크기와 위치를 지정
		LeftPanel.setLayout(null); // 레이아웃을  Absolute로 설정
		
		//오른쪽 부분을 구현하도록하는 Panel 구현
		RightPanel = new JPanel(); // 생성
		RightPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2)); // 가장자리 궇ㄴ
		RightPanel.setBounds(142, 0, 650, 420); // 크기와 위치를 지정
		RightPanel.setLayout(null); // 레이아웃을 Absolute로 설정
	}
	
	// Main에서 로그인을 할 경우 Student 객체를 저장하고 화면을 구현하도록함
	public void setStudentObject(Student stu){
		this.stu = stu; // Student객체를 방아와 설정
		setSubjectList(); // 수강중인 강의 목록 구현
		setInform(); // 학생 정보 표시창을 구현
		setMenu(); // 메뉴 표시창을 구현
		setRecommendList(); // 추천 강의 목록 구현
		contentPane.add(LeftPanel); // 왼쪽 패널을 Main Panel에 추가
		contentPane.add(RightPanel); // ㅇ오른쪽 패널을 Main Panel에 추가
	}
	
	// 학생 정보를 통해 이름,학번,학과를 출력하는 Panel을 생성하는 함수
	public void setInform(){
		// 학생 정보를 저장하는 Panel 구현
		inform = new JPanel(); // 생성
		inform.setLocation(12,47); // 위치 설정
		inform.setSize(114, 100); // 사이즈 설정
		inform.setBorder(BorderFactory.createLineBorder(Color.black)); // 가장자리를 검은색 실선으로 설정
		
		//학생 학번을 저장하는 JLabel 구현
		StId = new JLabel(stu.getId()); // 생성
		StId.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 크기를 설정
		StId.setBounds(12, 35, 34, 15); // 위치와 사이즈를 설정
		StId.setVisible(true); // 보이도록 함
		inform.add(StId); // 학생 정보를 저장하는 Panel에 더함
		
		//학생 이름을 저장하는 JLabel 구현
		StName = new JLabel(stu.getName()); // 생성
		StName.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 크기를 설정
		StName.setVisible(true); //보이도록 함
		StName.setBounds(12, 10, 57, 15); // 위치와 사이즈를 설정
		inform.add(StName); // 학생정보를 저장하는 Panel에 더함
		
		//학과를 저장하는 JLabel 구현
		StMajor = new JLabel(stu.getMajor()); // 생성
		StMajor.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 크기를 설정
		StMajor.setVisible(true); // 보이도록 함
		StMajor.setBounds(12, 60, 57, 15); // 위치와 사이즈를 설정
		inform.add(StMajor); // 학생 정보를 저장하는 Panel에 더함
		
		inform.setVisible(true); // 구현한 Panel을 보이도록 함
		// Title을 가지는 가장자리로 구현하도록 함 
		inform.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "개인 정보", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		LeftPanel.add(inform); // 왼쪽 패널에 구현한 Panel을 더함
	}
	
	//메뉴를 구현하도록하는 함수
	public void setMenu(){
		//Main화면으로 돌아가도록하는 버튼 구현
		gotoMain = new JButton("메인 화면으로"); // 이름 설정 후 생성
		gotoMain.setBounds(10, 10, 114, 30); // 위치와 크기 설정 
		gotoMain.setFont(new Font("맑은 고딕", Font.PLAIN, 14)); // 폰트와 크기 설정
		gotoMain.setActionCommand("gotoMain"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		LeftPanel.add(gotoMain); // 왼쪽 패널에 구현한 버튼을 저장
		
		//Menu 버튼을 저장하기 위한 Panel 구현
		MenuAll = new JPanel(); // 생성
		MenuAll.setBounds(12, 172, 114, 209); // 사이즈와 위치 설정
		MenuAll.setLayout(null); // 레이아웃을 Absolute로 설정 
		
		//과제 확인을 위한 Button 구현
		CheckAssign = new JButton("과제 확인"); // 이름 설정 후 생성
		CheckAssign.setBounds(0, 76, 114, 57); // 위치와 사이즈 설정
		CheckAssign.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		CheckAssign.setActionCommand("CheckAssign"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		MenuAll.add(CheckAssign); // MenuAll 패널에 더함
		
		//공지 확인을 위한 Button 구현
		CheckNoti = new JButton("공지 확인"); // 이름 설정 후 생성
		CheckNoti.setBounds(0, 0, 114, 57); // 위치와 사이즈 설정
		CheckNoti.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		CheckNoti.setActionCommand("CheckNoti"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		MenuAll.add(CheckNoti); // MenuAll 패널에 더함
		
		//성적 확인을 위한 Button 구현
		CheckGrade = new JButton("성적 확인"); // 이름 성정 후 생성
		CheckGrade.setBounds(0, 152, 114, 57); // 위치와 사이즈 설정 
		CheckGrade.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		CheckGrade.setActionCommand("CheckGrade"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		MenuAll.add(CheckGrade); // MenuAll 패널에 더함
		
		LeftPanel.add(MenuAll); // MenuAll패널을 왼쪽패널에 더함
	}
	
	// 수강중인 강의 목록을 띄우기 위한 패널 구현 함수
	public void setSubjectList()
	{	//강의 목록 저장 패널 구현
		SubjectPanel = new JPanel(); // 생성
		// 가장자리 타이틀을 가지고 있는 선을 생성
		SubjectPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "수강 과목", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		SubjectPanel.setBounds(50, 20, 188, 309); // 위치와 사이즈 설정
		SubjectPanel.setLayout(null); // 레이아웃을 Absolute로 설정
		
		//★★★★★★★★★★★★for문을 통해 강의 제목을 가져오도록하는 구문 필요
		subject = new Vector<String>();
		for(int i=0; i<50;i++){
			subject.add("듣는 강의");
		}
		
		//강의 목록을 가지고있는 Vector 배열을 통해 강의 목록 생성
		SubjectList = new JList<String>(subject); // 생성
		SubjectList.setFont(new Font("맑은 고딕", Font.PLAIN, 13)); // 폰트와 폰트 크기 설정
		SubjectList.setAutoscrolls(true); // 스크롤바를 생성하도록 함
		SubjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러개를 선택하지 못하도록함
		SubjectList.setBackground(SystemColor.control); // 색상 지정
		
		scrollPane = new JScrollPane(); // List에 스크롤바를 생성하도록함
		scrollPane.setBounds(12, 21, 164, 235); // 위치와 사이즈 설정
		scrollPane.setViewportView(SubjectList); // List 삽입
		SubjectPanel.add(scrollPane); // SubjectPanel에 더함
		
		//강의실 입장을 위한 버튼 구현
		EnterSubject = new JButton("강의실 입장"); // 이름 설정 후 생성
		EnterSubject.setBounds(22, 266, 143, 33); // 위치와 사이즈 설정
		EnterSubject.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기를 지정
		EnterSubject.setActionCommand("EnterSubject"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		SubjectPanel.add(EnterSubject); // SubjectPanel에 더함
		
		RightPanel.add(SubjectPanel); // SubjectPanel을 오른쪽 패널에 더함
	}
	
	//추천 강의 목록을 띄우기 위한 함수
	public void setRecommendList(){
		//추천 강의 목록을 구현하기 위한 Panel 설정
		RecSubjectPanel = new JPanel(); // 생성
		// 가장자리에 타이틀을 가지고 있는 선을 추가하기 위함
		RecSubjectPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "추천 강의", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		RecSubjectPanel.setBounds(350, 20, 250, 265); // 위치와 사이즈 설정
		RecSubjectPanel.setLayout(null); // 레이아웃을 Absolute로 설정
		
		String columnNames[] = {"학점","강의 제목"}; // Column을 설명하기 위함
		//강의 목록 저장
		Object rowData[][] = { {3,"주니어 디자인 프로젝트"},
				{3,"시스템 소프트웨어 프로그래밍"},
				{3,"프로그래밍 언어와 실습"},
				{3,"객체 지향 언어와 실습"},
				{3,"미적분학 및 연습"},
				{3,"기술 창조와 특허"},
				{1,"가나다"},
				{1,"가나다"},
				{1,"가나다"},
				{1,"가나다"},
		};
		//Default 테이블을 설정함
		DefaultTableModel defaulttablemodel = new DefaultTableModel(rowData,columnNames);
		
		RecSubjectTable = new JTable(defaulttablemodel); // Default 테이블을 통해 JTable 생성
		RecSubjectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		RecSubjectTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		RecSubjectTable.setBackground(SystemColor.control); // 생상 설정
		RecSubjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		//JTable의 Column의 크기를 지정하도록 함
		TableColumnModel columnmodel = RecSubjectTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(50); // 학점 칸은 50픽셀로 설정
		columnmodel.getColumn(1).setPreferredWidth(170); // 강의 제목 칸은 170픽셀로 설정
		
		RecscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		RecscrollPane.setBounds(12, 21, 230, 235); // 위치와 크기 설정
		RecscrollPane.setViewportView(RecSubjectTable);	//JTable 지정
		RecscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		RecscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		RecSubjectPanel.add(RecscrollPane); // RecSubjectPanel에 더함
		
		RightPanel.add(RecSubjectPanel); // RightPanel에 더함
	}
	
	//과제 확인 창을 구현하는 함수
	public void showAssign(){
		RightPanel.removeAll();
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	//공지 확인 창을 구현하는 함수
	public void showNoti(){
		RightPanel.removeAll();
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	//성적 확인 창을 구현하는 함수
	public void showGrade(){
		RightPanel.removeAll();
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	//메인화면으로 돌아가도록하는 함수
	public void gotoMain(ActionListener e){
		RightPanel.removeAll(); // 오른쪽 패널을 모두 삭제
		setSubjectList(); // 수강중인 강의 목록을 띄움
		setRecommendList(); // 추천 강의 목록을 띄움
		EnterSubject.addActionListener(e); // ActionListener을 설정
		contentPane.revalidate(); 
		contentPane.repaint(); //다시 Refresh해줌
	}
	
	//ActionListener을 설정해주는 함수 
	public void setActionListener(ActionListener e){
		//모든 버튼에 ActionListener을 추가함
		CheckAssign.addActionListener(e);
		CheckNoti.addActionListener(e);
		CheckGrade.addActionListener(e);
		EnterSubject.addActionListener(e);
		gotoMain.addActionListener(e);
	}
}
