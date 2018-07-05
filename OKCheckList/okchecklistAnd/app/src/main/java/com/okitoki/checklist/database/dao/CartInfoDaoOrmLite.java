/*
 * @(#)CategoryDaoORMLite.java $version 2013. 5. 24.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.dao;

import android.content.Context;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.okitoki.checklist.database.DatabaseHelperOrmLite;
import com.okitoki.checklist.database.model.T_CART_INFO;
import com.okitoki.checklist.database.model.T_CART_ITEM;

import java.sql.SQLException;
import java.util.List;

public class CartInfoDaoOrmLite implements CartInfoDao {
	private RuntimeExceptionDao<T_CART_INFO, Long> cartInfoDao = null;

	public void init(Context context) {
		DatabaseHelperOrmLite helper = new DatabaseHelperOrmLite(context);
		cartInfoDao = helper.getCartInfoDao();
	}
	
	public void init(DatabaseHelperOrmLite helper) {
		cartInfoDao = helper.getCartInfoDao();
	}

	@Override
	public int insert(T_CART_INFO TCARTINFO) {
		return cartInfoDao.create(TCARTINFO);
	}

	@Override
	public T_CART_INFO get(long id) {
		return cartInfoDao.queryForId(id);
	}

	@Override
	public List<T_CART_INFO> getAll() {
		return cartInfoDao.queryForAll();
	}

	@Override
	public List<T_CART_INFO> getAllCartInfoDesc(String orderBy) {
		try {
			return cartInfoDao.queryBuilder().orderBy(orderBy, false).where().eq("del_fl",false).query();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void delete(T_CART_INFO cartInfo) {
		ForeignCollection<T_CART_ITEM> TCARTITEMs = cartInfo.getCartItems();

		for (T_CART_ITEM item : TCARTITEMs) {
			try {
				item.setIsDelete(true);
				item.update();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		cartInfo.setDel_fl(true);
		update(cartInfo);
	}

	@Override
	public void update(T_CART_INFO TCARTINFO) {
		cartInfoDao.update(TCARTINFO);
	}

	public void close() {
		cartInfoDao = null;
	}
}