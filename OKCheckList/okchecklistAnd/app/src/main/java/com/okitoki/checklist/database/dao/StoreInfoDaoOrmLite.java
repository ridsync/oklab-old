/*
 * @(#)CategoryDaoORMLite.java $version 2013. 5. 24.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.dao;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.okitoki.checklist.database.DatabaseHelperOrmLite;
import com.okitoki.checklist.database.model.T_STORE_INFO;

import java.util.List;

public class StoreInfoDaoOrmLite implements StoreInfoDao {
	private RuntimeExceptionDao<T_STORE_INFO, Long> storeInfoDao = null;

	public void init(Context context) {
		DatabaseHelperOrmLite helper = new DatabaseHelperOrmLite(context);
		storeInfoDao = helper.getStoreInfoDao();
	}
	
	public void init(DatabaseHelperOrmLite helper) {
		storeInfoDao = helper.getStoreInfoDao();
	}

	@Override
	public void insert(T_STORE_INFO storeInfo) {
		storeInfoDao.create(storeInfo);
	}

	@Override
	public T_STORE_INFO get(long id) {
		return storeInfoDao.queryForId(id);
	}

	@Override
	public List<T_STORE_INFO> getAll() {
		return storeInfoDao.queryForAll();
	}

	@Override
	public void delete(T_STORE_INFO TCARTINFO) {
//		ForeignCollection<T_CART_ITEM> TCARTITEMs = TCARTINFO.getCartItems();
//
//		for (T_CART_ITEM TCARTITEM : TCARTITEMs) {
//			try {
//				TCARTITEM.delete();
//			} catch (SQLException e) {
//				throw new RuntimeException(e);
//			}
//		}
//		storeInfoDao.delete(TCARTINFO);
	}
	
	public void close() {
		storeInfoDao = null;
	}
}