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
import model.Grade;
import model.Lecture;
import model.Submit;
import model.User;
import view.DetailView.DetailViewType;

public class ClassGrade extends JFrame implements ReloadListener{
	
	User user = null; // 학생 정보를 저장하는 Student 객체 선언
	DataManager dataManager = null;
	NetworkManager networkManager = null;
	
	JPanel contentPane; // 메인 패널 선언
	JTable GradeTable; // 성적 테이블 선언
	JScrollPane GradescrollPane; // 성적 테이블의 스크롤 선언
	private ArrayList<Grade> grades; // 성적 저장 리스트 선언
	private ArrayList<Submit> submits; // 성적 부여를 위한 리스트 선언
	private Lecture lecture; // 강의 정보 저장 객체 선언 
	private DefaultTableModel tableModel; // 테이블 defalt 모델 선언
	private DetailView detailView; // detailview 객체 선언
	private JButton giveButton; // 성적 부여 버튼 선언
	
	/**
	 * ClassGrade 객체 생성자
	 * @param user
	 * @param dataManager
	 * @param networkManager
	 * @param lecture
	 */
	public ClassGrade(User user, DataManager dataManager, NetworkManager networkManager, Lecture lecture){
		this.user = user; // 유저 정보 설정
		this.dataManager = dataManager; // 데이타 매니저 설정
		this.networkManager = networkManager; //  네트워크 매니저 설정
		this.lecture = lecture; // 강의 정보 설정
		this.detailView = new DetailView(networkManager, lecture, user, this); // detailview 설정
		
		String titleString = "성적 확인"; //학생일 경우 제목을 "성적 확인"으로 설정
		if (user.getType() == 1) {
			titleString = "제출 확인"; // 교수일 경우 제목을 "제출 확인"으로 설정
		}
		
		setTitle(titleString); // 객체의 제목 설정
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
		
		GradeTable = new JTable();
		GradeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		GradeTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		GradeTable.setBackground(SystemColor.control); // 생상 설정
		GradeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		needsReloadData(null);
		
		GradescrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		GradescrollPane.setBounds(10,10, 300, 370); // 위치와 크기 설정
		GradescrollPane.setViewportView(GradeTable);	//JTable 지정
		GradescrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		GradescrollPane.setBackground(SystemColor.control); // 배경 색 설정
		contentPane.add(GradescrollPane);
		
		if (user.getType() == 1) { // 교수일 경우 성적 부여를 위한 버튼을 구현
			ActionListener giveAction = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					//버튼을 누르면 성적을 부여할 수 있도록 함
					Submit submit = submits.get(GradeTable.getSelectedRow()); // 선택한 제출의 정보를 가져옴
					detailView.setContext(DetailViewType.GRADE, true, null, submit, null, null, null);
					detailView.setVisible(true);
				}
			};
			
			giveButton = new JButton("성적부여"); // 이름 설정 후 생성
			giveButton.setBounds(330, 20, 80, 20); // 위치와 사이즈 설정
			giveButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
			giveButton.addActionListener(giveAction);
			contentPane.add(giveButton); // MenuAll 패널에 더함
		}
	}

	@Override // 데이터를 가져올 때 실행되는 함수
	public void needsReloadData(DetailViewType type) {
		
		if (user.getType() == 1) {
			// 교수일 경우 제출 확인
			String[] submitColumnNames = {"과제 번호", "제출자", "파일"};
			
			submits = new ArrayList<Submit>();
			
			// 특정 과제에 대한 것이 아니기 때문에 -1을 줌
			if (networkManager.syncReport(lecture.getLectureId(), -1, null)) {
				dataManager.openDB();
				submits = dataManager.selectSubmitDB(lecture.getLectureId(), -1, null);
				dataManager.closeDB();
			} else {
				JOptionPane.showMessageDialog(null, "레포트를 받아올 수 없습니다!");
				return;
			}
			//테이블 모델을 설정함
			tableModel = new DefaultTableModel(submits.size(), submitColumnNames.length) {
				    @Override
				    public boolean isCellEditable(int row, int column) {
				       //all cells false
				       return false;
				    }
			};
			
			//가져온 정보를 통해 테이블을 구현
			for (int i = 0; i < submits.size(); ++i) {
				Submit submit = submits.get(i);
				tableModel.setValueAt(submit.getAssignId(), i, 0); // 과제 번호 설정
				tableModel.setValueAt(submit.getStudentId(), i, 1); // 학번 설정
				tableModel.setValueAt(submit.getFilePath(), i, 2); // 파일 경로 설정
			}
			tableModel.setColumnIdentifiers(submitColumnNames);
		} else {
			// 학생일 경우 점수확인
			String[] gradeColumnNames = {"과제 번호","점수"}; // Column을 설명하기 위함
			
			grades = new ArrayList<Grade>();
			
			if (networkManager.syncGrade(user.getId(), lecture.getLectureId())) {
				dataManager.openDB();
				// DB에서 해당 강의 성적을 가져와 저장
				grades = dataManager.selectGradeDB(lecture.getUserId(), lecture.getLectureId());
				dataManager.closeDB();
			} else {
				JOptionPane.showMessageDialog(null, "성적표를 받아올 수 없습니다!");
				return;
			}
			
			//가져온 정보를 통해 테이블 모델 설정
			tableModel = new DefaultTableModel(grades.size(), gradeColumnNames.length) {
				    @Override
				    public boolean isCellEditable(int row, int column) {
				       //all cells false
				       return false;
				    }
			};
			
			for (int i = 0; i < grades.size(); ++i) {
				Grade grade = grades.get(i);
				dataManager.openDB();
				// sumbitId로 assignId를 검색하기 위함
				Submit submit = dataManager.selectSubmit(grade.getSubmitId());
				dataManager.closeDB();
				tableModel.setValueAt(submit.getAssignId(), i, 0); // 과제 번호 설정
				tableModel.setValueAt(grade.getScore(), i, 1); // 받은 점수 설정
			}
			tableModel.setColumnIdentifiers(gradeColumnNames);
		}
		
		GradeTable.setModel(tableModel);

		TableColumnModel columnmodel = GradeTable.getColumnModel();
		if (user.getType() == 1) { // 교수일 때에는 "과제 번호","학번","파일 경로"
			columnmodel.getColumn(0).setPreferredWidth(60); 
			columnmodel.getColumn(1).setPreferredWidth(100);
			columnmodel.getColumn(2).setPreferredWidth(120); 
			columnmodel.getColumn(0).setResizable(false);
			columnmodel.getColumn(1).setResizable(false);
			columnmodel.getColumn(2).setResizable(false);
		} else { // 학생일 경우 "과제 번호", "성적"
			columnmodel.getColumn(0).setPreferredWidth(60); 
			columnmodel.getColumn(1).setPreferredWidth(230); 
			columnmodel.getColumn(0).setResizable(false);
			columnmodel.getColumn(1).setResizable(false);
		}
	}
}
