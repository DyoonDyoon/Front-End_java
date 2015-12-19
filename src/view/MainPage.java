/**
 *  Created by JeongDongMin on 2015. 12. 08..
 */
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
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
import model.Assignment;
import model.Grade;
import model.Lecture;
import model.LectureOutline;
import model.Notification;
import model.Question;
import model.RecommendedLecture;
import model.Submit;
import model.User;
import model.Version;

public class MainPage extends JFrame{
	
	ActionListener actionlistener; // 버튼을 클릭할 때 발생할 action을 구현하기 위한 actionlistener
	//메인 화면의 내용을 저장하는 JPanel 선언
	public JPanel contentPane; // 메인 화면에 나타낼 패널
	
	private BufferedImage img = null; // 로고 사진
	
	private User user = null; // 사용자 정보를 저장하는 User 객체 선언
	private DataManager dataManager = null; // 정보를 관리하기 위한 DataManager 객체
	private NetworkManager networkManager = null; // 네트워크를 관리하기 위한 NetworkManager 객체
	
	private ArrayList<Lecture> lectures = new ArrayList<Lecture>(); // 강의 정보를 저장하는 ArrayList
	private ArrayList<RecommendedLecture> recommendedlectures  = new ArrayList<RecommendedLecture>(); // 추천 강의를 저장하는 ArrayList
	private Vector<String[]> lectureTitles = new Vector<String[]>(); // 강의 제목을 저장하는 ArrayList
	private ArrayList<Assignment> assigns = new ArrayList<Assignment>(); // 모든 과제를 저장하는 ArrayList
	private ArrayList<Notification> notis = new ArrayList<Notification>(); // 모든 공지를 저장하는 ArrayList
	private ArrayList<Grade> grades = new ArrayList<Grade>(); // 모든 성적을 저장하는 ArrayList
	private ArrayList<Question> questions = new ArrayList<Question>(); // 모든 질문을 저장하는 ArrayList
	
	private Lecture lectureNow; // 강의 목록에서 선택한 강의를 저장하는 객체
	
	//메인 패널의 좌우 정보를 저장하는 JPanel 선언
	private JPanel LeftPanel;
	private JPanel RightPanel;
	
	//왼쪽 패널에 학생 정보를 저장하는 부분 구현을 위한 JPanel과 JLabel 선언
	private JPanel inform; // 정보를 저장하기 위한 JPanel
	private JLabel UserId;   // 학번 저장할 JLabel
	private JLabel UserName; // user 이름을 저장할 JLabel
	private JLabel UserMajor; // 학과를 저장할 JLabel

	//왼쪽 패널의 메뉴 부분을 구현하기 위한 JPanel과 JButton 
	private JPanel MenuAll; // 버튼을 저장할 JPanel
	private JButton gotoMain; // 메인으로 돌아가는 버튼
	private JButton CheckAssign; // 과제 확인을 위한 버튼
	private JButton CheckNoti; // 공지사항 확인을 위한 버튼
	private JButton CheckGrade; // 성적 확인을 위한 버튼
	
	//오른쪽 패널에 존재하는 수강중인 강의와 강의실 입장을 구현하기 위한 JPanel과 List, Button
	private JPanel SubjectPanel; // 강의실을 나타내는 JPanel
	private JList SubjectList; // 수강중인 강의를 저장하는 JList
	private JScrollPane scrollPane; // JList는  자체적으로 Scrollbar가 존재하지 않으므로 구현을 위해 필요함
	private JButton EnterSubject; // 강의실 입장을 위한 JButton
	private Vector<String> subject; // 수강중인 강의를 저장하도록하는 String형 Vector 배열
	
	//오른쪽 패널에 존재하는 추천 강의 구현을 위한 JTable, JPanel
	private JPanel RecSubjectPanel; // 추천 강의 구현을 위한 JPanel
	private JScrollPane RecscrollPane; // 위와 동일
	private JTable RecSubjectTable; // 추천 강의를 저장하는 JTable
	
	//오른쪽 패널에 존재하는 과제 구현을 위한 요소
	private JPanel AssignmentPanel; // 과제 구현 메인 패널
	private JTable AssignmentTable; // 과제 정보 저장 테이블
	private JScrollPane AssignmentscrollPane; // 테이블에 존재하는 스크롤
	private JTextArea AssignmentTitle; // 과제 제목을 나타내기 위한 textarea
	private JTextArea AssignmentContent; // 과제 내용을 나타내기 위한 textarea
	private JButton ReadAssignmentButton; // 과제를 읽을 때 누를 button
	
	//오른쪽패널에 존재하는 공지 구현을 위한 요소
	private JPanel NotificationPanel; // 공지 구현 메인 패널
	private JTable NotificationTable; // 공지 정보 저장 테이블
	private JScrollPane NotificationscrollPane; // 테이블에 존재하는 스크롤
	private JTextArea NotificationTitle; // 공지 제목을 나타내기 위한 textarea
	private JTextArea NotificationContent; // 공지 내용을 나타내기 위한 textarea
	private JButton ReadNotificationButton; // 공지를 읽을 때 누를 button
	
