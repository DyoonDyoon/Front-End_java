package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
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
	DataManager dataManager = null;
	NetworkManager networkManager = null;
	
	JPanel contentPane;
	JTable AssignmentTable;
	JScrollPane AssignmentscrollPane;
	ArrayList<Assignment> Assignments = new ArrayList<Assignment>();
	
	private Lecture lecture;
	private JButton moreButton;
	private JButton submitButton;
	private DefaultTableModel tableModel;
	private DetailView detailView;
	
	public ClassAssignment(User user, DataManager dataManager, NetworkManager networkManager, Lecture lecture){
		this.detailView = new DetailView(networkManager, lecture, user, this);
		this.detailView.setVisible(false);
		this.dataManager = dataManager;
		this.networkManager = networkManager;
		this.user = user;
		this.lecture = lecture;
		
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
		
		AssignmentTable = new JTable();
		AssignmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		AssignmentTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		AssignmentTable.setBackground(SystemColor.control); // 생상 설정
		AssignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함

		needsReloadData(null);
		
		TableColumnModel columnmodel = AssignmentTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(30); 
		columnmodel.getColumn(1).setPreferredWidth(150); 
		columnmodel.getColumn(0).setResizable(false);
		columnmodel.getColumn(1).setResizable(false);
		
		AssignmentscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		AssignmentscrollPane.setBounds(10,10, 300, 370); // 위치와 크기 설정
		AssignmentscrollPane.setViewportView(AssignmentTable);	//JTable 지정
		AssignmentscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		AssignmentscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		contentPane.add(AssignmentscrollPane);
		
		ActionListener readAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Assignment assign = Assignments.get(AssignmentTable.getSelectedRow());
				detailView.setContext(DetailViewType.ASSIGNMENT, false, assign, null, null, null, null);
				detailView.setVisible(true);
			}
		};
		
		moreButton = new JButton("세부사항"); // 이름 설정 후 생성
		moreButton.setBounds(330, 20, 80, 20); // 위치와 사이즈 설정
		moreButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		moreButton.addActionListener(readAction);
		contentPane.add(moreButton); // MenuAll 패널에 더함

		ActionListener makeAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (user.getType() == 0) {
					Assignment assign = Assignments.get(AssignmentTable.getSelectedRow());
					detailView.setContext(DetailViewType.ASSIGNMENT, true, assign, null, null, null, null);
				} else {
					detailView.setContext(DetailViewType.ASSIGNMENT, true, null, null, null, null, null);
				}
				detailView.setVisible(true);
			}
		};
		String submitTitle = "";
		if (user.getType() == 0) {
			submitTitle = "레포트제출";
		} else if (user.getType() == 1) {
			submitTitle = "과제생성";
		}
		submitButton = new JButton(submitTitle); // 이름 설정 후 생성
		submitButton.setBounds(330, 50, 80, 20); // 위치와 사이즈 설정
		submitButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		submitButton.addActionListener(makeAction);
		contentPane.add(submitButton); // MenuAll 패널에 더함
	}

	@Override
	public void needsReloadData(DetailViewType type) {
		dataManager.openDB();
		Version version = dataManager.selectVersionDB(lecture.getLectureId());
		if (networkManager.syncNotification(lecture.getLectureId(), version.assignVersion)) {
			// DB에서 해당 강의의 과제를 가져와 저장
			Assignments = dataManager.selectAssignmentDB(lecture.getLectureId());
		}
		dataManager.closeDB();

		String[] columnNames = {"과제 번호","제목"}; // Column을 설명하기 위함
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
			tableModel.setValueAt(Assignment.getAssignId(), i, 0);
			tableModel.setValueAt(Assignment.title, i, 1);
		}
		AssignmentTable.setModel(tableModel);
	}
}
