package com.example.test.oktest;

/**
 * Created by ojungwon on 2014-10-07.
 */
public class NavDrawerItem {

    private int position;
    private String title;
    private int icon;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem(){}

    public NavDrawerItem(int position, String title, int icon){
        this.position = position;
        this.title = title;
        this.icon = icon;
    }

    public NavDrawerItem(int position, String title, int icon, boolean isCounterVisible, String count){
        this.position = position;
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle(){
        return this.title;
    }

    public int getPosition(){
        return this.position;
    }
    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}