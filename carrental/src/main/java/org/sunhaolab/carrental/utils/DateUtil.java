package org.sunhaolab.carrental.utils;

import org.sunhaolab.carrental.exception.ServiceException;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {

    /**
     * timestamp to date
     */
    public static String stampToDate(String timestamp) {
        String res;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long timestampLong = new Long(timestamp);
            Date date = new Date(timestampLong * 1000L);
            res = sdf.format(date);
        } catch (Exception e) {
            throw new ServiceException("transForm date failed, input is: '" + timestamp + "'");
        }

        return res;
    }
}
