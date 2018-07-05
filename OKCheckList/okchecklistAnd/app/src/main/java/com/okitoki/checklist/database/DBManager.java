/*
 * @(#)DatabaseService.java $version 2013. 5. 20.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database;

import android.database.sqlite.SQLiteDatabase;

import com.okitoki.checklist.database.dao.CartInfoDao;
import com.okitoki.checklist.database.dao.CartItemDao;
import com.okitoki.checklist.database.dao.DaoFactory;
import com.okitoki.checklist.database.dao.DaoFactoryOrmLite;
import com.okitoki.checklist.database.dao.StoreInfoDao;
import com.okitoki.checklist.database.model.T_CART_INFO;
import com.okitoki.checklist.database.model.T_CART_ITEM;
import com.okitoki.checklist.database.transaction.Transaction;

import java.util.List;

/**
 * UI 단에서 DB 를 직접 사용하는 매니저 클래스
 * TODO DBL로직 별로 클래스 분기 필요??
 *
 */
public class DBManager {
	private static SQLiteDatabase sqlDB = null;
	private static Transaction transaction = null;

	private static CartInfoDao cartInfoDao = null;
	private static CartItemDao cartItemDao = null;
	private static StoreInfoDao storeInfoDao = null;

	public static void setDatabase() {
		DaoFactory daoFactory = DaoFactoryOrmLite.getInstance();
		sqlDB = daoFactory.getSQLiteDatabase();
	}

    public static void setStoreInfoDao(StoreInfoDao dao) {
        storeInfoDao = dao;
    }

    public static void setCartInfoDao(CartInfoDao dao) {
		cartInfoDao = dao;
	}

	public static void setCartItemDao(CartItemDao dao) {
		cartItemDao = dao;
	}

	public static void setTransaction(Transaction t) {
		transaction = t;
	}

	public void beginTransaction() {
		transaction.begin();
	}

	public void endTransaction() {
		transaction.end();
	}

	public void commit() {
		transaction.commit();
	}

	public void rollback() {
		transaction.rollback();
	}

	// MainCartInfo DBL
	public int insertCartInfo(T_CART_INFO TCARTINFO) {
		return cartInfoDao.insert(TCARTINFO);
	}
	
	public T_CART_INFO getCartInfo(int cartId) {
		return cartInfoDao.get(cartId);
	}
	
	public void updateCartInfo(T_CART_INFO TCARTINFO) {
		cartInfoDao.update(TCARTINFO);
	}

	public void deleteCartInfo(T_CART_INFO TCARTINFO) {
		cartInfoDao.delete(TCARTINFO);
	}

	public void deleteAllCartInfo() {
		List<T_CART_INFO> categories = getAllCartInfo();
		for (T_CART_INFO c : categories) {
			deleteCartInfo(c);
		}
	}
	public List<T_CART_INFO> getAllCartInfo() {
		return cartInfoDao.getAll();
	}

	public List<T_CART_INFO> getAllCartInfoDesc(String orderby) {
		return cartInfoDao.getAllCartInfoDesc(orderby);
	}

	// CartItem DBL
	public void deleteAllCartItem() {
		List<T_CART_ITEM> TCARTITEMs = getAllCartItem();
		for (T_CART_ITEM n : TCARTITEMs) {
			deleteCartItem(n);
		}
	}

	public void insertCartItem(T_CART_ITEM TCARTITEM) {
		cartItemDao.insert(TCARTITEM);
	}
	
	public T_CART_ITEM getCartItem(int itemId) {
		return cartItemDao.get(itemId);
	}

	public void deleteCartItem(T_CART_ITEM TCARTITEM) {
		cartItemDao.delete(TCARTITEM);
	}
	
	public List<T_CART_ITEM> getAllCartItem() {
		return cartItemDao.getAll();
	}

	public List<T_CART_ITEM> queryCartItemByTitle(String query) {
		return cartItemDao.getByTitleQuery(query);
	}
	public List<T_CART_ITEM> queryCartItemByCartInfoId(int query, String orderBy) {
		return cartItemDao.getByCartIdQuery(query, orderBy);
	}
	public List<T_CART_ITEM> getAllFavItemListPaging(int limit, int offset) {
		return cartItemDao.getAllFavItemListPaging(limit, offset);
	}

	public void deleteItemByCartInfoId(int id) {
		cartItemDao.deleteByCartId(id);
	}
	
	public void updateCartItem(T_CART_ITEM cart_item) {
		cartItemDao.update(cart_item);
	}

	// StoreInfo DBL



	public void close() {
	}
}
