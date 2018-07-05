/*
 * @(#)CategoryDAOInterface.java $version 2013. 5. 24.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.dao;

import com.okitoki.checklist.database.model.T_CART_INFO;

import java.util.List;

/**
 * @author nbp
 */
public interface CartInfoDao {
	public int insert(T_CART_INFO TCARTINFO);
	
	public T_CART_INFO get(long id);
	
	public List<T_CART_INFO> getAll();
	public List<T_CART_INFO> getAllCartInfoDesc(String orderBy);

	public void delete(T_CART_INFO TCARTINFO);
	public void update(T_CART_INFO TCARTINFO);

	public void close();
}