/**
 *  Created by JeongDongMin on 2015. 12. 18..
 */
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.NetworkManager;
import controller.ReloadListener;
import model.Answer;
import model.Assignment;
import model.Lecture;
import model.Notification;
import model.Question;
import model.Submit;
import model.User;

public class DetailView extends JFrame {
	// 타입에 따라 출력할 정보를 다르게 하기 위함
	public enum DetailViewType {
		ASSIGNMENT, // 과제일 경우
		QUESTION, // 질문일 경우
		ANSWER, // 답변일 경우
		NOTIFICATION, // 공지일 경우
		GRADE // 성적일 경우
	};
	
	private User user; // 유저 객체 선언
	
	private DetailViewType type; // detailview의 type 선언 
	private Assignment assignment; // 과제 선언
	private Submit submit; // 과제 제출 선언
	private Answer answer; // 답변 선언
	private Question question; // 질문 선언
	private Notification notification; // 공지 선언
	
	private JPanel contentPane; // 메인 패널 선언
	private JTextArea titleArea; // 제목 textarea 선언
	private JTextArea contentArea; // 내용 textarea 선언
	private JButton customButton; // 추가 기능 구현 버튼
	private JFileChooser fileChooser; // 파일 업로드를 쉽게 하기 위해 사용하는 filechooser 선언
	private JButton uploadButton; // 업로드 버튼 선언
	private JButton deleteButton; // 삭제 버튼 선언
	
	private boolean writeMode; // 쓰기 형식인지 판단하기 위한 boolean형 변수 선언
	
	private NetworkManager manager; // 네트워크 매니저 선언
	private Lecture lecture; // 강의 정보 객체 선언
	private ReloadListener delegate; // 변경 사항 인식을 위한 객체 선언
	
