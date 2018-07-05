/*
 * @(#)DatabaseHelper.java $version 2013. 5. 22.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.okitoki.checklist.database.model.T_CART_INFO;
import com.okitoki.checklist.database.model.T_CART_ITEM;
import com.okitoki.checklist.database.model.T_STORE_INFO;

/**
 * ORMLite SqliteOpenHelper Class
 *  DAO를통해서 DB에 접근시에 DB 및 테이블 생성이 된다 !! ㅠ.ㅠ
 */
public class DatabaseHelperOrmLite extends OrmLiteSqliteOpenHelper {
	private static final String DB_NAME = "OKCart.db";
	private static final int DB_VERSION = 2;
	
	private RuntimeExceptionDao<T_CART_INFO, Long> cartInfoDao = null;
	private RuntimeExceptionDao<T_CART_ITEM, Long> itemDao = null;
	private RuntimeExceptionDao<T_STORE_INFO, Long> storeInfoDao = null;

	public DatabaseHelperOrmLite(Context context) {
		super(context, context.getExternalFilesDir(null) +  DB_NAME, null, DB_VERSION);
		Log.i("DatabaseHelperOrmLite", "DatabaseHelperOrmLite DB_NAME = " + DB_NAME + " / DB_VERSION = " + DB_VERSION) ;
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		Log.i("DatabaseHelperOrmLite", "onCreate");
		try {
			TableUtils.createTable(connectionSource, T_STORE_INFO.class);
			TableUtils.createTable(connectionSource, T_CART_INFO.class);
			TableUtils.createTable(connectionSource, T_CART_ITEM.class);
			Log.i("DatabaseHelperOrmLite", "createTable Finished") ;
		} catch (SQLException e) {
			Log.i("DatabaseHelperOrmLite", "createTable Failed") ;
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
	
	public RuntimeExceptionDao<T_CART_INFO, Long> getCartInfoDao() {
		if (cartInfoDao == null)
			cartInfoDao = getRuntimeExceptionDao(T_CART_INFO.class);
		return cartInfoDao;
	}

	public RuntimeExceptionDao<T_CART_ITEM, Long> getCartItemDao() {
		if (itemDao == null)
			itemDao = getRuntimeExceptionDao(T_CART_ITEM.class);
		return itemDao;
	}

	public RuntimeExceptionDao<T_STORE_INFO, Long> getStoreInfoDao() {
		if (storeInfoDao == null)
			storeInfoDao = getRuntimeExceptionDao(T_STORE_INFO.class);
		return storeInfoDao;
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer,
			int newVer) {
		try { // TODO 버젼업될때 필드및기타 수정처리 필요. drop은 안됨
			TableUtils.dropTable(connectionSource, T_CART_INFO.class, true);
			TableUtils.dropTable(connectionSource, T_CART_ITEM.class, true);
			TableUtils.dropTable(connectionSource, T_STORE_INFO.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.i("DatabaseHelperOrmLite", "onUpgrade Failed") ;
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
}