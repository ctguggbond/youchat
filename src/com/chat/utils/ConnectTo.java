package com.chat.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class ConnectTo {
	
	Connection con;
	Statement statement;
	
	public ConnectTo() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("成功加载MySQL驱动！");
			} catch (ClassNotFoundException e) {
				System.out.println("驱动加载失败");
			}
			
		
			
			try {
				con = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/youchat", "test", "123456");
				
				statement = con.createStatement();
				System.out.println("成功连接到数据库！");
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
   public String insert(String name,String passwd){
		
		try {
			statement.execute("insert into users(username,password) values ('"+ name + "','" + passwd + "')");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "注册失败,信息不合法";
		}				
		return "注册成功，请登陆";
	}
    public void dbclose()
    	{	
			try {
				statement.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    	}
    
    public boolean seek(String name,String pass){
    	
    ResultSet re = null;
		try {
			re = statement.executeQuery("select password from users where username='"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fpass = null;
		
		try {
			re.next();
			
			fpass = re.getString(1).trim();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		}
		
		if(pass.equals(fpass)){
			return true;
		}
		return false;
    	}
	
}