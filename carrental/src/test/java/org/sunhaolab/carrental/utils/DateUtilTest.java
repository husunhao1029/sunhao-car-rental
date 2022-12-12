package org.sunhaolab.carrental.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sunhaolab.carrental.exception.ServiceException;

import static org.junit.jupiter.api.Assertions.fail;

class DateUtilTest {

    @Test
    void testStampToDate() {
        String input = "1670428800";
        String res = DateUtil.stampToDate(input);
        Assertions.assertEquals("2022-12-08", res);
    }

    @Test
    void testInvalidInputToDate() {
        String input = "invalid input";
        try {
            String res = DateUtil.stampToDate(input);
            fail("Expected an ServiceException to be thrown");
        } catch (ServiceException e) {
            Assertions.assertTrue(e.getMessage().equals("transForm date failed, input is:" + input));
        }
    }

}