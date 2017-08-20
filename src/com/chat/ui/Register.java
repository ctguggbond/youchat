package com.chat.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.chat.utils.*;

public class Register extends JFrame implements FocusListener, ActionListener {
	private Box boxV, boxH1, boxH2, boxH3, boxH4, boxH5, title;
	private JTextField tName, tId;
	private JPasswordField tPassword;
	private JLabel lName, lId, lPassword, label;
	private JButton buttonYes, buttonNo;
	Login win;

	Register(Login win) {
		this.win = win;
		setBounds(750, 200, 300, 300);
		setLayout(new FlowLayout());
		init();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("注册");
		label = new JLabel("");
		lName = new JLabel("帐号");
		lPassword = new JLabel("密码");

		tName = new JTextField(25);

		tId = new JTextField(16);
		// tPhone = new JTextField(11);

		tPassword = new JPasswordField(16);
		tPassword.setText("请输入密码");
		tPassword.setEchoChar((char) 0);
		tPassword.addFocusListener(this);

		buttonYes = new JButton("确认");
		buttonYes.addActionListener(this);

		buttonNo = new JButton("取消");
		buttonNo.setSize(100, 50);
		buttonNo.addActionListener(this);

		boxV = Box.createVerticalBox();
		boxH1 = Box.createHorizontalBox();
		boxH2 = Box.createHorizontalBox();
		boxH3 = Box.createHorizontalBox();
		boxH4 = Box.createHorizontalBox();
		boxH5 = Box.createHorizontalBox();
		title = Box.createHorizontalBox();

		title.add(label);

		boxH1.add(lName);
		boxH1.add(Box.createHorizontalStrut(8));
		boxH1.add(tName);

		boxH4.add(lPassword);
		boxH4.add(Box.createHorizontalStrut(8));
		boxH4.add(tPassword);

		boxH5.add(buttonYes);
		boxH5.add(Box.createHorizontalStrut(48));
		boxH5.add(buttonNo);

		boxV.add(Box.createVerticalStrut(30));
		boxV.add(title);
		boxV.add(Box.createVerticalStrut(30));
		boxV.add(boxH1);
		boxV.add(Box.createVerticalStrut(25));
		boxV.add(boxH4);
		boxV.add(Box.createVerticalStrut(30));
		boxV.add(boxH5);

		add(boxV);
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		JPasswordField t = (JPasswordField) e.getSource();
		if (String.valueOf(t.getPassword()).equals("请输入密码")) {
			t.setText("");
			t.setEchoChar('*');
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		JPasswordField t = (JPasswordField) e.getSource();
		if (String.valueOf(t.getPassword()).equals("")) {
			t.setText("请输入密码");
			t.setEchoChar((char) 0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == buttonYes) {
			if (tName.getText().equals("")
					|| String.valueOf(tPassword.getPassword()).equals("")) {
				JOptionPane.showMessageDialog(this, "请填写信息", "error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				ConnectTo db = new ConnectTo();
				String name = tName.getText();
				String pass = String.valueOf(tPassword.getPassword());

				String BackMessage = db.insert(name, pass);
				db.dbclose();

				JOptionPane.showMessageDialog(this, BackMessage, "SUCCESS",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (e.getSource() == buttonNo) {
			this.dispose();
			win.setVisible(true);
		}
	}
}
