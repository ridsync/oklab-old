/*
 * @(#)NBPNoteDao.java $version 2013. 5. 24.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.okitoki.checklist.database.dao;

import com.okitoki.checklist.database.model.T_CART_ITEM;
import java.util.List;

public interface CartItemDao {
	void insert(T_CART_ITEM TCARTITEM);

	T_CART_ITEM get(long itemid);
	
	void delete(T_CART_ITEM TCARTITEM);
	
	void deleteByCartId(long cartId);

	List<T_CART_ITEM> getAll();

	List<T_CART_ITEM> getByTitleQuery(String query);

	List<T_CART_ITEM> getByCartIdQuery(int cartId, String orderBy);

	List<T_CART_ITEM> getAllFavItemListPaging(int limit, int offset);

	void update(T_CART_ITEM TCARTITEM);
	
	public void close();
}