	//오른쪽 패널에 존재하는 성적 확인을 위한 요소
	private JPanel GradePanel; // 성적 확인 구현 메인 패널
	private JTable GradeTable; // 성적을 저장할 테이블
	private JScrollPane GradescrollPane; // 테이블에 존재하는 스크롤
	
	//오른쪽 패널에 존재하는 질의 응답을 위한 요소
	private JPanel QuestionPanel; // 질의 읍답 구현 메인 패널
	private JTable QuestionTable; // 질문를 저장하는 테이블
	private JScrollPane QuestionscrollPane; // 테이블에 존재하는 스크롤
	private JTextArea QuestionTitle; // 질문 제목을 나타내기 위한 textarea
	private JTextArea QuestionContent; // 질문 내용을 나타내기 위한 textarea
	private JButton ReadQuestionButton; // 질문을 읽기 위한  button

	//수강중인 강의실에 입장했을 경우 오른쪽 패널에 나타낼 요소들
	private JPanel ClassPanel; // 강의실 구현 메인 패널
	private JPanel ClassNamePanel; // 강의 이름을 저장하는 패널
	private JLabel ClassName; // 강의 제목
	private JLabel ClassProfessor; // 강의 교수님 성함
	private JButton ClassNotification; // 수강 중인 과목의 공지를 읽을 때 사용하는 button
	private JButton ClassQuestion;// 수강 중인 과목의 질문을 읽을 때 사용하는 button
	private JButton ClassAssignment; // 수강 중인 과목의 과제를 읽을 때 사용하는 button
	private JButton ClassGrade; // 수강 중인 과목의 성적을 확인 할 때 사용하는 button
	
	
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
		
