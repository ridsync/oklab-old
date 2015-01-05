package com.oklab.oktwitter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.text.format.DateUtils;


public class Status {

	String createdAt;
	String text;
	User user;
	long id;
//	int rImage;
	
	public Status() { }

	public Status( String createdAt, String text, User user ,long id) {
		this.createdAt = createdAt;
		this.text = text;
		this.user = user;
		this.id = id;
	}
	// TestTwitter�� ���� (#(1)buildData()�� ���ǵ����ͻ���Ұ��)
//	public Status( int rImage , String createdAt, String screenName ,String text) {
//		this.rImage = rImage;
//		this.createdAt = createdAt;
//		this.screenName = screenName;
//		this.text = text;
//}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	
}
