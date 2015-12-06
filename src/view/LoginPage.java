package view;

import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginPage extends JFrame{//����ȭ��
	JLabel i;
	JLabel p;
	public JTextField IDField;			//ID�Է�ĭ
	public JPasswordField PWField;		//PW�Է�ĭ
	JButton LoginButton;			//�α��� ��ư
	
	public LoginPage(){
		
		setTitle("Mini E-class");
		setSize(350,170);
		setLocation(10,10);
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		i = new JLabel("Enter Username :");
		i.setLocation(10,10);
		i.setSize(i.getPreferredSize());
		this.add(i);
		
		IDField = new JTextField();
		IDField.setColumns(15);
		IDField.setLocation(150,10);
		IDField.setSize(IDField.getPreferredSize());
		IDField.setToolTipText("Input User ID");
		this.add(IDField);
		
		p = new JLabel("Enter Password :");
		p.setLocation(10, 40);
		p.setSize(p.getPreferredSize());
		this.add(p);
		
		PWField = new JPasswordField();
		PWField.setColumns(15);
		PWField.setLocation(150,40);
		PWField.setSize(PWField.getPreferredSize());
		PWField.setToolTipText("Input User ID");
		PWField.setActionCommand("login");
		this.add(PWField);
		
		LoginButton = new JButton("Log in");
		LoginButton.setLocation(150,80);
		LoginButton.setSize(LoginButton.getPreferredSize());
		this.add(LoginButton);
		
		LoginButton.setActionCommand("login");
		
		this.setVisible(true);
	}
	
	public void setActionListener(ActionListener e){
		PWField.addActionListener(e);
		LoginButton.addActionListener(e);
	}
}
