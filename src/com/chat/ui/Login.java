package com.chat.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import com.chat.utils.ConnectTo;

public class Login extends JFrame implements ActionListener {

	static String fmno;
	static ResultSet re;
	Box baseBox, boxV1, boxV2, boxV3, title;
	JLabel label, label1, labe12;
	JButton button1, button2;
	JPasswordField Password;
	JTextField text1;

	public Login() {
		setLayout(new java.awt.FlowLayout());
		init();
		setBounds(750, 200, 300, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void init() {
		title = Box.createHorizontalBox();
		label = new JLabel("YouChat");
		label.setFont(new Font("楷体", 0, 25));
		title.add(label);

		boxV1 = Box.createHorizontalBox();
		label1 = new JLabel("帐号");
		boxV1.add(label1);
		boxV1.add(Box.createHorizontalStrut(20));
		text1 = new JTextField(16);
		boxV1.add(text1);

		boxV2 = Box.createHorizontalBox();
		labe12 = new JLabel("密码");
		boxV2.add(labe12);
		boxV2.add(Box.createHorizontalStrut(20));
		Password = new JPasswordField(16);
		boxV2.add(Password);

		boxV3 = Box.createHorizontalBox();
		button1 = new JButton("注册");
		button1.addActionListener(this);
		boxV3.add(button1);
		boxV3.add(Box.createHorizontalStrut(20));
		button2 = new JButton("登录");
		button2.addActionListener(this);
		boxV3.add(button2);

		baseBox = Box.createVerticalBox();
		baseBox.add(Box.createVerticalStrut(50));
		baseBox.add(title);
		baseBox.add(Box.createVerticalStrut(50));
		baseBox.add(boxV1);
		baseBox.add(Box.createVerticalStrut(50));
		baseBox.add(boxV2);
		baseBox.add(Box.createVerticalStrut(50));
		baseBox.add(boxV3);

		add(baseBox);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == button1) {
			Register register = new Register(this);
			register.setVisible(true);
			this.setVisible(false);
		} else if (e.getSource() == button2) {
			// Mainwindow main = new Mainwindow();
			// this.setVisible(false);
			String num = text1.getText();
			String pass = String.valueOf(Password.getPassword());

			ConnectTo db = new ConnectTo();
			boolean isRight = db.seek(num, pass);
			db.dbclose();

			if (isRight) {
				// this.setVisible(false);
				this.dispose();
				new Client().startUp(num);
			} else {
				JOptionPane.showMessageDialog(this, "登录失败，检查信息", "error",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}
}
