package com.okitoki.checklist.database.model;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-06.
 */
public class PhotoEvent {

    public PhotoEvent(String photoPath){
        this.photoPath = photoPath;
    }

    int selectPhoto;

    public String photoPath;

    public int getSelectPhoto() {
        return selectPhoto;
    }

    public void setSelectPhoto(int selectPhoto) {
        this.selectPhoto = selectPhoto;
    }
}
