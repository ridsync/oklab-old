package com.oklab.oktwitter;

import android.graphics.Bitmap;

public class User {
	Bitmap profile_image_url;
	String screen_name;
	
	public User() { }
	
	public User(Bitmap profile_image_url, String screen_name) {
		this.profile_image_url = profile_image_url;
		this.screen_name = screen_name;
	}

	public Bitmap getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(Bitmap profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	
}