	public DetailView(NetworkManager networkManager, Lecture lecture, User user, ReloadListener delegate) {
		writeMode = false; // 쓰기 모드를 false로 지정
		
		//모든 변수의 초기값을 null로 선언
		type = null;
		assignment = null;
		submit = null;
		answer = null;
		question = null;
		notification = null;
		
		manager = networkManager; // 네트워크 매니저 설정
		this.lecture = lecture; // 강의 설정
		this.user = user; // 유저 설정
		this.delegate = delegate; // reloadlistener 설정
		
		this.setSize(320, 500); // 사이즈 설정
		this.setResizable(false); // 사이즈 조절 비활성화
		
		contentPane = new JPanel(); // 메인 패널 생성
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // 메인 패널 가장자리 구현
		setContentPane(contentPane); // 메인 패널로 지정
		contentPane.setLayout(null); // 레이아웃을 Absolute로 설정
		
		titleArea = new JTextArea(); // 제목 textarea 생성
		titleArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		titleArea.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		titleArea.setBackground(SystemColor.control); // 색상 설정
		titleArea.setAutoscrolls(true); // 자동 스크롤 활성화
		contentPane.add(titleArea);
		
		contentArea = new JTextArea(); // 내용 textarea 생성
		contentArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		contentArea.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		contentArea.setBackground(SystemColor.control); // 색상 설정
		contentArea.setLineWrap(true); // 자동 줄 바뀜 활성화
		contentArea.setAutoscrolls(true); // 자동 스크롤 활성화
		contentPane.add(contentArea);
		
		customButton = new JButton("Custom"); // 커스텀 버튼 생성
		customButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		customButton.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		contentPane.add(customButton);
		
		uploadButton = new JButton("열기"); // 업로드 버튼 생성
		uploadButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		uploadButton.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		ActionListener e = null; // Actionlistener 생성
		fileChooser = new JFileChooser(); // filechooser 생성
		uploadButton.addActionListener(e = new ActionListener(){
			@Override // 버튼을 누르면 filechooser가 실행되어 파일을 불러올 수 있도록 함
		    public void actionPerformed(ActionEvent e) 
		    {
		        if(e.getSource() == uploadButton)
		        {
		            int returnVal = fileChooser.showOpenDialog(contentPane);
		            if( returnVal == JFileChooser.APPROVE_OPTION)
		            {
		                //열기 버튼을 누르면
		                File file = fileChooser.getSelectedFile();
		                titleArea.setText(file.toString());
		            }
		            else
		            {
		                //취소 버튼을 누르면
		            	titleArea.setText("");
		            }
		        }

		}});
		contentPane.add(uploadButton);
		
		deleteButton = new JButton("삭제"); // 삭제 버튼 생성
		deleteButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		deleteButton.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		deleteButton.addActionListener(getDeleteActionListener()); // 삭제 액션 리스너 설정
		deleteButton.setBounds(110, 390, 100, 30); // 위치와 크기 설정
		contentPane.add(deleteButton);  
	}
	/**
	 * 입력받은 매개변수를 통해 객체 내부의 변수들을 지정하도록 함
	 * @param type
	 * @param writeMode
	 * @param assign
	 * @param submit
	 * @param question
	 * @param answer
	 * @param noti
	 */
	public void setContext(DetailViewType type, boolean writeMode,
			Assignment assign, Submit submit, Question question, Answer answer, Notification noti) {
		this.writeMode = writeMode; // 쓰기 모드 설정
		this.type = type; // detailview의 타입 설정
		this.assignment = assign; // 과제 설정
		this.submit = submit; // 제출 설정
		this.answer = answer; // 답변 설정
		this.question = question; // 질문 설정
		this.notification = noti; // 공지 설정
		reloadData(); // 레이아웃을 재설정하도록 함
	}
	/**
	 * 메인 프레임 내부의 요소들을 재설정하도록함
	 */
	private void reloadData() {
		// Layout 재설정
		titleArea.setBounds(10, 10, 300, 20);
		contentArea.setBounds(10, 40, 300, 330);
		customButton.setBounds(110, 390, 100, 30);
		uploadButton.setVisible(false);
		deleteButton.setVisible(false);
		contentArea.setVisible(true);
		
		//커스텀 버튼의 Actionlistener를 모두 제거함
		for (ActionListener action : customButton.getActionListeners()) {
			customButton.removeActionListener(action);
		}
		
		//쓰기 모드일 경우
		if (writeMode) {
			setSize(this.getWidth(), 500); // 프레임의 크기를 새로 지정
			customButton.setText("제출"); // 커스텀 버튼의 제목을 "제출"로 변경
			customButton.addActionListener(actionForType(type)); // 타입에 맞는 actionlistener 설정
			titleArea.setText(""); // 제목 textarea 텍스트 설정
			contentArea.setText(""); // 내용 textarea 텍스트 설정
		} else { // 아닐 경우
			setSize(this.getWidth(), 400); // 프레임의 크기를 새로 지정
		}
		
		//TextArea가 쓰기모드일 경우 텍스트를 입력할 수 있도록하고 아닐 경우 입력할 수 없도록 함
		titleArea.setEditable(writeMode);
		contentArea.setEditable(writeMode);
		
		switch(this.type) { // 타입을 통해 다른 행동을 하도록 함
		case ASSIGNMENT: // 과제일 경우
			if(!writeMode) { // 쓰기 모드가 아닐 때
				titleArea.setText(this.assignment.title); // 지정한 과제의 제목을 textarea에 출력
				contentArea.setText(this.assignment.description); // 지정한 과제의 내용을 textarea에 출력
				if (user.getType() == 1) { // 교수일 경우
					setSize(this.getWidth(), 500); // 사이즈를 조정
					deleteButton.setVisible(true); // 삭제 버튼을 추가함
				}
			} else { // 쓰기 모드일 경우
				if (user.getType() == 0) { // 학생일 경우
					// 레포트 제출하는 프레임은 파일 경로만 입력받음
					contentArea.setVisible(false); // 제목 textarea를 삭제
					customButton.setBounds(190, 40, 100, 20); // 커스텀 버튼 위치 지정 ("취소"버튼임)
					uploadButton.setBounds(30, 40, 100, 20); // 제출 버튼 위치 지정
					uploadButton.setVisible(true); // 추가함
					setSize(this.getWidth(), 100); // 사이즈를 조정함
				}
			}
			break;
		case QUESTION: // 질문일 경우
			if(!writeMode) { // 쓰기 모드가 아닐 경우
				titleArea.setText(this.question.getStudentId()); // 지정한 질문의 제목을 textarea에 출력
				contentArea.setText(this.question.content); // 지정한 질문의 내용을 textarea에 출력
			} else { // 쓰기 모드일 경우
				titleArea.setEditable(false); // 제목 수정 비활성화
				titleArea.setText(this.lecture.getLectureId()); // 제목을 강의 번호로 지정함
			}
			break;
		case ANSWER: // 답변일 경우
			if(!writeMode) { // 쓰기 모드가 아닐 경우
				titleArea.setText(String.valueOf(this.answer.getQuestionId())); // 지정한 답변의 제목을 textarea에 출력
				contentArea.setText(this.answer.content); // 지정한 답변의 내용을 textarea에 출력
			} else { // 쓰기 모드일 경우
				titleArea.setEditable(false); // 제목 수정 비활성화
				titleArea.setText(question.getStudentId() + "의 질문"); // 제목을 "(학번)의 질문"으로 고정
			}
			break;
		case NOTIFICATION: // 공지일 경우
			if(!writeMode) { // 쓰기 모드가 아닐 경우
				titleArea.setText(this.notification.title); // 지정한 공지의 제목을 textarea에 출력
				contentArea.setText(this.notification.description); // 지정한 공지의 내용을 textarea에 출력
				if (user.getType() == 1) { // 교수일 경우
					setSize(this.getWidth(), 500); // 사이즈를 조정하고
					deleteButton.setVisible(true); // 삭제 버튼을 추가함
				}
			}
			break;
		case GRADE: // 성적 확인일 경우
			contentArea.setVisible(false); // 내용 textarea를 삭제함
			customButton.setBounds(110, 40, 100, 20); // 커스텀 버튼의 위치를 지정함
			setSize(this.getWidth(), 85); // 창 크기를 조정
			break;
		default:
			break;
		}
		
		customButton.setVisible(writeMode); // 쓰기 모드일 경우 커스텀 버튼을 보이도록 함
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	/**
	 * 삭제 버튼에 사용할 Actionlistener를 생성하는 함수
	 * 과제일 경우 매니저를 통해 과제를 삭제하고 공지일 경우 매니저를 통해 공지를 삭제함
	 * @return
	 */
	private ActionListener getDeleteActionListener() {
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (type == DetailViewType.ASSIGNMENT) { // 과제일 경우
					// 과제 삭제
					if (manager.cancelAssignment(lecture.getLectureId(), assignment.getAssignId())) {
						setVisible(false);
						delegate.needsReloadData(type);
					} else {
						JOptionPane.showMessageDialog(null, "과제 삭제에 실패했습니다!");
					}
				} else if (type == DetailViewType.NOTIFICATION) { // 공지일 경우
					// 공지 삭제
					if (manager.cancelNotification(lecture.getLectureId(), notification.getNotificationId())) {
						setVisible(false);
						delegate.needsReloadData(type);
					} else {
						JOptionPane.showMessageDialog(null, "공지 삭제에 실패했습니다!");
					}
				}
			}
		};
	}
	/**
	 * detailview의 타입에 따라 Actionlistener를 생성하고 구현하는 함수
	 * @param type
	 * @return
	 */
	private ActionListener actionForType(DetailViewType type) {
		ActionListener action = null;
		switch(type) { // 타입에 따라 실행되는 구문이 다르도록 함
		case ASSIGNMENT: // 과제일 경우
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (user.getType() == 0) { // 학생일 경우
						// 레포트 제출
						if (manager.submitReport(lecture.getLectureId(),
								assignment.getAssignId(),
								user.getId(),
								titleArea.getText())) {
							setVisible(false);
							delegate.needsReloadData(type);
						} else {
							JOptionPane.showMessageDialog(null, "레포트 제출에 실패했습니다!");
						}
					} else if (user.getType() == 1) { // 교수일 경우
						// 과제 생성
						if (manager.postAssignment(lecture.getLectureId(),
								titleArea.getText(),
								contentArea.getText(),
								null, null, null)) {
							setVisible(false);
							delegate.needsReloadData(type);
						} else {
							JOptionPane.showMessageDialog(null, "과제 생성에 실패했습니다!");
						}
					}
				}
			};
			break;
		case QUESTION: // 질문일 경우
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					//질문을 생성하도록 함
					if (manager.makeQuestion(lecture.getLectureId(),
							user.getId(), contentArea.getText())) {
						setVisible(false);
						delegate.needsReloadData(type);
					} else {
						JOptionPane.showMessageDialog(null, "질문 업로드에 실패했습니다!");
					}
				}
			};
			break;
		case ANSWER: // 답변일 경우
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// 답변을 생성하도록 함
					if (manager.makeAnswer(question.getQuestionId(), contentArea.getText())) {
						setVisible(false);
						delegate.needsReloadData(type);
					} else {
						JOptionPane.showMessageDialog(null, "답변 업로드에 실패했습니다!");
					}
				}
			};
			break;
		case GRADE: // 성적 부분일 경우
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// 성적을 부여하도록 함
					if (manager.giveGrade(lecture.getLectureId(),
							submit.getSubmitId(), submit.getStudentId(), Double.parseDouble(titleArea.getText()))) {
						setVisible(false);
						delegate.needsReloadData(type);
					} else {
						JOptionPane.showMessageDialog(null, "성적 부여에 실패했습니다!");
					}
				}
			};
			break;
		case NOTIFICATION: // 공지일 경우
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// 공지 사항을 올리도록 함
					if (manager.postNotification(lecture.getLectureId(),
							titleArea.getText(),
							contentArea.getText())) {
						setVisible(false);
						delegate.needsReloadData(type);
					} else {
						JOptionPane.showMessageDialog(null, "공지사항 업로드에 실패했습니다!");
					}
				}
			};
			break;
		default:
			return null;
		}
		return action;
	}
}