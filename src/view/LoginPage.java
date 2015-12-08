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

public class LoginPage extends JFrame{//����ȭ��
	BufferedImage img = null;
	JLabel i;
	JLabel p;
	public JTextField IDField;			//ID�Է�ĭ
	public JPasswordField PWField;		//PW�Է�ĭ
	JButton LoginButton;			//�α��� ��ư
	
	public LoginPage(){
		setTitle("Mini E-class");
		setSize(310,370);
		setLocation(10,10);
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try{
			img = ImageIO.read(new File("img/login.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"이미지 불러오기 실패!");
			System.exit(0);
		}
		MyPanel logo = new MyPanel();
        logo.setBounds(20, 20, 230, 150);
        this.add(logo);
		
		
		i = new JLabel("Enter Username :");
		i.setLocation(10,220+10);
		i.setSize(i.getPreferredSize());
		this.add(i);
		
		IDField = new JTextField();
		IDField.setColumns(12);
		IDField.setLocation(150,220+10);
		IDField.setSize(IDField.getPreferredSize());
		IDField.setToolTipText("Input User ID");
		this.add(IDField);
		
		p = new JLabel("Enter Password :");
		p.setLocation(10, 220+40);
		p.setSize(p.getPreferredSize());
		this.add(p);
		
		PWField = new JPasswordField();
		PWField.setColumns(12);
		PWField.setLocation(150,220+40);
		PWField.setSize(PWField.getPreferredSize());
		PWField.setToolTipText("Input User ID");
		PWField.setActionCommand("login");
		this.add(PWField);
		
		LoginButton = new JButton("Log in");
		LoginButton.setLocation(150,220+80);
		LoginButton.setSize(LoginButton.getPreferredSize());
		this.add(LoginButton);
		
		LoginButton.setActionCommand("login");
		
		this.setVisible(true);
	}
	
	public void setActionListener(ActionListener e){
		PWField.addActionListener(e);
		LoginButton.addActionListener(e);
	}
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(img, 20, 20, null);
        }
    }
}
