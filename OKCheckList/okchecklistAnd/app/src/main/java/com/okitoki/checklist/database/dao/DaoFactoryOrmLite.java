/*
 * @(#)DaoFactory.java $version 2013. 5. 25.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.okitoki.checklist.database.DBManager;
import com.okitoki.checklist.database.DatabaseHelperOrmLite;
import com.okitoki.checklist.database.transaction.Transaction;
import com.okitoki.checklist.database.transaction.TransactionOrmLite;

public class DaoFactoryOrmLite implements DaoFactory {
	private static DaoFactoryOrmLite INSTANCE = new DaoFactoryOrmLite();
	
	private Context context;
	private DatabaseHelperOrmLite databaseHelper;
	private TransactionOrmLite transaction;
	private CartInfoDaoOrmLite cartInfoDao = new CartInfoDaoOrmLite();
	private CartItemDaoOrmLite cartItem = new CartItemDaoOrmLite();
	private StoreInfoDaoOrmLite storeInfoDao = new StoreInfoDaoOrmLite();

	private DaoFactoryOrmLite() {}
	
	public static DaoFactory getInstance() { return INSTANCE; }
	
	@Override
	public void setContext(Context context) {
		if (this.context == null || this.databaseHelper == null) {
			this.context = context;
			databaseHelper = new DatabaseHelperOrmLite(this.context);
			cartInfoDao.init(databaseHelper);
			cartItem.init(databaseHelper);
			storeInfoDao.init(databaseHelper);
		}

		DBManager.setStoreInfoDao(storeInfoDao);
		DBManager.setCartInfoDao(cartInfoDao);
		DBManager.setCartItemDao(cartItem);
		DBManager.setTransaction(getTransaction());
		Log.i("OKCartApplication", "onCreate DBManager Set Dao") ;
	}

	@Override
	public SQLiteDatabase getSQLiteDatabase() {
		return databaseHelper.getWritableDatabase();
	}

	@Override
	public Transaction getTransaction() {
		if (databaseHelper == null)
			throw new RuntimeException("Context is not set");
		
		if (transaction == null) {
			transaction = new TransactionOrmLite();
			transaction.init(databaseHelper);
		}
		
		return transaction;
	}

	@Override
	public CartInfoDao getCartInfoDao() {
		return cartInfoDao;
	}

	@Override
	public CartItemDao getCartItemDao() {
		return cartItem;
	}

	@Override
	public StoreInfoDao getStoreInfoDao() {
		return storeInfoDao;
	}
}