package com.okitoki.checklist.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-13.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalcTest {
    @Mock
    private Calc calc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(calc);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSum() throws Exception {

    }

    @Test
    public void testMultiply() throws Exception {
        when(calc.multiply(2, 3)).thenReturn(6);
        assertThat(calc.multiply(2, 3), is(4));
    }
}