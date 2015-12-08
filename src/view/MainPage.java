package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import controller.DataManager;
import controller.NetworkManager;
import model.Lecture;
import model.LectureOutline;
import model.User;

public class MainPage extends JFrame{
	
	ActionListener actionlistener;
	MouseAdapter mouseadapter_jlist;
	MouseAdapter mouseadapter_jtable;
	//메인 화면의 내용을 저장하는 JPanel 선언
	public JPanel contentPane;
	
	User stu = null; // 학생 정보를 저장하는 Student 객체 선언
	DataManager dataManager = null;
	NetworkManager networkManager = null;
	
	ArrayList<Lecture> lectures = new ArrayList<Lecture>();
	
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
	JButton gotoMain; // 메인으로 돌아가는 버튼
	JButton CheckAssign; // 과제 확인을 위한 버튼
	JButton CheckNoti; // 공지사항 확인을 위한 버튼
	JButton CheckGrade; // 성적 확인을 위한 버튼
	
	//오른쪽 패널에 존재하는 수강중인 강의와 강의실 입장을 구현하기 위한 JPanel과 List, Button
	JPanel SubjectPanel; // 강의실을 나타내는 JPanel
	JList SubjectList; // 수강중인 강의를 저장하는 JList
	JScrollPane scrollPane; // JList는  자체적으로 Scrollbar가 존재하지 않으므로 구현을 위해 필요함
	JButton EnterSubject; // 강의실 입장을 위한 JButton
	Vector<String> subject; // 수강중인 강의를 저장하도록하는 String형 Vector 배열
	
	//오른쪽 패널에 존재하는 추천 강의 구현을 위한 JTable, JPanel
	JPanel RecSubjectPanel; // 추천 강의 구현을 위한 JPanel
	JScrollPane RecscrollPane; // 위와 동일
	JTable RecSubjectTable; // 추천 강의를 저장하는 JTable
	
	JPanel AssignmentPanel;
	JTable AssignmentTable;
	JScrollPane AssignmentscrollPane;
	JTextArea AssignmentTitle;
	JTextArea AssignmentContent;
	
	JPanel NotificationPanel;
	JTable NotificationTable;
	JScrollPane NotificationscrollPane;
	JTextArea NotificationTitle;
	JTextArea NotificationContent;
	
	JPanel GradePanel;
	JTable GradeTable;
	
	JPanel ClassPanel;
	
	JScrollPane GradescrollPane;
	
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
		
