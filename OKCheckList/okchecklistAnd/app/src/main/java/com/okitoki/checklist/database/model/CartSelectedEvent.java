package com.okitoki.checklist.database.model;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-06.
 */
public class CartSelectedEvent {

    public final T_CART_INFO item;

    public CartSelectedEvent (T_CART_INFO item) {
        this.item = item;
    }

    public T_CART_INFO getItem() {
        return item;
    }
}
