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
import model.Lecture;
import model.Notification;
import model.User;
import model.Version;
import view.DetailView.DetailViewType;

public class ClassNotification extends JFrame implements ReloadListener{
	User user; //유저 정보를 저장하는 User 객체 선언
	DataManager dataManager; // 데이터 매니저 객체 선언
	NetworkManager networkManager; // 네트워크 매니저 객체 선언
	Lecture lecture; // 강의 정보 저장 객체 선언
	
	JPanel contentPane; // 메인 패널 선언
	JTable NotificationTable; // 공지 사항 테이블 선언
	JScrollPane NotificationscrollPane; // 테이블에 있는 스크롤 선언
	private JButton moreButton; // 세부 사항 버튼 선언
	private JButton makeButton; // 공지 올리기 버튼 선언 
	private DetailView detailView; // detailview 객체 선언
	private ArrayList<Notification> notifications; // 공지 저장 리스트 선언
	private DefaultTableModel tableModel; // default 테이블 모델 선언
	
	public ClassNotification(User user, DataManager dataManager, NetworkManager networkManager,Lecture lectureNow){
		this.dataManager = dataManager; // 데이터 매니저 설정
		this.user = user; // 유저 설정
		this.networkManager = networkManager; // 네트워크 매니저 설정
		this.lecture = lectureNow; // 현재 선택한 강의 설정
		this.detailView = new DetailView(networkManager, lectureNow, user, this); // detailview 구현
		this.detailView.setVisible(false);
		
		setTitle("공지 확인"); // 객체의 제목 설정
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
		
		NotificationTable = new JTable(); // 공지 테이블 생성
		NotificationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 크기가 자동적으로 바뀌지 않도록함
		NotificationTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12)); // 폰트와 폰트 크기 설정
		NotificationTable.setBackground(SystemColor.control); // 생상 설정
		NotificationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택받도록 함
		
		needsReloadData(null);
		
		NotificationscrollPane = new JScrollPane(); // 스크롤바 설정을 위함
		NotificationscrollPane.setBounds(10,10, 300, 370); // 위치와 크기 설정
		NotificationscrollPane.setViewportView(NotificationTable);	//JTable 지정
		NotificationscrollPane.setBorder(new LineBorder(new Color(0, 0, 0))); // 가장자리 설정
		NotificationscrollPane.setBackground(SystemColor.control); // 배경 색 설정
		contentPane.add(NotificationscrollPane);
		 
		//ActionListener 생성 및 구현
		ActionListener readAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Notification noti = notifications.get(NotificationTable.getSelectedRow()); // 열 설정
			    // detailview 객체에 정보 설정
				detailView.setContext(DetailViewType.NOTIFICATION, false, null, null, null, null, noti);
				detailView.setVisible(true);
			}
		};
		
		moreButton = new JButton("세부사항"); // 이름 설정 후 생성
		moreButton.setBounds(330, 20, 80, 20); // 위치와 사이즈 설정
		moreButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
		moreButton.addActionListener(readAction); // actionlistener 설정
		contentPane.add(moreButton); // MenuAll 패널에 더함
		
		if (user.getType() != 0) { // 교수일 경우 공지 작성 버튼을 구현하도록 함
			ActionListener makeAction = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					//교수일 경우 detailview를 다르게 설정
					detailView.setContext(DetailViewType.NOTIFICATION, true, null, null, null, null, null);
					detailView.setVisible(true);
				}
			};
			makeButton = new JButton("공지작성"); // 이름 설정 후 생성
			makeButton.setBounds(330, 50, 80, 20); // 위치와 사이즈 설정
			makeButton.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트와 크기 설정
			makeButton.addActionListener(makeAction); // actionlistener 설정
			contentPane.add(makeButton); // MenuAll 패널에 더함
		}
	}

	@Override // 데이터를 가져올 때 실행할 함수 
	public void needsReloadData(DetailViewType type) {
		notifications = new ArrayList<Notification>();
		dataManager.openDB();
		Version version = dataManager.selectVersionDB(lecture.getLectureId());
		if (networkManager.syncNotification(lecture.getLectureId(), version.notiVersion)) {
			// DB에서 해당 강의의 공지사항을 가져와 저장
			notifications = dataManager.selectNotificationDB(lecture.getLectureId());
		} else {
			JOptionPane.showMessageDialog(null, "공지사항을 받아올 수 없습니다!");
			dataManager.closeDB();
			return;
		}
		dataManager.closeDB();
		
		String[] columnNames = {"공지 번호", "제목"}; // Column을 설명하기 위함
		tableModel = new DefaultTableModel(notifications.size(), columnNames.length)
		{
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
				}
		};
		
		//테이블의 열의 index를 설정
		tableModel.setColumnIdentifiers(columnNames);
		for (int i = 0; i < notifications.size(); ++i) {
			Notification noti = notifications.get(i);
			tableModel.setValueAt(noti.getNotificationId(), i, 0); //공지 id 설정
			tableModel.setValueAt(noti.title, i, 1); // 공지 이름 설정
		}

		NotificationTable.setModel(tableModel); // default 모ㅗ델로 테이블 설정
		TableColumnModel columnmodel = NotificationTable.getColumnModel();
		columnmodel.getColumn(0).setPreferredWidth(50); // 가로 길이 지정
		columnmodel.getColumn(1).setPreferredWidth(230); 
		columnmodel.getColumn(0).setResizable(false); // 사이즈 수정 비활성화
		columnmodel.getColumn(1).setResizable(false);
	}
}
