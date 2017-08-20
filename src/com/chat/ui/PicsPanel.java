package com.chat.ui;

import java.awt.*;

import javax.swing.*;

import com.chat.entity.ChatPic;

import java.awt.event.*;

public class PicsPanel extends JPanel {

	GridLayout gridLayout1 = new GridLayout(7, 15);
	JLabel[] ico = new JLabel[105];
	int i;
	Client c;
	String[] intro = { "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", };/* ͼƬ���� */

	public PicsPanel(Client c) {
		this.c = c;
		try {
			init();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void init() throws Exception {
		this.setPreferredSize(new Dimension(28 * 15, 28 * 7));

		this.setLayout(gridLayout1);
		this.setBackground(SystemColor.text);
		String fileName = "";
		for (i = 0; i < ico.length; i++) {
			fileName = "Pic/" + i + ".gif";
			ico[i] = new JLabel(new ChatPic(
					PicsPanel.class.getResource(fileName), i),
					SwingConstants.CENTER);
			ico[i].setBorder(BorderFactory.createLineBorder(new Color(225, 225,
					225), 1));

			ico[i].setToolTipText(i + "");

			ico[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 1) {
						JLabel cubl = (JLabel) (e.getSource());
						ChatPic cupic = (ChatPic) (cubl.getIcon());
						c.insertSendPic(cupic);
						cubl.setBorder(BorderFactory.createLineBorder(
								new Color(225, 225, 225), 1));
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					((JLabel) e.getSource()).setBorder(BorderFactory
							.createLineBorder(Color.BLUE));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					((JLabel) e.getSource()).setBorder(BorderFactory
							.createLineBorder(new Color(225, 225, 225), 1));
				}

			});
			this.add(ico[i]);
		}
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {

			}

		});
	}

}