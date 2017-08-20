package com.chat.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.io.*;

import javax.sound.midi.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.net.*;

import com.chat.entity.*;

public class Client { // implements MetaEventListener

	JList onlineList;
	DefaultListModel listModel;
	JTextPane jpMsg, jpChat;
	JScrollPane jspMsg, jspChat;
	Vector<String> listVector;
	String userName;
	SimpleDateFormat sf;
	ObjectInputStream in;
	ObjectOutputStream out;

	Sequencer sequencer;
	Sequence sequence;
	Sequence mySequence = null;
	Track track;

	JFrame theFrame;
	StyledDocument docMsg;
	StyledDocument docChat;
	int pos1;
	int pos2;
	List<PicInfo> myPicInfo;
	List<PicInfo> receivdPicInfo;

	boolean isSend = false; // 判断是自己发送还是来自服务器的消息，为了填颜色显示的坑
	private JLabel infolabel;

	public void startUp(String name) {
		init();
		userName = name;
		connectToSever();
		buildGUI();
	}

	private void init() {
		// TODO Auto-generated method stub
		sf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		listModel = new DefaultListModel();
		myPicInfo = new LinkedList<PicInfo>();
		receivdPicInfo = new LinkedList<PicInfo>();
		listVector = new Vector<String>();
	}

	// 聊天界面
	public void buildGUI() {
		theFrame = new JFrame("youChat同城夜聊室");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				sendUauslMessage(2, userName, "offline");
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		Box buttonBox = new Box(BoxLayout.Y_AXIS);

		jpChat = new JTextPane();
		jpChat.setEditable(false);
		jspChat = new JScrollPane(jpChat,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jspChat.setPreferredSize(new Dimension(150, 600));

		buttonBox.add(jspChat);

		jpMsg = new JTextPane();
		jpMsg.setFont(new Font("楷体", 0, 25));

		jspMsg = new JScrollPane(jpMsg,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jspMsg.setPreferredSize(new Dimension(150, 100));

		buttonBox.add(jspMsg);

		Box Hori = Box.createHorizontalBox();

		JButton sendIt = new JButton("发送");
		sendIt.addActionListener(new MySendListener());
		Hori.add(Box.createHorizontalStrut(15));
		Hori.add(sendIt);

		buttonBox.add(Hori);

		background.add(BorderLayout.CENTER, buttonBox);
		theFrame.getContentPane().add(background);

		Box rightBox = new Box(BoxLayout.Y_AXIS);

		JLabel onlinelabel = new JLabel("---在线好友---");
		onlinelabel.setFont(new Font("楷体", 0, 18));
		rightBox.add(onlinelabel);

		infolabel = new JLabel();
		infolabel.setFont(new Font("楷体", 0, 18));
		infolabel.setForeground(Color.RED);
		rightBox.add(infolabel);

		onlineList = new JList(listModel);
		onlineList.setFont(new Font("楷体", 0, 18));
		onlineList.setVisibleRowCount(30);
		JScrollPane theOnlineList = new JScrollPane(onlineList);
		rightBox.add(theOnlineList);

		Box Hori2 = Box.createHorizontalBox();
		ImageIcon icon = new ImageIcon(Client.class.getResource("Pic/tmg.gif"));
		icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth(),
				icon.getIconHeight(), Image.SCALE_DEFAULT));
		JLabel xiu = new JLabel(icon, SwingConstants.CENTER);
		Hori2.add(xiu);
		rightBox.add(Hori2);

		PicsPanel pp = new PicsPanel(this);
		rightBox.add(pp);
		background.add(BorderLayout.EAST, rightBox);

		docChat = jpChat.getStyledDocument();
		docMsg = jpMsg.getStyledDocument();
		theFrame.setBounds(250, 150, 1200, 800);
		theFrame.setVisible(true);
	}

	// 发送监听
	public class MySendListener implements ActionListener { // new - save
		public void actionPerformed(ActionEvent a) {
			isSend = true;
			sendMsg();

		}
	}

	// 插入表情到发送区
	public void insertSendPic(ImageIcon imgIc) {
		jpMsg.insertIcon(imgIc);
	}

