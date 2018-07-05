package com.okitoki.checklist.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.okitoki.checklist.database.transaction.Transaction;

public interface DaoFactory {
	public void setContext(Context context);
	public SQLiteDatabase getSQLiteDatabase();
	public Transaction getTransaction();
	public CartInfoDao getCartInfoDao();
	public CartItemDao getCartItemDao();
	public StoreInfoDao getStoreInfoDao();
}
