package com.oklab.util;

public class RSSItem {
	private String m_Title;
	private String m_Date;
	private String m_Url;
	private String m_Description;
	
	public void setTitle(String title) {
		m_Title = title;
	}
	
	public String getTitle() {
		return this.m_Title;
	}
	
	public void setDescription(String description){
		m_Description = description;
	}
	
	public String getDescription(){
		return this.m_Description;
	}
	
	public void setUrl(String url){
		m_Url = url;
	}
	
	public String getUrl(){
		return this.m_Url;
	}
	
	public void setDate(String date){
		m_Date = date;
	}
	
	public String getDate(){
		return this.m_Date;
	}
}
