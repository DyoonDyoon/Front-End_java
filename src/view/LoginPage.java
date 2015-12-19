/**
 *  Created by JeongDongMin on 2015. 12. 05..
 */
package view;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class LoginPage extends JFrame{ // Jframe을 확장시킨 클래스 LoginPage 구현
	BufferedImage img = null; // 메인 로고 사진
	JLabel i; // Enter UserName:을 나타내는 라벨
	JLabel p; // Enter PassWord:을 나타내는 라벨
	public JTextField IDField;			// ID입력 필드
	public JPasswordField PWField;		// PW입력 필드
	JButton LoginButton;			// 로그인 버튼
	
	public LoginPage(){
		setTitle("Mini E-class"); // 제목 설정
		setSize(310,370); // 사이즈 설정
		setLocation(10,10); // 위치 설정
		setLayout(null); // Layout을 Absolute로 설정
		setResizable(false); // 크기 조정 비활성화
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X를 누르면 프로그램이 종료되도록 함
		
		//이미지를 불러오기 위해 사용하는 try catch문 
		try{
			img = ImageIO.read(new File("img/login.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"이미지 불러오기 실패!");
			System.exit(0);
		}
		MyPanel logo = new MyPanel(); // 이미지를 가지고 로고로 지정
        logo.setBounds(20, 20, 230, 150); // 크기와 위치 지정
        this.add(logo); // 메인 프레임에 추가
		
		
		i = new JLabel("Enter Username :"); // 라벨 이름 지정 후 생성
		i.setLocation(10,220+10); // 위치 지정
		i.setSize(i.getPreferredSize()); // 사이즈 지정
		this.add(i); // 메임 프레임에 추가
		
		IDField = new JTextField(); // ID필드 생성
		IDField.setColumns(12); // 12자를 입력할 수 있도록 크기 지정
		IDField.setLocation(150,220+10); // 위치 지정
		IDField.setSize(IDField.getPreferredSize()); // 사이즈 지정
		IDField.setToolTipText("Input User ID"); // 마우스를 올렸을 때 Tooltip하는 문자열 지정
		this.add(IDField); // 메인 프레임에 추가
		
		p = new JLabel("Enter Password :"); // 라벨 이름 지정 후 생성
		p.setLocation(10, 220+40); // 위치 지정
		p.setSize(p.getPreferredSize()); // 사이즈 지정
		this.add(p); // 메인 프레임에 추가
		
		PWField = new JPasswordField(); // PW필드 생성
		PWField.setColumns(12); // 12자를 입력할 수 있도록 크기 지정
		PWField.setLocation(150,220+40); // 위치 지정
		PWField.setSize(PWField.getPreferredSize()); // 사이즈 지정
		PWField.setToolTipText("Input User ID"); // 마우스를 올렸을 때 Tooltip하는 문자열 지정
		PWField.setActionCommand("login"); // 액션 커맨드를 login으로 지정
		this.add(PWField); // 메인 프레임에 추가
		
		LoginButton = new JButton("Log in"); // 로그인 버튼 생성
		LoginButton.setLocation(150,220+80); // 위치 지정
		LoginButton.setSize(LoginButton.getPreferredSize()); // 사이즈 지정
		LoginButton.setActionCommand("login"); // 액션 커멘드를login으로 지정
		this.add(LoginButton); // 메인 프레임에 추가
	}
	/**
	 * Main에서 구현한 ActionListener를 PW필드와 버튼에 추가
	 * @param e
	 */
	public void setActionListener(ActionListener e){
		PWField.addActionListener(e); // PW필드에서 엔터를 치면 인식하도록 함
		LoginButton.addActionListener(e); // 로그인 버튼을 클릭하면 인식하도록 함
	}
	
	// 로고를 불러와 메인 프레임에 추가하기 위해 사용하는 중첩 클래스
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(img, 20, 20, null);
        }
    }
}
