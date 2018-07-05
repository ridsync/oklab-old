/*
 * @(#)Note.java $version 2013. 5. 21.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "T_CART_ITEM")
public class T_CART_ITEM extends BaseDaoEnabled<T_CART_ITEM, Long> {
	@DatabaseField(generatedId = true)
	private int item_id;

    @DatabaseField(canBeNull = false)
    private int cart_id;

	@DatabaseField(canBeNull = false)
	private String item_name;
	
	@DatabaseField
    private String barcode;

    @DatabaseField
    private int qty;

    @DatabaseField(defaultValue = "0")
    private boolean check_fl;

	private boolean checkView_fl;

    @DatabaseField(defaultValue = "0")
    private boolean favor_fl;

	@DatabaseField(defaultValue = "0")
	private boolean del_fl;

	@DatabaseField(dataType = DataType.DATE)
    private java.util.Date reg_date;

    @DatabaseField(dataType = DataType.DATE)
    private java.util.Date mod_date;


    @DatabaseField(foreign = true, foreignAutoRefresh=true)
    private T_CART_INFO cartInfo;

	public T_CART_ITEM() {}
	
	public T_CART_ITEM(String item_name, String barcode, Date reg_date , Date mod_date) {
		this.item_name = item_name;
		this.barcode = barcode;
		this.reg_date = reg_date;
		this.mod_date = mod_date;
	}
	
	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public java.util.Date getMod_date() {
		return mod_date;
	}

	public void setMod_date(java.util.Date mod_date) {
		this.mod_date = mod_date;
	}

	public int getCart_id() {
		return cart_id;
	}

	public void setCart_id(int cart_id) {
		this.cart_id = cart_id;
	}

	public T_CART_INFO getCartInfo() {
		return cartInfo;
	}

	public void setCartInfo(T_CART_INFO cartInfo) {
		this.cartInfo = cartInfo;
	}

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public boolean isCheck_fl() {
        return check_fl;
    }

    public void setCheck_fl(boolean check_fl) {
        this.check_fl = check_fl;
    }

    public boolean getFavor_fl() {
        return favor_fl;
    }

    public void setFavor_fl(boolean favor_fl) {
        this.favor_fl = favor_fl;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

	public boolean isCheckView_fl() {
		return checkView_fl;
	}

	public void setCheckView_fl(boolean checkView_fl) {
		this.checkView_fl = checkView_fl;
	}

	public boolean isDelete() {
		return del_fl;
	}

	public void setIsDelete(boolean isDelete) {
		this.del_fl = isDelete;
	}
}
