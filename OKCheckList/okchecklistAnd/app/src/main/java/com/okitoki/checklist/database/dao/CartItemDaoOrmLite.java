/*
 * @(#)NBPNoteDaoOrmLite.java $version 2013. 5. 24.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.dao;

import android.content.Context;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.okitoki.checklist.database.DatabaseHelperOrmLite;
import com.okitoki.checklist.database.model.T_CART_ITEM;

import java.sql.SQLException;
import java.util.List;

public class CartItemDaoOrmLite implements CartItemDao {
	private RuntimeExceptionDao<T_CART_ITEM, Long> cartItemDao = null;

	public void init(Context context) {
		DatabaseHelperOrmLite helper = new DatabaseHelperOrmLite(context);
		cartItemDao = helper.getCartItemDao();
	}
	
	public void init(DatabaseHelperOrmLite helper) {
		cartItemDao = helper.getCartItemDao();
	}


	@Override
	public void insert(T_CART_ITEM TCARTITEM) {
		cartItemDao.create(TCARTITEM);
	}

	@Override
	public T_CART_ITEM get(long noteId) {
		return cartItemDao.queryForId(noteId);
	}

	@Override
	public void delete(T_CART_ITEM TCARTITEM) {
		cartItemDao.delete(TCARTITEM);
	}

	@Override
	public List<T_CART_ITEM> getAll() {
		return cartItemDao.queryForAll();
	}

	@Override
	public List<T_CART_ITEM> getByTitleQuery(String query) {
		try {
			return cartItemDao.queryBuilder().where().eq("isDel", false).and()
					.like("cart_title", "%" + query + "%").query();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<T_CART_ITEM> getByCartIdQuery(int cartId, String orderBy) {
		try {
			return cartItemDao.queryBuilder().orderBy(orderBy, false).where()
					.eq("cart_id", cartId).and().eq("del_fl", false).query();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<T_CART_ITEM> getAllFavItemListPaging(int limit, int offset) {
		try {
			String query = "select * from T_CART_ITEM where del_fl = 0 " +
					"group by item_name ORDER BY COUNT(*) DESC limit +" + limit +" offset "+offset+";";

			GenericRawResults<T_CART_ITEM> result  =
					cartItemDao.queryRaw(query, cartItemDao.getRawRowMapper());
			return result.getResults();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		cartItemDao = null;
	}

	@Override
	public void deleteByCartId(long cartId) {
		List<T_CART_ITEM> TCARTITEMList = cartItemDao.queryForEq("cart_id", cartId);
		for (T_CART_ITEM item : TCARTITEMList) {
			try {
				item.setIsDelete(true);
				item.update();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void update(T_CART_ITEM TCARTITEM) {
		cartItemDao.update(TCARTITEM);
	}
}