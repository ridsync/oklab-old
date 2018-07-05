package com.okitoki.checklist.network.model;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-20.
 */
public class UserInfo {

    int list_seq;
    int user_id;
    String user_login_id;
    String photo_name;
    String essay;
    int user_gender;

    public String getEssay() {
        return essay;
    }

    public void setEssay(String essay) {
        this.essay = essay;
    }

    public int getList_seq() {
        return list_seq;
    }

    public void setList_seq(int list_seq) {
        this.list_seq = list_seq;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_login_id() {
        return user_login_id;
    }

    public void setUser_login_id(String user_login_id) {
        this.user_login_id = user_login_id;
    }
}
