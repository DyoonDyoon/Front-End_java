/**
 *  Created by JeongDongMin on 2015. 12. 05..
 */
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import controller.DataManager;
import controller.NetworkManager;
import controller.ReloadListener;
import model.Assignment;
import model.Lecture;
import model.User;
import model.Version;
import view.DetailView.DetailViewType;

public class ClassAssignment extends JFrame implements ReloadListener{
	
	User user = null; // 학생 정보를 저장하는 Student 객체 선언
	DataManager dataManager = null; // 데이터 매니저 객체
	NetworkManager networkManager = null; // 네트워크 매니저 객체
	
	JPanel contentPane; // 메인 프레임 선언
	JTable AssignmentTable; // 과제 목록 선언 
	JScrollPane AssignmentscrollPane; // 과제 목록에 스크롤 선언
	ArrayList<Assignment> Assignments = new ArrayList<Assignment>(); // 과제 정보 저장 arraylist 선언 및 생성
	
	private Lecture lecture; // 강의 정보
	private JButton moreButton; // 세부 사항 확인 버튼
	private JButton submitButton; // 과제 제출 버튼
	private DefaultTableModel tableModel; // 테이블 생성을 위한 default 테이블 모델
	private DetailView detailView; // 세부 사항 확인을 위한  detailView객체
	
	/**
	 * ClassAssignment 객체 생성자
	 * @param user
	 * @param dataManager
	 * @param networkManager
	 * @param lecture
	 */
	public ClassAssignment(User user, DataManager dataManager, NetworkManager networkManager, Lecture lecture){
		this.detailView = new DetailView(networkManager, lecture, user, this); // 알맞은 강의의 정보를 표시하기 위해 생성
		this.detailView.setVisible(false); // 버튼을 누르지 않았을 경우에는 보이지 않도록 함
		this.dataManager = dataManager; // 데이터 매니저 설정
		this.networkManager = networkManager; // 네트워크 매니저 설정
		this.user = user; // 유저 설정
		this.lecture = lecture; // 강의 설정
		
		setTitle("과제 확인"); // 객체의 제목 설정
		setSize(450,450); // 객체  Size 설정
		setLocation(820,20); // 창이 뜰 위치를 설정
		setLayout(null); // 객체의 Layout을 Absolute로 설정
		setResizable(false); // JFrame의 사이즈를 조정하지 못하도록함
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // X키를 누르면 프로그램이 종료하도록함

		setVisible(true);
		
		contentPane = new JPanel(); // 생성
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // 가장자리 구현
		setContentPane(contentPane); // 메인 Panel을 설정
		contentPane.setLayout(null); // 위의 레이아웃을 Absolute로 설정
		
		AssignmentTable = new JTable(); // 테이블 설정
		AssignmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		AssignmentTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		AssignmentTable.setBackground(SystemColor.control); // 생상 설정
		AssignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함

		needsReloadData(null); 
		
		AssignmentscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		AssignmentscrollPane.setBounds(10,10, 300, 370); // 위치와 크기 설정
		AssignmentscrollPane.setViewportView(AssignmentTable);	//JTable 지정
		AssignmentscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		AssignmentscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		contentPane.add(AssignmentscrollPane);
		
		// actionlistener 생성 및 구현
		ActionListener readAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//morebutton을 누를 경우 과제에 대한 세부 사항이 출력되도록 detailview를 구현해 출력
				Assignment assign = Assignments.get(AssignmentTable.getSelectedRow());
				detailView.setContext(DetailViewType.ASSIGNMENT, false, assign, null, null, null, null);
				detailView.setVisible(true);
			}
		};
		
		moreButton = new JButton("세부사항"); // 이름 설정 후 생성
		moreButton.setBounds(330, 20, 80, 20); // 위치와 사이즈 설정
		moreButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		moreButton.addActionListener(readAction); // actionlistener 지정
		contentPane.add(moreButton); // MenuAll 패널에 더함

		// actionlistener 생성 및 구현
		ActionListener makeAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (user.getType() == 0) { // 학생일 경우 레포트 제출 형식으로 detailview가 생성
					Assignment assign = Assignments.get(AssignmentTable.getSelectedRow());
					detailView.setContext(DetailViewType.ASSIGNMENT, true, assign, null, null, null, null);
				} else { // 교수일 경우 과제 생성 형식으로 detailview가 생성
					detailView.setContext(DetailViewType.ASSIGNMENT, true, null, null, null, null, null);
				}
				detailView.setVisible(true);
			}
		};
		String submitTitle = "";
		if (user.getType() == 0) { // 학생일 경우 버튼 이름을 "레포트제출"로 설정
			submitTitle = "레포트제출";
		} else if (user.getType() == 1) { // 교수일 경우 버튼 이름을 "과제 생성"으로 설정
			submitTitle = "과제생성";
		}
		submitButton = new JButton(submitTitle); // 이름 설정 후 생성
		submitButton.setBounds(330, 50, 80, 20); // 위치와 사이즈 설정
		submitButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		submitButton.addActionListener(makeAction); // actionlistener 지정
		contentPane.add(submitButton); // MenuAll 패널에 더함
	}

	
	@Override // 데이터를 가져올 경우 실행되는 함수 
	public void needsReloadData(DetailViewType type) {
		dataManager.openDB();
		Version version = dataManager.selectVersionDB(lecture.getLectureId()); // version을 사용함
		if (networkManager.syncAssignment(lecture.getLectureId(), version.assignVersion)) {
			// DB에서 해당 강의의 과제를 가져와 저장
			Assignments = dataManager.selectAssignmentDB(lecture.getLectureId());
		} else {
			JOptionPane.showMessageDialog(null, "과제를 받아올 수 없습니다!");
			dataManager.closeDB();
			return;
		}
		dataManager.closeDB();

		String[] columnNames = {"과제번호","제목"}; // Column을 설명하기 위함
		tableModel = new DefaultTableModel(Assignments.size(), columnNames.length) {
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		
		tableModel.setColumnIdentifiers(columnNames);
		for (int i = 0; i < Assignments.size(); ++i) {
			Assignment Assignment = Assignments.get(i);
			tableModel.setValueAt(Assignment.getAssignId(), i, 0); // 과제 번호를 불러와 지정
			tableModel.setValueAt(Assignment.title, i, 1); // 과제 제목을 불러와 지정
		}
		AssignmentTable.setModel(tableModel); // 불러온 정보를 통해 테이블 생성
		
		TableColumnModel columnmodel = AssignmentTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(60);  // 테이블 가로 크기 지정
		columnmodel.getColumn(1).setPreferredWidth(230);  
		columnmodel.getColumn(0).setResizable(false); // 크기 조정 비활성화
		columnmodel.getColumn(1).setResizable(false);
	}
}
