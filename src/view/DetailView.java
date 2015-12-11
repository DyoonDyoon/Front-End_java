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
	public enum DetailViewType {
		ASSIGNMENT,
		QUESTION,
		ANSWER,
		NOTIFICATION,
		GRADE
	};
	
	private User user;
	
	private DetailViewType type;
	private Assignment assignment;
	private Submit submit;
	private Answer answer;
	private Question question;
	private Notification notification;
	
	private JPanel contentPane;
	private JTextArea titleArea;
	private JTextArea contentArea;
	private JButton customButton;
	private JFileChooser fileChooser;
	private JButton uploadButton;
	
	private boolean writeMode;
	
	private NetworkManager manager;
	private Lecture lecture;
	private ReloadListener delegate;
	
	public DetailView(NetworkManager networkManager, Lecture lecture, User user, ReloadListener delegate) {
		writeMode = false;
		
		type = null;
		assignment = null;
		submit = null;
		answer = null;
		question = null;
		notification = null;
		
		manager = networkManager;
		this.lecture = lecture;
		this.user = user;
		this.delegate = delegate;
		
		this.setSize(320, 500);
		this.setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		titleArea = new JTextArea();
		titleArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		titleArea.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		titleArea.setBackground(SystemColor.control); // 색상 설정
		titleArea.setAutoscrolls(true);
		contentPane.add(titleArea);
		
		contentArea = new JTextArea();
		contentArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		contentArea.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		contentArea.setBackground(SystemColor.control); // 색상 설정
		contentArea.setLineWrap(true);
		contentArea.setAutoscrolls(true);
		contentPane.add(contentArea);
		
		customButton = new JButton("Custom");
		customButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		customButton.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		contentPane.add(customButton);
		
		uploadButton = new JButton("열기");
		uploadButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		uploadButton.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		ActionListener e = null;
		fileChooser = new JFileChooser();
		uploadButton.addActionListener(e = new ActionListener(){
			@Override
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
		
	}
	
	public void setContext(DetailViewType type, boolean writeMode,
			Assignment assign, Submit submit, Question question, Answer answer, Notification noti) {
		this.writeMode = writeMode;
		this.type = type;
		this.assignment = assign;
		this.submit = submit;
		this.answer = answer;
		this.question = question;
		this.notification = noti;
		reloadData();
	}
	
	private void reloadData() {
		// Layout 재설정
		titleArea.setBounds(10, 10, 300, 20);
		contentArea.setBounds(10, 40, 300, 330);
		customButton.setBounds(110, 390, 100, 30);
		uploadButton.setVisible(false);
		contentArea.setVisible(true);
		
		for (ActionListener action : customButton.getActionListeners()) {
			customButton.removeActionListener(action);
		}
		
		if (writeMode) {
			setSize(this.getWidth(), 500);
			customButton.setText("제출");
			customButton.addActionListener(actionForType(type));
			titleArea.setText("");
			contentArea.setText("");
		} else {
			setSize(this.getWidth(), 400);
		}
		
		titleArea.setEditable(writeMode);
		contentArea.setEditable(writeMode);
		
		switch(this.type) {
		case ASSIGNMENT:
			if(!writeMode) {
				titleArea.setText(this.assignment.title);
				contentArea.setText(this.assignment.description);
			} else {
				if (user.getType() == 0) {
					// 레포트 제출하는 프레임은 파일 경로만 입력받음
					contentArea.setVisible(false);
					customButton.setBounds(190, 40, 100, 20);
					uploadButton.setBounds(30, 40, 100, 20);
					uploadButton.setVisible(true);
					setSize(this.getWidth(), 100);
				}
			}
			break;
		case QUESTION:
			if(!writeMode) {
				titleArea.setText(this.question.getStudentId());
				contentArea.setText(this.question.content);
			} else {
				titleArea.setEditable(false);
				titleArea.setText(this.lecture.getLectureId());
			}
			break;
		case ANSWER:
			if(!writeMode) {
				titleArea.setText(String.valueOf(this.answer.getQuestionId()));
				contentArea.setText(this.answer.content);
			} else {
				titleArea.setEditable(false);
				titleArea.setText(question.getStudentId() + "의 질문");
			}
			break;
		case NOTIFICATION:
			if(!writeMode) {
				titleArea.setText(this.notification.title);
				contentArea.setText(this.notification.description);
			}
			break;
		case GRADE:
			contentArea.setVisible(false);
			customButton.setBounds(110, 40, 100, 20);
			setSize(this.getWidth(), 85);
			break;
		default:
			break;
		}
		
		customButton.setVisible(writeMode);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	private ActionListener actionForType(DetailViewType type) {
		ActionListener action = null;
		switch(type) {
		case ASSIGNMENT:
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (user.getType() == 0) {
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
					} else if (user.getType() == 1) {
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
		case QUESTION:
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
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
		case ANSWER:
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (manager.makeAnswer(question.getQuestionId(), contentArea.getText())) {
						setVisible(false);
						delegate.needsReloadData(type);
					} else {
						JOptionPane.showMessageDialog(null, "답변 업로드에 실패했습니다!");
					}
				}
			};
			break;
		case GRADE:
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
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
		case NOTIFICATION:
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
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