		actionlistener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {//ActionListener�� implements�� �Լ�
				switch(event.getActionCommand())//�̺�Ʈ���� ������ ActionCommand�� ������
				{
					case "CheckAssign":
						JOptionPane.showMessageDialog(null, "과제확인이요!");
						showAssign();
						setVisible(true);
						break;
					case "CheckNoti":
						JOptionPane.showMessageDialog(null, "공지확인이요!");
						showNoti();
						setVisible(true);
						break;
					case "CheckGrade":
						JOptionPane.showMessageDialog(null, "성적확인이요!");
						showGrade();
						setVisible(true);
						break;
					case "EnterSubject":
						JOptionPane.showMessageDialog(null, "강의실입장이요!");
						gotoClass();
						setVisible(true);
						break;
					case "gotoMain":
						gotoMain();
						setVisible(true);
						break;
					default:
						JOptionPane.showMessageDialog(null, "구현 ㄴㄴ");
					break;
				}
				}
			};
			mouseadapter_jlist = new MouseAdapter(){
				public void mouseClicked(MouseEvent evt) {
			        JList list = (JList)evt.getSource();
			        if (evt.getClickCount() == 2) {
			            // Double-click detected
			            int index = list.locationToIndex(evt.getPoint());
						JOptionPane.showMessageDialog(null, index+"번 줄입니다");
			        } else if (evt.getClickCount() == 3) {
			            // Triple-click detected
			            int index = list.locationToIndex(evt.getPoint());
						JOptionPane.showMessageDialog(null, index+"번 줄입니다");
			        }
			    }
			};
			mouseadapter_jtable = new MouseAdapter(){
				public void mouseClicked(MouseEvent evt) {
			        JTable table = (JTable)evt.getSource();
			        if (evt.getClickCount() == 2) {
			            // Double-click detected
			            int index = table.getSelectedRow();
						JOptionPane.showMessageDialog(null, index+"번 줄입니다");
			        } else if (evt.getClickCount() == 3) {
			            // Triple-click detected
			            int index = table.getSelectedRow();
						JOptionPane.showMessageDialog(null, index+"번 줄입니다");
			        }
			    }
			};
	}
	
	// Main에서 로그인을 할 경우 Student 객체를 저장하고 화면을 구현하도록함
	public void setContext(User stu, NetworkManager networkManager, DataManager dataManager){
		this.networkManager = networkManager;
		this.dataManager = dataManager;
		this.stu = stu; // Student객체를 방아와 설정
		setSubjectList(); // 수강중인 강의 목록 구현
		setInform(); // 학생 정보 표시창을 구현
		setMenu(); // 메뉴 표시창을 구현
		setRecommendList(); // 추천 강의 목록 구현
		contentPane.add(LeftPanel); // 왼쪽 패널을 Main Panel에 추가
		contentPane.add(RightPanel); // ㅇ오른쪽 패널을 Main Panel에 추가
		contentPane.revalidate();
		contentPane.repaint();
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
		StName = new JLabel(stu.name); // 생성
		StName.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 크기를 설정
		StName.setVisible(true); //보이도록 함
		StName.setBounds(12, 10, 57, 15); // 위치와 사이즈를 설정
		inform.add(StName); // 학생정보를 저장하는 Panel에 더함
		
		//학과를 저장하는 JLabel 구현
		StMajor = new JLabel(stu.major); // 생성
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
		gotoMain = new JButton("메인 화면"); // 이름 설정 후 생성
		gotoMain.setBounds(10, 10, 114, 30); // 위치와 크기 설정 
		gotoMain.setFont(new Font("맑은 고딕", Font.PLAIN, 14)); // 폰트와 크기 설정
		gotoMain.addActionListener(actionlistener);
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
		CheckAssign.addActionListener(actionlistener);
		CheckAssign.setActionCommand("CheckAssign"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		MenuAll.add(CheckAssign); // MenuAll 패널에 더함
		
		//공지 확인을 위한 Button 구현
		CheckNoti = new JButton("공지 확인"); // 이름 설정 후 생성
		CheckNoti.setBounds(0, 0, 114, 57); // 위치와 사이즈 설정
		CheckNoti.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		CheckNoti.addActionListener(actionlistener);
		CheckNoti.setActionCommand("CheckNoti"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		MenuAll.add(CheckNoti); // MenuAll 패널에 더함
		
		//성적 확인을 위한 Button 구현
		CheckGrade = new JButton("성적 확인"); // 이름 성정 후 생성
		CheckGrade.setBounds(0, 152, 114, 57); // 위치와 사이즈 설정 
		CheckGrade.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		CheckGrade.addActionListener(actionlistener);
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
		
		subject = new Vector<String>();
		if (networkManager.syncLectures(stu.getId())) {
			dataManager.openDB();
			lectures = dataManager.selectLectureDB(stu.getId());
			for (int i = 0; i < lectures.size(); i++) {
				Lecture lecture = lectures.get(i);
				LectureOutline lectureOutline = dataManager.selectLectureOutlineDB(lecture.getLectureId());
				if (lectureOutline != null)
					subject.add(lectureOutline.title);
			}
			dataManager.closeDB();
		}
		
		//강의 목록을 가지고있는 Vector 배열을 통해 강의 목록 생성
		SubjectList = new JList<String>(subject); // 생성
		SubjectList.setFont(new Font("맑은 고딕", Font.PLAIN, 13)); // 폰트와 폰트 크기 설정
		SubjectList.setAutoscrolls(true); // 스크롤바를 생성하도록 함
		SubjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러개를 선택하지 못하도록함
		SubjectList.setBackground(SystemColor.control); // 색상 지정
		SubjectList.addMouseListener(mouseadapter_jlist);
		
		scrollPane = new JScrollPane(); // List에 스크롤바를 생성하도록함
		scrollPane.setBounds(12, 21, 164, 235); // 위치와 사이즈 설정
		scrollPane.setViewportView(SubjectList); // List 삽입
		SubjectPanel.add(scrollPane); // SubjectPanel에 더함
		
		//강의실 입장을 위한 버튼 구현
		EnterSubject = new JButton("강의실 입장"); // 이름 설정 후 생성
		EnterSubject.setBounds(22, 266, 143, 33); // 위치와 사이즈 설정
		EnterSubject.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기를 지정
		EnterSubject.addActionListener(actionlistener);
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
		DefaultTableModel defaulttablemodel = new DefaultTableModel(rowData,columnNames)
		{
		    @Override
		    public boolean isCellEditable(int row, int column){
		       //all cells false
		       return false;
		    }
		};
		
		RecSubjectTable = new JTable(defaulttablemodel); // Default 테이블을 통해 JTable 생성
		RecSubjectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		RecSubjectTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		RecSubjectTable.setBackground(SystemColor.control); // 생상 설정
		RecSubjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		RecSubjectTable.addMouseListener(mouseadapter_jtable);
		
		//JTable의 Column의 크기를 지정하도록 함
		TableColumnModel columnmodel = RecSubjectTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(50); // 학점 칸은 50픽셀로 설정
		columnmodel.getColumn(1).setPreferredWidth(170); // 강의 제목 칸은 170픽셀로 설정
		columnmodel.getColumn(0).setResizable(false);
		columnmodel.getColumn(1).setResizable(false);
		
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
		AssignmentPanel = new JPanel();
		AssignmentPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "과제 확인", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		AssignmentPanel.setBounds(10, 10, 630, 400); // 위치와 사이즈 설정
		AssignmentPanel.setLayout(null); // 레이아웃을 Absolute로 설정
	
		String columnNames[] = {"강의","제목"}; // Column을 설명하기 위함
		//강의 목록 저장
		Object rowData[][] = { {"시스템 소프트웨어 프로그래밍","씨발 힘들어요"},
				{"주니어 디자인 프로젝트","뒤질꺼같아요"},
				{"객체 지향 언어와 실습","다신 하기 싫어요"},
				{"프로그래밍 언어와 실습","살려주세요"},
				{"미적분학 및 연습","배고파요"},
				{"미적분학 및 연습","기술 창조와 특허"},
				{"미적분학 및 연습","가나다"},
				{"미적분학 및 연습","가나다"},
				{"미적분학 및 연습","가나다"},
				{"미적분학 및 연습","가나다"},
		};

		DefaultTableModel defaulttablemodel = new DefaultTableModel(rowData,columnNames)
			{
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		
		AssignmentTable = new JTable(defaulttablemodel);
		AssignmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		AssignmentTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		AssignmentTable.setBackground(SystemColor.control); // 생상 설정
		AssignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		AssignmentTable.addMouseListener(mouseadapter_jtable);
		
		TableColumnModel columnmodel = AssignmentTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(150); 
		columnmodel.getColumn(1).setPreferredWidth(440);
		columnmodel.getColumn(0).setResizable(false);
		columnmodel.getColumn(1).setResizable(false);
		
		AssignmentscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		AssignmentscrollPane.setBounds(10,20, 610, 180); // 위치와 크기 설정
		AssignmentscrollPane.setViewportView(AssignmentTable);	//JTable 지정
		AssignmentscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		AssignmentscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		AssignmentPanel.add(AssignmentscrollPane);

		AssignmentTitle = new JTextArea("123");
		AssignmentTitle.setBounds(10, 210, 400, 20);
		AssignmentTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		AssignmentTitle.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		AssignmentTitle.setEditable(false);
		AssignmentTitle.setBackground(SystemColor.control); // 생상 설정
		AssignmentPanel.add(AssignmentTitle);
		
		AssignmentContent = new JTextArea("123");
		AssignmentContent.setBounds(10, 230, 600, 150);
		AssignmentContent.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		AssignmentContent.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		AssignmentContent.setEditable(false);
		AssignmentContent.setBackground(SystemColor.control); // 생상 설정
		AssignmentPanel.add(AssignmentContent);
		
		RightPanel.removeAll();
		RightPanel.add(AssignmentPanel);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	//공지 확인 창을 구현하는 함수
	public void showNoti(){
		NotificationPanel = new JPanel();
		NotificationPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "공지 확인", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		NotificationPanel.setBounds(10, 10, 630, 400); // 위치와 사이즈 설정
		NotificationPanel.setLayout(null); // 레이아웃을 Absolute로 설정
	
		String columnNames[] = {"강의","제목"}; // Column을 설명하기 위함
		//강의 목록 저장
		Object rowData[][] = { {"시스템 소프트웨어 프로그래밍","과제 없습니다"},
				{"주니어 디자인 프로젝트","과제 없습니다"},
				{"객체 지향 언어와 실습","과제 없습니다"},
				{"프로그래밍 언어와 실습","과제 없습니다"},
				{"미적분학 및 연습","과제 없습니다"},
				{"미적분학 및 연습","과제 없습니다"},
				{"미적분학 및 연습","과제 없습니다"},
				{"미적분학 및 연습","과제 없습니다"},
				{"미적분학 및 연습","과제 없습니다"},
				{"미적분학 및 연습","과제 없습니다"},
		};

		DefaultTableModel defaulttablemodel = new DefaultTableModel(rowData,columnNames)
			{
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		
		NotificationTable = new JTable(defaulttablemodel);
		NotificationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		NotificationTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		NotificationTable.setBackground(SystemColor.control); // 생상 설정
		NotificationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		NotificationTable.addMouseListener(mouseadapter_jtable);
		
		TableColumnModel columnmodel = NotificationTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(150); 
		columnmodel.getColumn(1).setPreferredWidth(440);
		columnmodel.getColumn(0).setResizable(false);
		columnmodel.getColumn(1).setResizable(false);
		
		NotificationscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		NotificationscrollPane.setBounds(10,20, 610, 180); // 위치와 크기 설정
		NotificationscrollPane.setViewportView(NotificationTable);	//JTable 지정
		NotificationscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		NotificationscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		NotificationPanel.add(NotificationscrollPane);

		NotificationTitle = new JTextArea("123");
		NotificationTitle.setBounds(10, 210, 400, 20);
		NotificationTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		NotificationTitle.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		NotificationTitle.setEditable(false);
		NotificationTitle.setBackground(SystemColor.control); // 생상 설정
		NotificationPanel.add(NotificationTitle);
		
		NotificationContent = new JTextArea("123");
		NotificationContent.setBounds(10, 230, 600, 150);
		NotificationContent.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		NotificationContent.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		NotificationContent.setEditable(false);
		NotificationContent.setBackground(SystemColor.control); // 생상 설정
		NotificationPanel.add(NotificationContent);
		
		RightPanel.removeAll();
		RightPanel.add(NotificationPanel);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	//성적 확인 창을 구현하는 함수
	public void showGrade(){
		GradePanel = new JPanel();
		GradePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "성적 확인", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GradePanel.setBounds(10, 10, 630, 400); // 위치와 사이즈 설정
		GradePanel.setLayout(null); // 레이아웃을 Absolute로 설정
	
		String columnNames[] = {"강의","과제 번호","점수"}; // Column을 설명하기 위함
		//강의 목록 저장
		Object rowData[][] = { {"시스템 소프트웨어 프로그래밍",0,40},
				{"주니어 디자인 프로젝트",0,50},
				{"객체 지향 언어와 실습",40},
				{"프로그래밍 언어와 실습",60},
				{"미적분학 및 연습",0,60},
				{"미적분학 및 연습",2,60},
				{"미적분학 및 연습",3,60},
				{"미적분학 및 연습",4,60},
				{"미적분학 및 연습",5,60},
				{"미적분학 및 연습",6,60},
				{"미적분학 및 연습",6,60},
				{"미적분학 및 연습",6,60},
				{"미적분학 및 연습",6,60},
				{"미적분학 및 연습",6,60},
				{"미적분학 및 연습",6,60},
				{"미적분학 및 연습",6,60},
				{"미적분학 및 연습",6,60},
				{"미적분학 및 연습",6,60},
		};

		DefaultTableModel defaulttablemodel = new DefaultTableModel(rowData,columnNames)
			{
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		
		GradeTable = new JTable(defaulttablemodel);
		GradeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		GradeTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		GradeTable.setBackground(SystemColor.control); // 생상 설정
		GradeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		TableColumnModel columnmodel = GradeTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(150); 
		columnmodel.getColumn(1).setPreferredWidth(50); 
		columnmodel.getColumn(2).setPreferredWidth(100);
		columnmodel.getColumn(0).setResizable(false);
		columnmodel.getColumn(1).setResizable(false);
		columnmodel.getColumn(2).setResizable(false);
		
		GradescrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		GradescrollPane.setBounds(150,20, 300, 370); // 위치와 크기 설정
		GradescrollPane.setViewportView(GradeTable);	//JTable 지정
		GradescrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		GradescrollPane.setBackground(SystemColor.control); // 배경 색 설정
		GradePanel.add(GradescrollPane);
		
		RightPanel.removeAll();
		RightPanel.add(GradePanel);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	public void gotoClass(){
		ClassPanel = new JPanel();
		ClassPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "ABC" + " 강의실", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ClassPanel.setBounds(10, 10, 630, 400); // 위치와 사이즈 설정
		ClassPanel.setLayout(null); // 레이아웃을 Absolute로 설정
		
		RightPanel.removeAll();
		RightPanel.add(ClassPanel);
		contentPane.revalidate();
		contentPane.repaint();
	
	}
	
	//메인화면으로 돌아가도록하는 함수
	public void gotoMain(){
		RightPanel.removeAll(); // 오른쪽 패널을 모두 삭제
		setSubjectList(); // 수강중인 강의 목록을 띄움
		setRecommendList(); // 추천 강의 목록을 띄움
		contentPane.revalidate(); 
		contentPane.repaint(); //다시 Refresh해줌
	}
}
