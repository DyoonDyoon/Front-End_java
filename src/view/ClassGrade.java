package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.ArrayList;

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
import model.Grade;
import model.Lecture;
import model.Submit;
import model.User;
import view.DetailView.DetailViewType;

public class ClassGrade extends JFrame implements ReloadListener{
	
	User user = null; // 학생 정보를 저장하는 Student 객체 선언
	DataManager dataManager = null;
	NetworkManager networkManager = null;
	
	JPanel contentPane;
	JTable GradeTable;
	JScrollPane GradescrollPane;
	ArrayList<Grade> grades = new ArrayList<Grade>();
	ArrayList<Submit> submits = new ArrayList<Submit>();
	
	public ClassGrade(User user, DataManager dataManager, NetworkManager networkManager, Lecture lecture){
		setTitle("성적 확인"); // 객체의 제목 설정
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
		
		String[] columnNames = {"과제 번호","점수"}; // Column을 설명하기 위함
		
		dataManager.openDB();
		// DB에서 해당 강의 성적을 가져와 저장
		grades = dataManager.selectGradeDB(lecture.getUserId(),lecture.getLectureId());
		dataManager.closeDB();
		DefaultTableModel defaulttablemodel = new DefaultTableModel(grades.size(), columnNames.length)
			{
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		
		defaulttablemodel.setColumnIdentifiers(columnNames);
		for (int i = 0; i < grades.size(); ++i) {
			Grade grade = grades.get(i);
			dataManager.openDB();
			// sumbitId로 assignId를 검색하기 위함
			ArrayList<Submit> submit = dataManager.selectSubmitDB(lecture.getLectureId(), grade.getSubmitId() ,lecture.getUserId());
			dataManager.closeDB();
			defaulttablemodel.setValueAt(submit.get(0).getAssignId(), i, 0);
			defaulttablemodel.setValueAt(grade.getScore(), i, 1);
		}
		
		GradeTable = new JTable(defaulttablemodel);
		GradeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		GradeTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		GradeTable.setBackground(SystemColor.control); // 생상 설정
		GradeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		TableColumnModel columnmodel = GradeTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(200); 
		columnmodel.getColumn(1).setPreferredWidth(100); 
		columnmodel.getColumn(0).setResizable(false);
		columnmodel.getColumn(1).setResizable(false);
		
		GradescrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		GradescrollPane.setBounds(10,10, 300, 370); // 위치와 크기 설정
		GradescrollPane.setViewportView(GradeTable);	//JTable 지정
		GradescrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		GradescrollPane.setBackground(SystemColor.control); // 배경 색 설정
		contentPane.add(GradescrollPane);
	}

	@Override
	public void needsReloadData(DetailViewType type) {
		
	}
}
