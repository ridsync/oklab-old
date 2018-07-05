/*
 * @(#)Note.java $version 2013. 5. 21.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "T_STORE_INFO")
public class T_STORE_INFO extends BaseDaoEnabled<T_STORE_INFO, Long> {
	@DatabaseField(generatedId = true)
	private int store_id;

	@DatabaseField(canBeNull = false)
	private String store_name;

	@DatabaseField
    private String store_icon;

	public T_STORE_INFO() {}

	public T_STORE_INFO(String store_name, String store_icon) {
		this.store_name = store_name;
		this.store_icon = store_icon;
	}

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_icon() {
        return store_icon;
    }

    public void setStore_icon(String store_icon) {
        this.store_icon = store_icon;
    }
}