		//ActionListener를 지정하여 버튼을 눌렀을 때 실행될 함수를 설정
		actionlistener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {//ActionListener implements
				switch(event.getActionCommand())// ActionCommand
				{
					case "CheckAssign":
						showAssign(); // 과제 확인 버튼을 눌렀을 경우 실행
						break;
					case "CheckNoti":
						showNoti(); // 공지 확인 버튼을 눌렀을 경우 실행
						break;
					case "CheckGrade":
						showGrade(); // 성적 확인 버튼을 눌렀을 경우 실행
						break;
					case "CheckQuestion":
						showQuestion(); // 질문 확인 버튼을 눌렀을 경우 실행
						break;
					case "EnterSubject":
						gotoClass(); // 강의실 입장 버튼을 눌렀을 경우 실행
						break;
					case "gotoMain":
						gotoMain(); // 메인 화면으로 버튼을 눌렀을 경우 실행
						break;
					case "ReadAssign": // 과제 조회 버튼을 눌렀을 경우 실행
						Assignment assign = assigns.get(AssignmentTable.getSelectedRow()); // 선택한 과제를 읽어오도록함
						AssignmentTitle.setText(assign.title); // 과제 제목 지정
						AssignmentContent.setText(assign.description); // 과제 내용 지정
						break;
					case "ReadNoti": // 공지 조회 버튼을 눌렀을 경우 실행
						Notification noti = notis.get(NotificationTable.getSelectedRow()); // 선택한 공지를 읽어오도록함
						NotificationTitle.setText(noti.title); // 공지 제목 지정
						NotificationContent.setText(noti.description); // 공지 내용 지정
						break;
					case "ReadQuestion": // 교수님이 질문 조회 버튼을 눌렀을 경우 실행
						String lectureName = null; // 강의 제목 선언
						Question question = questions.get(QuestionTable.getSelectedRow()); // 선택한 질문을 읽어오도록함
						for (int j = 0; j < lectures.size(); ++j) { 
							String[] lectureTitle = lectureTitles.get(j); // 강의 제목을 찾아 지정
							if (question.getLectureId().equals(lectureTitle[1])) {
								lectureName = lectureTitle[0];
								break;
							}
						}
						QuestionTitle.setText(lectureName + "\t\t" +question.getStudentId()); // 강의 제목과 질문한 학생의 학번을 제목으로 지정
						QuestionContent.setText(question.content); // 질문 내용 지정
						break; 
					case "ClassNoti": // 강의실 내부에서 공지 확인 버튼을 눌렀을 경우 실행 
						// 현재 과목과 유저 정보와 dataManager, networkManager 객체를 가지고 생성
						new ClassNotification(user, dataManager, networkManager, lectureNow); 
						break;
					case "ClassAssign": // 강의실 내부에서 과제 확인 버튼을 눌렀을 경우 실행
						// 현재 과목과 유저 정보와 dataManager, networkManager 객체를 가지고 생성
						new ClassAssignment(user, dataManager, networkManager, lectureNow);
						break;
					case "ClassQuestion": // 강의실 내부에서 질문 확인 버튼을 눌렀을 경우 실행
						// 현재 과목과 유저 정보와 dataManager, networkManager 객체를 가지고 생성
						new ClassQuestion(user, dataManager, networkManager, lectureNow);
						break;
					case "ClassGrade": // 강의실 내부에서 성적 확인 버튼을 눌렀을 경우 실행
						// 현재 과목과 유저 정보와 dataManager, networkManager 객체를 가지고 생성
						new ClassGrade(user, dataManager, networkManager, lectureNow);
						break;
					default: // 그 외의 default 값은 구현되지 않음
						JOptionPane.showMessageDialog(null, "구현 ㄴㄴ");
					break;
				}
			}
		};
		
	}
	
	/**
	 * Main에서 로그인을 할 경우 그 User의 정보를 통해 MainPage 구현
	 * @param user
	 * @param networkManager
	 * @param dataManager
	 */
	public void setContext(User user, NetworkManager networkManager, DataManager dataManager){
		this.networkManager = networkManager;
		this.dataManager = dataManager;
		this.user = user; // User 객체를 받아와 설정
		RightPanel.removeAll(); // 오른쪽 패널을 모두 삭제
		setSubjectList(); // 수강중인 강의 목록 구현
		setInform(); // user 정보 표시창을 구현
		setMenu(); // 메뉴 표시창을 구현
		if(user.getType() == 0)	setRecommendList(); // 로그인 한 user가 학생일 경우 추천 강의 목록 구현
		else if(user.getType() == 1){ // 교수님일 경우 추천 강의 부분에 로고를 대신 출력하도록 함
			try{
				img = ImageIO.read(new File("img/login.png"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"이미지 불러오기 실패!");
				System.exit(0);
			}
			MyPanel logo = new MyPanel();
	        logo.setBounds(340, 100, 230, 150);
	        RightPanel.add(logo);
		}
		contentPane.add(LeftPanel); // 왼쪽 패널을 Main Panel에 추가
		contentPane.add(RightPanel); // 오른쪽 패널을 Main Panel에 추가
		contentPane.revalidate(); // 메인 프레임을 refresh함
		contentPane.repaint(); 
	}
	
	/**
	 * user 정보를 통해 이름,학번,학과를 출력하는 Panel을 생성하는 함수
	 * 교수님일 경우에도 같은 방식으로 출력되도록 함
	 */
	public void setInform(){
		// user 정보를 저장하는 Panel 구현
		inform = new JPanel(); // 생성
		inform.setLocation(12,47); // 위치 설정
		inform.setSize(114, 100); // 사이즈 설정
		inform.setBorder(BorderFactory.createLineBorder(Color.black)); // 가장자리를 검은색 실선으로 설정
		
		//user 학번을 저장하는 JLabel 구현
		UserId = new JLabel(user.getId()); // 생성
		UserId.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 크기를 설정
		UserId.setBounds(12, 35, 34, 15); // 위치와 사이즈를 설정
		UserId.setVisible(true); // 보이도록 함
		inform.add(UserId); // user 정보를 저장하는 Panel에 더함
		
		//user 이름을 저장하는 JLabel 구현
		UserName = new JLabel(user.name); // 생성
		UserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 크기를 설정
		UserName.setVisible(true); //보이도록 함
		UserName.setBounds(12, 10, 57, 15); // 위치와 사이즈를 설정
		inform.add(UserName); // user 정보를 저장하는 Panel에 더함
		
		//학과를 저장하는 JLabel 구현
		UserMajor = new JLabel(user.major); // 생성
		UserMajor.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 크기를 설정
		UserMajor.setVisible(true); // 보이도록 함
		UserMajor.setBounds(12, 60, 57, 15); // 위치와 사이즈를 설정
		inform.add(UserMajor); // user 정보를 저장하는 Panel에 더함
		
		inform.setVisible(true); // 구현한 Panel을 보이도록 함
		// Title을 가지는 가장자리로 구현하도록 함 
		inform.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "개인 정보", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		LeftPanel.add(inform); // 왼쪽 패널에 구현한 Panel을 더함
	}
	
	/**
	 * 메뉴를 구현하도록하는 함수
	 */
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
		if(user.getType() == 1){
			CheckGrade.setText("질문 확인");
			CheckGrade.setActionCommand("CheckQuestion");
		}
		MenuAll.add(CheckGrade); // MenuAll 패널에 더함
		
		LeftPanel.add(MenuAll); // MenuAll패널을 왼쪽패널에 더함
	}
	
	/**
	 * 수강중인 강의 목록을 띄우기 위한 패널 구현 함수
	 * User 객체를 통해 데이터를 가지고 학생이면 수강 중인 강의를 가져오고 교수님이라면 강의 중인 강의를 가져오도록 함
	 */
	public void setSubjectList()
	{	//강의 목록 저장 패널 구현
		SubjectPanel = new JPanel(); // 생성
		// 가장자리 타이틀을 가지고 있는 선을 생성
		SubjectPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "수강 과목", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		SubjectPanel.setBounds(50, 20, 188, 309); // 위치와 사이즈 설정
		SubjectPanel.setLayout(null); // 레이아웃을 Absolute로 설정
		
		subject = new Vector<String>(); // 강의 목록을 저장할 배열 선언  -> 리스트에 추가
		lectureTitles = new Vector<String[]>(); // 강의 목록을 저장할 배열 선언  -> dataManager을 통해 가져옴
		if (networkManager.syncLectures(user.getId())) { // user의 id로 부터  강의 목록을 가져옴
			dataManager.openDB();
			lectures = dataManager.selectLectureDB(user.getId());
			for (int i = 0; i < lectures.size(); i++) {
				Lecture lecture = lectures.get(i); // 강의 목록에서 하나의 강의를 가져옴
				//lectureoutline에서 검색하여 강의 정보를 가져옴
				LectureOutline lectureOutline = dataManager.selectLectureOutlineDB(lecture.getLectureId());
				if (lectureOutline != null) {
					String[] lectureTitle = new String[2];
					lectureTitle[0] = lectureOutline.title; // 제목 지정
					lectureTitle[1] = lectureOutline.getLectureId(); // 번호 지정
					lectureTitles.add(lectureTitle); // 강의 제목에 추가
					subject.add(lectureOutline.title); // 강의 목록에 추가
				}
			}
			dataManager.closeDB();
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
		EnterSubject.addActionListener(actionlistener);
		EnterSubject.setActionCommand("EnterSubject"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		SubjectPanel.add(EnterSubject); // SubjectPanel에 더함
		
		RightPanel.add(SubjectPanel); // SubjectPanel을 오른쪽 패널에 더함
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	/**
	 * 추천 강의 목록을 띄우기 위한 함수
	 */
	public void setRecommendList(){
		// 추천 강의 목록을 구현하기 위한 Panel 설정
		RecSubjectPanel = new JPanel(); // 생성
		// 가장자리에 타이틀을 가지고 있는 선을 추가하기 위함
		RecSubjectPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "추천 강의", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		RecSubjectPanel.setBounds(350, 20, 250, 265); // 위치와 사이즈 설정
		RecSubjectPanel.setLayout(null); // 레이아웃을 Absolute로 설정
		
		ArrayList<RecommendedLecture> temp  = new ArrayList<RecommendedLecture>(); // 추천 강의를 저장하는 arraylist
		
		recommendedlectures = new ArrayList<RecommendedLecture>(); // 추천 강의 저장 리스트
		dataManager.openDB();
		temp = dataManager.selectRecommendedLecture(); // db에서 강의 리스트를 가져옴
		dataManager.closeDB();
		
		for(int i=0;i<20;i++){
			int r = (int)(Math.random()*1000)%temp.size(); // 가져온 강의 리스트 중에 랜덤으로 뽑아 옴
			recommendedlectures.add(temp.get(r)); // 강의를 저장함
		}
		String[] columnNames = {"제목","학점"}; // Column을 설명하기 위함
		//테이블의 주제를 가지고 틀을 잡고 생성
		DefaultTableModel defaulttablemodel = new DefaultTableModel(recommendedlectures.size(), columnNames.length)
			{
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		
		//저장한 강의를 통해 테이블을 구현함
		defaulttablemodel.setColumnIdentifiers(columnNames);
		for (int i = 0; i < recommendedlectures.size(); ++i) {
			RecommendedLecture recommendedlecture = recommendedlectures.get(i);
			defaulttablemodel.setValueAt(recommendedlecture.title, i, 1);
			defaulttablemodel.setValueAt(recommendedlecture.getPoint(), i, 0);
		}
		
		RecSubjectTable = new JTable(defaulttablemodel); // Default 테이블을 통해 JTable 생성
		RecSubjectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		RecSubjectTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		RecSubjectTable.setBackground(SystemColor.control); // 생상 설정
		RecSubjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		//JTable의 Column의 크기를 지정하도록 함
		TableColumnModel columnmodel = RecSubjectTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(50); // 강의 제목 칸은 170픽셀로 설정
		columnmodel.getColumn(1).setPreferredWidth(170);  // 학점 칸은 50픽셀로 설정
		columnmodel.getColumn(0).setResizable(false); // 사이즈 조절을 비활성화함
		columnmodel.getColumn(1).setResizable(false); // 사이즈 조절을 비활성화함
		
		RecscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		RecscrollPane.setBounds(12, 21, 230, 235); // 위치와 크기 설정
		RecscrollPane.setViewportView(RecSubjectTable);	//JTable 지정
		RecscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		RecscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		RecSubjectPanel.add(RecscrollPane); // RecSubjectPanel에 더함
		
		RightPanel.add(RecSubjectPanel); // RightPanel에 더함
	}
	
	/**
	 * 과제 확인 창을 구현하는 함수
	 */
	public void showAssign(){
		AssignmentPanel = new JPanel();
		AssignmentPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "과제 확인", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		AssignmentPanel.setBounds(10, 10, 630, 400); // 위치와 사이즈 설정
		AssignmentPanel.setLayout(null); // 레이아웃을 Absolute로 설정
	
		assigns = new ArrayList<Assignment>(); // 과제 정보를 저장하는 arraylist
		dataManager.openDB();
		//db에서 과제 정보를 가져와 리스트에 저장하는 과정
		for (int i = 0; i < lectures.size(); ++i) {
			Lecture lecture = lectures.get(i);
			Version version = dataManager.selectVersionDB(lecture.getLectureId());
			if (version == null)
				continue;
			if(!this.isVisible()) return;
			if (networkManager.syncAssignment(lecture.getLectureId(), version.assignVersion)) {
				ArrayList<Assignment> localAssign = dataManager.selectAssignmentDB(lecture.getLectureId());
				if (localAssign != null && !localAssign.isEmpty()) {
					assigns.addAll(localAssign);
				}
			}
		}
		dataManager.closeDB();
		
		String[] columnNames = {"강의","제목"}; // Column을 설명하기 위함
		//테이블의 주제를 가지고 틀을 잡고 생성
		DefaultTableModel defaulttablemodel = new DefaultTableModel(assigns.size(), columnNames.length)
			{
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};

		//저장한 과제를 통해 테이블을 구현함
		defaulttablemodel.setColumnIdentifiers(columnNames);
		for (int i = 0; i < assigns.size(); ++i) {
			Assignment assign = assigns.get(i);
			for (int j = 0; j < lectures.size(); ++j) {
				String[] lectureTitle = lectureTitles.get(j);
				if (assign.getLectureId().equals(lectureTitle[1])) {
					defaulttablemodel.setValueAt(lectureTitle[0], i, 0);
					break;
				}
			}
			defaulttablemodel.setValueAt(assign.title, i, 1);
		}
		
		AssignmentTable = new JTable(defaulttablemodel);
		AssignmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		AssignmentTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		AssignmentTable.setBackground(SystemColor.control); // 색상 설정
		AssignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		TableColumnModel columnmodel = AssignmentTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(150); // 가로 길이 지정
		columnmodel.getColumn(1).setPreferredWidth(440);
		columnmodel.getColumn(0).setResizable(false); // 사이즈 조정 비활성화
		columnmodel.getColumn(1).setResizable(false);
		
		AssignmentscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		AssignmentscrollPane.setBounds(10,20, 610, 180); // 위치와 크기 설정
		AssignmentscrollPane.setViewportView(AssignmentTable);	//JTable 지정
		AssignmentscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		AssignmentscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		AssignmentPanel.add(AssignmentscrollPane);

		AssignmentTitle = new JTextArea(); // 제목 textarea 생성
		AssignmentTitle.setBounds(10, 205, 400, 20); // 위치와 크기 설정
		AssignmentTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		AssignmentTitle.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		AssignmentTitle.setEditable(false); // 수정 비활성화
		AssignmentTitle.setBackground(SystemColor.control); // 색상 설정
		AssignmentPanel.add(AssignmentTitle);
		
		AssignmentContent = new JTextArea(); // 내용 textarea 생성
		AssignmentContent.setBounds(10, 230, 600, 150); // 위치와 크기 설정
		AssignmentContent.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		AssignmentContent.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		AssignmentContent.setEditable(false); // 수정 비활성화
		AssignmentContent.setBackground(SystemColor.control); // 색상 설정
		AssignmentPanel.add(AssignmentContent);
		
		ReadAssignmentButton = new JButton("조회"); // 버튼 생성
		ReadAssignmentButton.setBounds(450, 205, 120, 20); // 위치와 설정
		ReadAssignmentButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		ReadAssignmentButton.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		ReadAssignmentButton.addActionListener(actionlistener); // actionlistener 설정
		ReadAssignmentButton.setActionCommand("ReadAssign"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		AssignmentPanel.add(ReadAssignmentButton);
		
		RightPanel.removeAll();
		RightPanel.add(AssignmentPanel);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	/**
	 * 공지 확인 창을 구현하는 함수
	 */
	public void showNoti(){
		NotificationPanel = new JPanel(); // 패널 생성
		NotificationPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "공지 확인", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		NotificationPanel.setBounds(10, 10, 630, 400); // 위치와 사이즈 설정
		NotificationPanel.setLayout(null); // 레이아웃을 Absolute로 설정
	
		String[] columnNames = {"강의","제목"}; // Column을 설명하기 위함
		//강의 목록 저장

		notis = new ArrayList<Notification>(); // 강의 정보 저장 arraylist
		dataManager.openDB();
		//db에서 강의 정보를 통해 공지를 가져오는 과정
		for (int i = 0; i < lectures.size(); ++i) {
			Lecture lecture = lectures.get(i);
			Version version = dataManager.selectVersionDB(lecture.getLectureId());
			if (version == null)
				continue;
			if(!this.isVisible()) return;
			if (networkManager.syncNotification(lecture.getLectureId(), version.notiVersion)) {
				ArrayList<Notification> localNoti = dataManager.selectNotificationDB(lecture.getLectureId());
				if (localNoti != null && !localNoti.isEmpty()) {
					notis.addAll(localNoti);
				}
			}
		}
		dataManager.closeDB();
		//테이블의 주제를 가지고 틀을 잡고 생성
		DefaultTableModel defaulttablemodel = new DefaultTableModel(notis.size(), columnNames.length)
			{
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		
		// 가져온 공지를 통해 테이블을 구현
		defaulttablemodel.setColumnIdentifiers(columnNames);
		for (int i = 0; i < notis.size(); ++i) {
			Notification noti = notis.get(i);
			for (int j = 0; j < lectures.size(); ++j) {
				String[] lectureTitle = lectureTitles.get(j);
				if (noti.getLectureId().equals(lectureTitle[1])) {
					defaulttablemodel.setValueAt(lectureTitle[0], i, 0);
					break;
				}
			}
			defaulttablemodel.setValueAt(noti.title, i, 1);
		}
		
		NotificationTable = new JTable(defaulttablemodel); // 테이블 생성
		NotificationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		NotificationTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		NotificationTable.setBackground(SystemColor.control); // 색상 설정
		NotificationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		TableColumnModel columnmodel = NotificationTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(150);  // 가로 길이 지정
		columnmodel.getColumn(1).setPreferredWidth(440);
		columnmodel.getColumn(0).setResizable(false); // 사이즈 조절 비활성화
		columnmodel.getColumn(1).setResizable(false);
		
		NotificationscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		NotificationscrollPane.setBounds(10,20, 610, 180); // 위치와 크기 설정
		NotificationscrollPane.setViewportView(NotificationTable);	//JTable 지정
		NotificationscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		NotificationscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		NotificationPanel.add(NotificationscrollPane);

		NotificationTitle = new JTextArea(); // 제목 textarea 생성
		NotificationTitle.setBounds(10, 205, 400, 20); // 위치와 크기 설정
		NotificationTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		NotificationTitle.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		NotificationTitle.setEditable(false); // 수정 비활성화
		NotificationTitle.setBackground(SystemColor.control); // 색상 설정
		NotificationPanel.add(NotificationTitle);
		
		NotificationContent = new JTextArea(); // 내용 textarea 생성
		NotificationContent.setBounds(10, 230, 600, 150); // 위치와 크기 설정
		NotificationContent.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		NotificationContent.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		NotificationContent.setEditable(false); // 수정 비활성화
		NotificationContent.setBackground(SystemColor.control); // 색상 설정
		NotificationPanel.add(NotificationContent);

		ReadNotificationButton = new JButton("조회"); // 버튼 생성
		ReadNotificationButton.setBounds(450, 205, 120, 20); // 위치와 크기 설정
		ReadNotificationButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		ReadNotificationButton.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		ReadNotificationButton.addActionListener(actionlistener); // actionlister 추가
		ReadNotificationButton.setActionCommand("ReadNoti"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		NotificationPanel.add(ReadNotificationButton);
		
		RightPanel.removeAll();
		RightPanel.add(NotificationPanel);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	/**
	 * 성적 확인 창을 구현하는 함수
	 */
	public void showGrade(){
		GradePanel = new JPanel(); // 패널 생성
		GradePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "성적 확인", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GradePanel.setBounds(10, 10, 630, 400); // 위치와 사이즈 설정
		GradePanel.setLayout(null); // 레이아웃을 Absolute로 설정
	
		String[] columnNames = {"강의","과제 번호","점수"}; // Column을 설명하기 위함
		
		//유저를 통해 성적 정보를 모두 가져옴
		grades = new ArrayList<Grade>();
		if (networkManager.syncGrade(user.getId(), null)) {
			dataManager.openDB();
			grades = dataManager.selectGradeDB(user.getId(), null);
			dataManager.closeDB();
		}
		//테이블의 주제를 가지고 틀을 잡고 생성
		DefaultTableModel defaulttablemodel = new DefaultTableModel(grades.size(), columnNames.length)
			{
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		
		// 가져온 성적 정보를 통해 테이블을 구현
		defaulttablemodel.setColumnIdentifiers(columnNames);
		for (int i = 0; i < grades.size(); ++i) {
			Grade grade = grades.get(i);
			for (int j = 0; j < lectures.size(); ++j) {
				String[] lectureTitle = lectureTitles.get(j);
				if (grade.getLectureId().equals(lectureTitle[1])) {
					defaulttablemodel.setValueAt(lectureTitle[0], i, 0);
					break;
				}
			}
			dataManager.openDB();
			Submit submit = dataManager.selectSubmit(grade.getSubmitId());
			dataManager.closeDB();
			defaulttablemodel.setValueAt(submit.getAssignId(), i, 1);
			defaulttablemodel.setValueAt(grade.getScore(), i, 2);
		}
		
		
		GradeTable = new JTable(defaulttablemodel); // 테이블 구현
		GradeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		GradeTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		GradeTable.setBackground(SystemColor.control); // 색상 설정
		GradeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		TableColumnModel columnmodel = GradeTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(150); // 가로 길이 설정
		columnmodel.getColumn(1).setPreferredWidth(50); 
		columnmodel.getColumn(2).setPreferredWidth(100);
		columnmodel.getColumn(0).setResizable(false); // 사이즈 조절 비활성화
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
	
	/**
	 * 질문을 출력하는 함수
	 */
	public void showQuestion(){
		QuestionPanel = new JPanel(); // 패널 생성
		QuestionPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "질문 확인", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		QuestionPanel.setBounds(10, 10, 630, 400); // 위치와 사이즈 설정
		QuestionPanel.setLayout(null); // 레이아웃을 Absolute로 설정
	
		questions = new ArrayList<Question>(); // 질문을 저장할 arraylist
		dataManager.openDB();
		//db에서 모든 질문을 가져오는 과정
		for (int i = 0; i < lectures.size(); ++i) {
			Lecture lecture = lectures.get(i);
			if(!this.isVisible()) return;
			if (networkManager.syncQuestion(null, lecture.getLectureId())) {
				ArrayList<Question> localQuestion = dataManager.selectQuestionDB(null,lecture.getLectureId());
				if (localQuestion != null && !localQuestion.isEmpty()) {
					questions.addAll(localQuestion);
				}
			}
		}
		dataManager.closeDB();
		
		String[] columnNames = {"강의","질문자","내용"}; // Column을 설명하기 위함
		
		//테이블의 주제를 가지고 틀을 잡고 생성
		DefaultTableModel defaulttablemodel = new DefaultTableModel(questions.size(), columnNames.length)
			{
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		//질문을 통해 질문이 존재하는 강의 이름, 학번, 내용을 출력
		defaulttablemodel.setColumnIdentifiers(columnNames);
		for (int i = 0; i < questions.size(); ++i) {
			Question question = questions.get(i);
			for (int j = 0; j < lectures.size(); ++j) {
				String[] lectureTitle = lectureTitles.get(j);
				if (question.getLectureId().equals(lectureTitle[1])) {
					defaulttablemodel.setValueAt(lectureTitle[0], i, 0);
					break;
				}
			}
			defaulttablemodel.setValueAt(question.getStudentId(), i, 1);
			defaulttablemodel.setValueAt(question.content, i, 2);
		}
		
		QuestionTable = new JTable(defaulttablemodel); // 테이블 생성
		QuestionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		QuestionTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		QuestionTable.setBackground(SystemColor.control); // 색상 설정
		QuestionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		TableColumnModel columnmodel = QuestionTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(150); // 가로 크기 지정
		columnmodel.getColumn(1).setPreferredWidth(140); 
		columnmodel.getColumn(2).setPreferredWidth(300);
		columnmodel.getColumn(0).setResizable(false); // 사이즈 조절 비활성화
		columnmodel.getColumn(1).setResizable(false);
		columnmodel.getColumn(2).setResizable(false);
		
		QuestionscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		QuestionscrollPane.setBounds(10,20, 610, 180); // 위치와 크기 설정
		QuestionscrollPane.setViewportView(QuestionTable);	//JTable 지정
		QuestionscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		QuestionscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		QuestionPanel.add(QuestionscrollPane);

		QuestionTitle = new JTextArea(); // 질문 제목 textarea 생성
		QuestionTitle.setBounds(10, 205, 400, 20);  // 위치와 크기 설정
		QuestionTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		QuestionTitle.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		QuestionTitle.setEditable(false); // 내용 변경 비활성화
		QuestionTitle.setBackground(SystemColor.control); // 색상 설정
		QuestionPanel.add(QuestionTitle);
		
		QuestionContent = new JTextArea(); // 질문 내용 textarea 생성
		QuestionContent.setBounds(10, 230, 600, 150); // 위치와 크기 설정
		QuestionContent.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		QuestionContent.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		QuestionContent.setEditable(false); // 내영 변경 비활성화
		QuestionContent.setBackground(SystemColor.control); // 색상 설정
		QuestionPanel.add(QuestionContent);
		
		ReadQuestionButton = new JButton("조회"); // 버튼 생성
		ReadQuestionButton.setBounds(450, 205, 120, 20); // 위치와 크기 설정
		ReadQuestionButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		ReadQuestionButton.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		ReadQuestionButton.addActionListener(actionlistener); // actionlister 설정
		ReadQuestionButton.setActionCommand("ReadQuestion"); // 클릭할 경우 ActionListener에 보낼 Command 설정
		QuestionPanel.add(ReadQuestionButton);
		
		RightPanel.removeAll();
		RightPanel.add(QuestionPanel);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	/**
	 * 강의실 입장 버튼을 눌렀을 경우 지정한 강의의 강의실로 입장하게 하는 함수
	 */
	public void gotoClass(){
		int lectureIdx = SubjectList.getSelectedIndex(); // 선택한 강의의 정보를 가져옴
		Lecture lecture = lectures.get(lectureIdx);
		dataManager.openDB();
		LectureOutline lectureoutline = dataManager.selectLectureOutlineDB(lecture.getLectureId()); // 선택한 강의의 정보를 DB에서 가져옴
		dataManager.closeDB();
		lectureNow = lecture; // 현재 선택하고있는 강의로 지정
		
		ClassPanel = new JPanel(); // 패널 생성
		ClassPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "강의실", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ClassPanel.setBounds(10, 10, 630, 400); // 위치와 사이즈 설정
		ClassPanel.setLayout(null); // 레이아웃을 Absolute로 설정
		
		ClassNamePanel = new JPanel(); // 클래스 이름을 나타내는 패널 생성
		ClassNamePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "강의 정보", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ClassNamePanel.setBounds(40,40, 550, 120); // 위치와 사이즈 설정
		ClassNamePanel.setLayout(null); // 레이아웃을 Absolute로 설정
		
		ClassName = new JLabel(); // 클래스 이름 label 생성
		ClassName.setText(lectureoutline.title + " [ " + lectureoutline.getLectureId() + " ]"); // 강의 이름과 강의 번호로 라벨 지정
		ClassName.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 폰트와 폰트 크기 설정
		ClassName.setLocation(20,20); // 위치 지정
		ClassName.setSize(ClassName.getPreferredSize()); // 크기 지정
		ClassNamePanel.add(ClassName);
		
		ClassProfessor = new JLabel(); // 교수 이름 label 생성
		ClassProfessor.setText("담당 교수 : " +lectureoutline.professorName); // 교수 이름으로 라벨 지정
		ClassProfessor.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 폰트 크기 설정
		ClassProfessor.setLocation(20,70); // 위치 지정
		ClassProfessor.setSize(ClassProfessor.getPreferredSize()); // 크기 지정
		ClassNamePanel.add(ClassProfessor);

		ClassPanel.add(ClassNamePanel);
		
		ClassNotification = new JButton("공지 사항"); // 공지 사항 확인 버튼 생성
		ClassNotification.setBounds(65, 190, 240, 80); // 위치와 크기 지정
		ClassNotification.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 폰트와 폰트 크기 설정
		ClassNotification.addActionListener(actionlistener); // actionlistener 설정
		ClassNotification.setActionCommand("ClassNoti"); // 액션 커맨드 설정
		ClassPanel.add(ClassNotification);
		
		ClassAssignment = new JButton("과제 확인"); // 과제 확인 버튼 생성
		ClassAssignment.setBounds(325, 190, 240, 80); // 위치와 크기 지정
		ClassAssignment.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 폰트와 폰트 크기 설정
		ClassAssignment.addActionListener(actionlistener); // actionlistener 설정
		ClassAssignment.setActionCommand("ClassAssign"); // 액션 커맨드 설정
		ClassPanel.add(ClassAssignment);
		
		ClassQuestion = new JButton("질의 응답"); // 질의 응답 확인 버튼 생성
		ClassQuestion.setBounds(65, 290, 240, 80); // 위치와 크기 지정
		ClassQuestion.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 폰트와 폰트 크기 설정
		ClassQuestion.addActionListener(actionlistener); // actionlistener 설정
		ClassQuestion.setActionCommand("ClassQuestion"); // 액션 커맨드 설정
		ClassPanel.add(ClassQuestion);
	
		ClassGrade = new JButton("성적 확인"); // 성적 확인 버튼 생성
		ClassGrade.setBounds(325, 290, 240, 80); // 위치와 크기 지정
		ClassGrade.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 폰트와 폰트 크기 설정
		ClassGrade.addActionListener(actionlistener); // actionlistener 설정
		ClassGrade.setActionCommand("ClassGrade"); // 액션 커맨드 설정
		ClassPanel.add(ClassGrade);
		
		RightPanel.removeAll();
		RightPanel.add(ClassPanel);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	/**
	 * 메인화면으로 돌아가도록하는 함수
	 */
	public void gotoMain(){
		RightPanel.removeAll(); // 오른쪽 패널을 모두 삭제
		setSubjectList(); // 수강중인 강의 목록을 띄움
		if(user.getType() == 0)	setRecommendList(); // 추천 강의 목록 구현
		else if(user.getType() == 1){ // 교수일 경우 추천 강의 목록 자리에 로고 구현
			try{
				img = ImageIO.read(new File("img/login.png"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"이미지 불러오기 실패!");
				System.exit(0);
			}
			MyPanel logo = new MyPanel();
	        logo.setBounds(340, 100, 230, 150);
	        RightPanel.add(logo);
		}
		contentPane.revalidate(); 
		contentPane.repaint(); //다시 Refresh해줌
	}
	
	// 로고 이미지를 출력하도록 하는 중첩 클래스 구현
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(img, 20, 20, null);
        }
    }
}