	// 发送消息
	public void sendMsg() {
		String message = jpMsg.getText() + "*" + buildPicInfo();

		if (sendUauslMessage(0, userName, message)) {

			addMeg();
			this.jpMsg.setText("");
		} else {
			JOptionPane.showMessageDialog(theFrame, "发送失败,检查网络连接", "error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// 添加消息到消息队列
	public void addMeg() {
		if (jpMsg.getText().length() == 0)
			return;
		String date = sf.format(new Date());
		String msg = userName + "   " + date;
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(attrSet, 20);
		StyleConstants.setForeground(attrSet, Color.RED);
		insert(msg, attrSet);

		pos2 = jpChat.getCaretPosition();
		StyleConstants.setFontSize(attrSet, 25);
		StyleConstants.setForeground(attrSet, Color.BLACK);
		insert(jpMsg.getText(), attrSet);
		insertPics(false);
	}

	// 插入文字
	private void insert(String text, SimpleAttributeSet s) {
		try {
			docChat.insertString(docChat.getLength(), text + "\n", s);
			jpChat.setCaretPosition(docChat.getLength());

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// 插入图片
	private void insertPics(boolean isFriend) {

		if (isFriend) {
			if (this.receivdPicInfo.size() <= 0) {
				return;
			} else {
				for (int i = 0; i < receivdPicInfo.size(); i++) {
					PicInfo pic = receivdPicInfo.get(i);
					String fileName;
					jpChat.setCaretPosition(pos1 + pic.getPos());
					fileName = "Pic/" + pic.getVal() + ".gif";
					jpChat.insertIcon(new ImageIcon(Client.class
							.getResource(fileName)));

				}
				receivdPicInfo.clear();
			}
		} else {

			if (myPicInfo.size() <= 0) {
				return;
			} else {
				for (int i = 0; i < myPicInfo.size(); i++) {
					PicInfo pic = myPicInfo.get(i);
					jpChat.setCaretPosition(pos2 + pic.getPos());
					String fileName;
					fileName = "Pic/" + pic.getVal() + ".gif";
					jpChat.insertIcon(new ImageIcon(Client.class
							.getResource(fileName)));
					/* jpChat.updateUI(); */
				}
				myPicInfo.clear();
			}
		}
		jpChat.setCaretPosition(docChat.getLength());
	}

	// 创建图片信息
	private String buildPicInfo() {
		StringBuilder sb = new StringBuilder("");

		for (int i = 0; i < this.jpMsg.getText().length(); i++) {
			if (docMsg.getCharacterElement(i).getName().equals("icon")) {
				Icon icon = StyleConstants.getIcon(jpMsg.getStyledDocument()
						.getCharacterElement(i).getAttributes());
				ChatPic cupic = (ChatPic) icon;
				PicInfo picInfo = new PicInfo(i, cupic.getIm() + "");
				myPicInfo.add(picInfo);
				sb.append(i + "&" + cupic.getIm() + "+");
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
		// return null;
	}

	// 处理接收到的信息
	public void handleReceivMessage(String MessageReceiv) {

		if (isSend)
			return;
		String[] strs = MessageReceiv.split("[*]");
		int msgType; // msgType 0 表示消息 1 表示上线 2表示下线 3
		if (strs.length >= 3) {
			msgType = Integer.parseInt(strs[0]);
		} else {
			return;
		}
		String uname = strs[1];

		String message = strs[2];

		if (strs.length > 3) {
			for (int i = 3; i < strs.length; i++) {
				message = message + "*" + strs[i];
			}
		}
		if (msgType == 0)
			addRecMsg(uname, message);
		else if (msgType == 1) {
			if (uname.equals(userName))
				return;
			infolabel.setText(uname + "上线了！！！");
			listModel.addElement(uname);
		} else if (msgType == 2) {
			infolabel.setText(uname + "下线了！！！");
			listVector.remove(uname);
			listModel.removeElement(uname);
		} else if (msgType == 3) {
			for (int i = 2; i < strs.length; i++) {
				listModel.addElement(strs[i]);
			}
		}

	}

	// 添加接收消息到窗口
	private void addRecMsg(String uname, String message) {
		// TODO Auto-generated method stub

		String msg = uname + " " + sf.format(new Date());
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(attrSet, 20);
		StyleConstants.setForeground(attrSet, Color.BLUE);
		insert(msg, attrSet);

		int index = message.lastIndexOf("*");

		System.out.println("index=" + index);

		StyleConstants.setFontSize(attrSet, 25);
		StyleConstants.setForeground(attrSet, Color.BLACK);

		pos1 = jpChat.getCaretPosition();

		if (index > 0 && index < message.length() - 1) {

			insert(message.substring(0, index), attrSet);
			receivedPicInfo(message.substring(index + 1, message.length()));
			insertPics(true);
		} else {
			insert(message, attrSet);
		}
	}

	// 接收到的表情
	public void receivedPicInfo(String picInfos) {
		String[] infos = picInfos.split("[+]");
		for (int i = 0; i < infos.length; i++) {
			String[] tem = infos[i].split("[&]");
			if (tem.length == 2) {
				PicInfo pic = new PicInfo(Integer.parseInt(tem[0]), tem[1]);
				receivdPicInfo.add(pic);
			}
		}
	}

	// 发送消息
	public boolean sendUauslMessage(int flag, String userName, String message) {

		try {

			out.writeObject(flag + "*" + userName + "*" + message);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	// 连接服务器
	public void connectToSever() {
		try {
			Socket sock = new Socket("127.0.0.1", 4242);
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
			Thread remote = new Thread(new RemoteReader());
			remote.start();
			sendUauslMessage(1, userName, "online");
		} catch (Exception ex) {
			System.out.println("连接不上服务器，你可以一个人玩");
		}
	}

	// ==================================================================客户端接收线程

	public class RemoteReader implements Runnable {
		Object obj = null;

		public void run() {
			try {
				while ((obj = in.readObject()) != null) {

					System.out.println("got an object from server");
					String MessageReceiv = (String) obj;
					Client.this.handleReceivMessage(MessageReceiv);

					isSend = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	// ========================================================================
}