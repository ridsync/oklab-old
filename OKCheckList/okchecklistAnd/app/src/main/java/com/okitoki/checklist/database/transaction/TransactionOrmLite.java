package com.okitoki.checklist.database.transaction;

import java.sql.SQLException;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTable;
import com.okitoki.checklist.database.DatabaseHelperOrmLite;

public class TransactionOrmLite implements Transaction {
	DatabaseHelperOrmLite databaseHelper;
	RuntimeExceptionDao<DaoForTransaction, Long> dao;
	
	public void init(DatabaseHelperOrmLite helper) {
		dao = helper.getRuntimeExceptionDao(DaoForTransaction.class);
	}
	
	@Override
	public void begin() {
		try {
			ConnectionSource connectionSource = dao.getConnectionSource();
			DatabaseConnection databaseConnection = connectionSource.getReadWriteConnection();
			dao.setAutoCommit(databaseConnection,false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void commit() {
		try {
			ConnectionSource connectionSource = dao.getConnectionSource();
			DatabaseConnection databaseConnection = connectionSource.getReadWriteConnection();
			dao.commit(databaseConnection);
			connectionSource.releaseConnection(databaseConnection);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void rollback() {
		try {
			ConnectionSource connectionSource = dao.getConnectionSource();
			DatabaseConnection databaseConnection = connectionSource.getReadWriteConnection();
			dao.rollBack(databaseConnection);
			connectionSource.releaseConnection(databaseConnection);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void end() {
		try {
			ConnectionSource connectionSource = dao.getConnectionSource();
			DatabaseConnection databaseConnection = connectionSource.getReadWriteConnection();
			dao.setAutoCommit(databaseConnection,true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@DatabaseTable
	private static class DaoForTransaction {
		@DatabaseField
		private Long id;
	}
}
