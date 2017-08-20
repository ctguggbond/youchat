package com.chat.entity;
import java.net.URL;

import javax.swing.ImageIcon;
public class ChatPic extends ImageIcon{

	private static final long serialVersionUID = 1L;
	int im;
	
	public int getIm() {
		return im;
	}
	public void setIm(int im) {
		this.im = im;
	}
	public ChatPic(URL url,int im){
		super(url);
		this.im = im;
	}
}
