package com.chat.server;

import java.io.*;
import java.net.*;
import java.sql.ClientInfoStatus;
import java.util.*;

import javax.swing.DefaultListModel;

public class Server {
	ArrayList clientOutputStreams;
	ArrayList<String> clients;

	public static void main(String[] args) {

		new Server().go();
	}

	public class ClientHandler implements Runnable {
		ObjectInputStream in;
		Socket sock;

		public ClientHandler(Socket clientSOcket) {
			try {
				sock = clientSOcket;
				in = new ObjectInputStream(sock.getInputStream());

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public void run() {
			Object o1;
			try {
				while ((o1 = in.readObject()) != null) {
					System.out.println("read one objects");
					handleMessage(o1);
					tellEveryone(o1);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		private void handleMessage(Object o1) {
			// TODO Auto-generated method stub
			String s = (String) o1;
			String[] strs = s.split("[*]");
			int msgType;
			if (strs.length >= 3) {
				msgType = Integer.parseInt(strs[0]);
			} else {
				return;
			}
			String uname = strs[1];

			String message = strs[2];

			if (msgType == 1) {
				clients.add(uname);
			} else if (msgType == 2) {
				clients.remove(uname);
			}
		}
	}

	public void go() {
		clientOutputStreams = new ArrayList();
		clients = new ArrayList<String>();
		try {
			ServerSocket serverSock = new ServerSocket(4242);
			while (true) {
				Socket clientSocket = serverSock.accept();
				ObjectOutputStream out = new ObjectOutputStream(
						clientSocket.getOutputStream());

				clientOutputStreams.add(out);

				out.writeObject(getOnlineString());

				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void tellEveryone(Object one) {
		Iterator it = clientOutputStreams.iterator();
		while (it.hasNext()) {
			try {
				ObjectOutputStream out = (ObjectOutputStream) it.next();
				out.writeObject(one);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public String getOnlineString() {
		Iterator it = clients.iterator();
		StringBuffer s = new StringBuffer();
		s.append("3*nobody");
		while (it.hasNext()) {
			s.append("*" + it.next());
		}
		return s.toString();
	}

}
