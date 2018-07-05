/*
 * @(#)Transaction.java $version 2013. 6. 15.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.transaction;

public interface Transaction {
	public void begin();
	public void commit();
	public void rollback();
	public void end();
}
