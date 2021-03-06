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
import model.Answer;
import model.Lecture;
import model.Question;
import model.User;
import view.DetailView.DetailViewType;

public class ClassQuestion extends JFrame implements ReloadListener{
	User user = null; // 학생 정보를 저장하는 Student 객체 선언
	DataManager dataManager = null; // 데이터 매니저 선언
	NetworkManager networkManager = null; // 네트워크 매니저 선언
	
	JPanel contentPane; // 메인 패널 선언
	JTable QuestionTable; // 질문 테이블 선언
	JScrollPane QuestionscrollPane; // 테이블의 스크롤 선언
	ArrayList<Question> Questions = new ArrayList<Question>(); // 질문을 저장할 리스트 선언
	
	private Lecture lecture; // 강의 정보를 저장할 객체 선언
	private DefaultTableModel tableModel; // default 테이블 모델 선언
	private JButton detailButton; // 질문 내용 확인 버튼 선언
	private JButton actionButton; // 질문 올리기 버튼 선언
	private JButton answerViewButton; // 답변 확인 버튼 선언
	
	private DetailView detailView; // detailview 객체 선언
	/**
	 * ClassQuestion 객체 생성자
	 * @param user
	 * @param dataManager
	 * @param networkManager
	 * @param lecture
	 */
	public ClassQuestion(User user, DataManager dataManager, NetworkManager networkManager, Lecture lecture){
		this.user = user; // 유저 정보 설정
		this.dataManager = dataManager; // 데이터 매니저 설정
		this.networkManager = networkManager; // 네트워크 매니저 설정  
		this.lecture = lecture; // 강의 정보 설정
		this.detailView = new DetailView(networkManager, lecture, user, this); //detailview 설정
		
		setTitle("질의 응답 확인"); // 객체의 제목 설정
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
		
		QuestionTable = new JTable(); // 질문 테이블 설정
		QuestionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		QuestionTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		QuestionTable.setBackground(SystemColor.control); // 생상 설정
		QuestionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		needsReloadData(null);
		
		QuestionscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		QuestionscrollPane.setBounds(10,10, 300, 370); // 위치와 크기 설정
		QuestionscrollPane.setViewportView(QuestionTable);	//JTable 지정
		QuestionscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		QuestionscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		contentPane.add(QuestionscrollPane);
		
		//질문 확인 actionlistener 생성 및 구현
		ActionListener readAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//질문 리스트에서 질문을 가져와 detailview를 구현
				Question question = Questions.get(QuestionTable.getSelectedRow());
				detailView.setContext(DetailViewType.QUESTION, false, null, null, question, null, null);
				detailView.setVisible(true);
			}
		};
		
		detailButton = new JButton("세부사항"); // 이름 설정 후 생성
		detailButton.setBounds(330, 20, 80, 20); // 위치와 사이즈 설정
		detailButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		detailButton.addActionListener(readAction); // actionlistener 설정
		contentPane.add(detailButton); // MenuAll 패널에 더함

		//교수일 경우 답변을 작성할 수 있도록 설정하고 학생일 경우 질문 할 수 있도록 설정
		ActionListener action = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (user.getType() != 0) {
					// 교수님용 액션
					Question question = Questions.get(QuestionTable.getSelectedRow());
					detailView.setContext(DetailViewType.ANSWER, true, null, null, question, null, null);					
				} else {
					// 학생용 액션
					detailView.setContext(DetailViewType.QUESTION, true, null, null, null, null, null);
				}
				detailView.setVisible(true);
			}
		};
		String actionTitle = "";
		if (user.getType() != 0) { // 교수일 경우 "답변 달기"로 버튼 제목 설정
			actionTitle = "답변 달기";
		}else { // 학생일 경우 "질문 하기"로 버튼 제목 설정
			actionTitle = "질문하기"; 
		}
		actionButton = new JButton(actionTitle); // 이름 설정 후 생성
		actionButton.setBounds(330, 50, 80, 20); // 위치와 사이즈 설정
		actionButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		actionButton.addActionListener(action); // actionlistener 설정
		contentPane.add(actionButton); // MenuAll 패널에 더함
		
		if (user.getType() == 0) { // 학생일 경우 답변 확인 구현
			//답변 확인을 위한 actionlistener 생성및 구현
			ActionListener answerViewAction = new ActionListener(){
				@Override // db에서 지정한 질문에 답변이 달려있는지 확인한 뒤 답변을 출력
				public void actionPerformed(ActionEvent e) {
					Question question = Questions.get(QuestionTable.getSelectedRow());
					if (networkManager.syncAnswer(question.getQuestionId())) {
						dataManager.openDB();
						//답변 객체를 생성(답변이 있을 경우)
						Answer answer = dataManager.selectAnswerDB(question.getQuestionId());
						dataManager.closeDB();
						//답변을 설정해줌
						detailView.setContext(DetailViewType.ANSWER, false, null, null, null, answer, null);
						detailView.setVisible(true); 
					} else {
						JOptionPane.showMessageDialog(null, "답변이 없습니다!");
					}
				}
			};
			answerViewButton = new JButton("답변확인"); // 이름 설정 후 생성
			answerViewButton.setBounds(330, 80, 80, 20); // 위치와 사이즈 설정
			answerViewButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
			answerViewButton.addActionListener(answerViewAction); // actionlistener 설정
			contentPane.add(answerViewButton); // MenuAll 패널에 더함
		}
	}

	@Override // 데이터를 불러올 때 사용하는 함수
	public void needsReloadData(DetailViewType type) {
		String userId = null;
		String lectureId = null;
		
		// 학생이면 학생 ID로만 질문 검색
		// 교수면 강의 ID로 질문 검색
		if (user.getType() == 0)
			userId = user.getId();
		lectureId = lecture.getLectureId();
		
		if (networkManager.syncQuestion(userId, lectureId)) {
			dataManager.openDB();
			// DB에서 해당 강의의 질의를 가져와 저장
			Questions = dataManager.selectQuestionDB(userId, lectureId);
			dataManager.closeDB();
		} else {
			JOptionPane.showMessageDialog(null, "질문을 받아올 수 없습니다!");
			return;
		}
		
		String[] columnNames = {"질문자","내용"}; // Column을 설명하기 위함
		
		//테이블 모델 생성
		tableModel = new DefaultTableModel(Questions.size(), columnNames.length) {
			    @Override
			    public boolean isCellEditable(int row, int column){
			       //all cells false
			       return false;
			    }
		};
		
		//테이블 모델의 행 지정
		tableModel.setColumnIdentifiers(columnNames);
		for (int i = 0; i < Questions.size(); ++i){
			//질문 리스트에서 질문을 가져와 테이블을 구현
			Question Question = Questions.get(i);
			tableModel.setValueAt(Question.getStudentId(), i, 0);
			tableModel.setValueAt(Question.content, i, 1);
		}
		
		QuestionTable.setModel(tableModel); // 테이블 모델로 테이블 구현
		TableColumnModel columnmodel = QuestionTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(100); // 가로 사이즈 지정
		columnmodel.getColumn(1).setPreferredWidth(200); 
		columnmodel.getColumn(0).setResizable(false); // 사이즈 조절 비활성화
		columnmodel.getColumn(1).setResizable(false);
	}
}
