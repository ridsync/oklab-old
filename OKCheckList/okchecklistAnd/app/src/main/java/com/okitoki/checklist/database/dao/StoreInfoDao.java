/*
 * @(#)CategoryDAOInterface.java $version 2013. 5. 24.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.dao;

import com.okitoki.checklist.database.model.T_STORE_INFO;

import java.util.List;

/**
 * @author nbp
 */
public interface StoreInfoDao {
	public void insert(T_STORE_INFO StoreInfo);
	
	public T_STORE_INFO get(long id);
	
	public List<T_STORE_INFO> getAll();
	
	public void delete(T_STORE_INFO StoreInfo);
	
	public void close();
}