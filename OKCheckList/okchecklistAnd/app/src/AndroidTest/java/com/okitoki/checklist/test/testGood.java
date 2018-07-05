package com.okitoki.checklist.test;

import android.test.AndroidTestCase;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-13.
 */
public class testGood extends AndroidTestCase {
    public testGood(String name) {
        super();
    }

    //테스트할 메소드
    public void testAdd() {

        assertEquals(2, calculateSomeThing(2, 4));

    }

    public int calculateSomeThing(int A, int B) {

        return 2;

    }
}
