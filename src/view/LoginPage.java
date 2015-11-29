package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginPage{
	JFrame jframe;				//메인화면
	JLabel i;
	JLabel p;
	public JTextField IDField;			//ID입력칸
	public JPasswordField PWField;		//PW입력칸
	JButton LoginButton;			//로그인 버튼
	
	public LoginPage(ActionListener actionListener){
		
		jframe = new JFrame("Mini E-class");
		jframe.setSize(350,170);
		jframe.setLocation(300,300);
		jframe.setLayout(null);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		i = new JLabel("Enter Username :");
		i.setLocation(10,10);
		i.setSize(i.getPreferredSize());
		jframe.add(i);
		
		IDField = new JTextField();
		IDField.setColumns(15);
		IDField.setLocation(150,10);
		IDField.setSize(IDField.getPreferredSize());
		IDField.setToolTipText("Input User ID");
		jframe.add(IDField);
		
		p = new JLabel("Enter Password :");
		p.setLocation(10, 40);
		p.setSize(p.getPreferredSize());
		jframe.add(p);
		
		PWField = new JPasswordField();
		PWField.setColumns(15);
		PWField.setLocation(150,40);
		PWField.setSize(PWField.getPreferredSize());
		PWField.setToolTipText("Input User ID");
		PWField.addActionListener(actionListener);
		PWField.setActionCommand("login");
		jframe.add(PWField);
		
		LoginButton = new JButton("Log in");
		LoginButton.setLocation(150,80);
		LoginButton.setSize(LoginButton.getPreferredSize());
		jframe.add(LoginButton);
		
		LoginButton.addActionListener(actionListener);
		LoginButton.setActionCommand("login");
		
		jframe.setVisible(true);
	}
}
