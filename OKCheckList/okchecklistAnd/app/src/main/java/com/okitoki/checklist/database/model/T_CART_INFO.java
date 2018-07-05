/*
 * @(#)Category.java $version 2013. 5. 22.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * With BaseDaoEnabled, Entities can perform operations that are available only through DAO.
 */
@DatabaseTable(tableName = "T_CART_INFO")
public class T_CART_INFO extends BaseDaoEnabled<T_CART_INFO, Long> {
	@DatabaseField(generatedId = true)
	private int cart_id;
	
	@DatabaseField(canBeNull = false)
	private String store_id;
	
	@DatabaseField(canBeNull = false)
    private String cart_title; // 매물명

    @DatabaseField
    private int totalScore; // 토탈 점수

    @DatabaseField
    private String location; // 상세매물위치 동 호수 층

    @DatabaseField
    private String address;  // 주소 위치

    @DatabaseField
    private double address_latitude;  // 주소 위치 위도

    @DatabaseField
    private double address_longitude;  // 주소 위치 경도

    @DatabaseField
    private int addressScore;  // 위치및 편의시설 점수

    @DatabaseField
    private String prices; // 분양가/매매가

    @DatabaseField
    private Boolean isMijung; // 이사가능날짜

    @DatabaseField
    private String migrateDate; // 이사가능날짜

    @DatabaseField
    private int aroundStatusScore; // 집주변 환경 상태 인접건물관계 점수

    @DatabaseField
    private String aroundStatus; // 집주변 환경 상태 인접건물관계

    @DatabaseField
    private String fee; // 관리비

    @DatabaseField
    private String area; // 공급/전용면적

    @DatabaseField
    private int interior; // 인테리어 점수

    @DatabaseField
    private int facilityScore; // 집안 시설작동상태 점수

    @DatabaseField
    private int roomsCnt; // 방수량

    @DatabaseField
    private int bathroomsCnt; // 화장실수량

    @DatabaseField
    private int schoolScore; // 학군 점수

    @DatabaseField
    private int trafficScore; // 교통 점수

    @DatabaseField
    private int sunshineRight; // 일조권 조망권 점수

    @DatabaseField
    private String parking; // 주차장상태

    @DatabaseField
    private String usage; // 건물용도 주거용

    @DatabaseField
    private String et_noise; // 기타사항

    @DatabaseField
    private String etc; // 기타사항

    @DatabaseField
    private String photoUris; // 사진정보.

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    byte[] imageBytes;   // 사진정보.


    @DatabaseField
    private String status;

    @DatabaseField(defaultValue = "0")
    private boolean isGotCartAllitem;

    @DatabaseField(defaultValue = "0")
    private boolean del_fl;

    @DatabaseField(dataType = DataType.DATE)
    private Date buy_date;

    @DatabaseField(dataType = DataType.DATE)
    private Date reg_date;

    @DatabaseField(dataType = DataType.DATE)
    private Date mod_date;

	@ForeignCollectionField(eager = true)
	private ForeignCollection<T_CART_ITEM> cartItems;

    @DatabaseField
    private int cartItemsCount;

    @DatabaseField
    private int checkedCount;

    @DatabaseField
    private String itemNameString;

    @DatabaseField
    private String desc;

	public int getCart_id() {
		return cart_id;
	}

	public void setCart_id(int cart_id) {
		this.cart_id = cart_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getCart_title() {
		return cart_title;
	}

	public void setCart_title(String cart_title) {
		this.cart_title = cart_title;
	}

	public ForeignCollection<T_CART_ITEM> getCartItems() {
		return cartItems;
	}

	public void setCartItems(ForeignCollection<T_CART_ITEM> cartItems) {
		this.cartItems = cartItems;
	}

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDel_fl() {
        return del_fl;
    }

    public void setDel_fl(boolean del_fl) {
        this.del_fl = del_fl;
    }

    public Date getBuy_date() {
        return buy_date;
    }

    public void setBuy_date(Date buy_date) {
        this.buy_date = buy_date;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public Date getMod_date() {
        return mod_date;
    }

    public void setMod_date(Date mod_date) {
        this.mod_date = mod_date;
    }

    public boolean isGotCartAllitem() {
        return isGotCartAllitem;
    }

    public void setIsGotCartAllitem(boolean isGotCartAllitem) {
        this.isGotCartAllitem = isGotCartAllitem;
    }

    public int getCartItemsCount() {
        return cartItemsCount;
    }

    public void setCartItemsCount(int cartItemsCount) {
        this.cartItemsCount = cartItemsCount;
    }

    public String getItemNameString() {
        return itemNameString;
    }

    public void setItemNameString(String itemNameString) {
        this.itemNameString = itemNameString;
    }

    public int getCheckedCount() {
        return checkedCount;
    }

    public void setCheckedCount(int checkedCount) {
        this.checkedCount = checkedCount;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAddressScore() {
        return addressScore;
    }

    public void setAddressScore(int addressScore) {
        this.addressScore = addressScore;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getMigrateDate() {
        return migrateDate;
    }

    public void setMigrateDate(String migrateDate) {
        this.migrateDate = migrateDate;
    }

    public int getAroundStatusScore() {
        return aroundStatusScore;
    }

    public void setAroundStatusScore(int aroundStatusScore) {
        this.aroundStatusScore = aroundStatusScore;
    }

    public String getAroundStatus() {
        return aroundStatus;
    }

    public void setAroundStatus(String aroundStatus) {
        this.aroundStatus = aroundStatus;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getInterior() {
        return interior;
    }

    public void setInterior(int interior) {
        this.interior = interior;
    }

    public int getFacilityScore() {
        return facilityScore;
    }

    public void setFacilityScore(int facilityScore) {
        this.facilityScore = facilityScore;
    }

    public int getRoomsCnt() {
        return roomsCnt;
    }

    public void setRoomsCnt(int roomsCnt) {
        this.roomsCnt = roomsCnt;
    }

    public int getBathroomsCnt() {
        return bathroomsCnt;
    }

    public void setBathroomsCnt(int bathroomsCnt) {
        this.bathroomsCnt = bathroomsCnt;
    }

    public int getSchoolScore() {
        return schoolScore;
    }

    public void setSchoolScore(int schoolScore) {
        this.schoolScore = schoolScore;
    }

    public int getTrafficScore() {
        return trafficScore;
    }

    public void setTrafficScore(int trafficScore) {
        this.trafficScore = trafficScore;
    }

    public int getSunshineRight() {
        return sunshineRight;
    }

    public void setSunshineRight(int sunshineRight) {
        this.sunshineRight = sunshineRight;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getEt_noise() {
        return et_noise;
    }

    public void setEt_noise(String et_noise) {
        this.et_noise = et_noise;
    }

    public String getPhotoList() {
        return photoUris;
    }

    public void setPhotoUris(String picture) {
        this.photoUris = picture;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Boolean getIsMijung() {
        return isMijung;
    }

    public void setIsMijung(Boolean isMijung) {
        this.isMijung = isMijung;
    }

    public double getAddress_latitude() {
        return address_latitude;
    }

    public void setAddress_latitude(double address_latitude) {
        this.address_latitude = address_latitude;
    }

    public double getAddress_longitude() {
        return address_longitude;
    }

    public void setAddress_longitude(double address_longitude) {
        this.address_longitude = address_longitude;
    }
}
