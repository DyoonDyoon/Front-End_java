package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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
import model.Grade;
import model.Lecture;
import model.Notification;
import model.Question;
import model.User;

public class DetailView extends JFrame {
	public enum DetailViewType {
		ASSIGNMENT,
		SUBMIT,
		QUESTION,
		ANSWER,
		NOTIFICATION,
		GRADE
	};
	
	private User user;
	
	private DetailViewType type;
	private Assignment assignment;
	private Grade grade;
	private Answer answer;
	private Question question;
	private Notification notification;
	
	private JPanel contentPane;
	private JTextArea titleArea;
	private JTextArea contentArea;
	private JButton customButton;
	
	private boolean writeMode;
	
	private NetworkManager manager;
	private Lecture lecture;
	private ReloadListener delegate;
	
	public DetailView(NetworkManager networkManager, Lecture lecture, User user, ReloadListener delegate) {
		user = null;
		
		writeMode = false;
		
		type = null;
		assignment = null;
		grade = null;
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
		titleArea.setBounds(10, 10, 300, 20);
		titleArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		titleArea.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		titleArea.setBackground(SystemColor.control); // 색상 설정
		titleArea.setAutoscrolls(true);
		contentPane.add(titleArea);
		
		contentArea = new JTextArea();
		contentArea.setBounds(10, 40, 300, 330);
		contentArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		contentArea.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		contentArea.setBackground(SystemColor.control); // 색상 설정
		contentArea.setLineWrap(true);
		contentArea.setAutoscrolls(true);
		contentPane.add(contentArea);
		
		customButton = new JButton("Custom");
		customButton.setBounds(130, 400, 100, 30);
		customButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		customButton.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		contentPane.add(customButton);
	}
	
	public void setContext(DetailViewType type, boolean writeMode,
			Assignment assign, Grade grade, Question question, Answer answer, Notification noti) {
		this.writeMode = writeMode;
		this.type = type;
		this.assignment = assign;
		this.grade = grade;
		this.answer = answer;
		this.question = question;
		this.notification = noti;
		reloadData();
	}
	
	private void reloadData() {
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
		
		switch(this.type) {
		case ASSIGNMENT:
			if(!writeMode) {
				titleArea.setText(this.assignment.title);
				contentArea.setText(this.assignment.description);
			}
			break;
		case QUESTION:
			if(!writeMode) {
				titleArea.setText(this.question.getStudentId());
				contentArea.setText(this.question.content);
			}
			break;
		case ANSWER:
			if(!writeMode) {
				titleArea.setText(String.valueOf(this.answer.getQuestionId()));
				contentArea.setText(this.answer.content);
			}
			break;
		case NOTIFICATION:
			if(!writeMode) {
				titleArea.setText(this.notification.title);
				contentArea.setText(this.notification.description);
			}
			break;
		case GRADE:
			break;
		case SUBMIT:
			break;
		default:
			break;
		}

		titleArea.setEditable(writeMode);
		contentArea.setEditable(writeMode);
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
			};
			break;
		case SUBMIT:
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (manager.postAssignment(lecture.getLectureId(),
							titleArea.getText(),
							contentArea.getText(),
							null, null, null)) {
						setVisible(false);
						delegate.needsReloadData(type);
					} else {
						JOptionPane.showMessageDialog(null, "레포트 제출에 실패했습니다!");
					}
				}
			};
			break;
		case QUESTION:
			action = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (manager.postAssignment(lecture.getLectureId(),
							titleArea.getText(),
							contentArea.getText(),
							null, null, null)) {
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
					if (manager.postAssignment(lecture.getLectureId(),
							titleArea.getText(),
							contentArea.getText(),
							null, null, null)) {
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
					/*
					if (manager.giveGrade(lecture.getLectureId(), submitId, stuId, score)) {
						setVisible(false);
						delegate.needsReloadData(type);
					} else {
						
					}
					*/
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