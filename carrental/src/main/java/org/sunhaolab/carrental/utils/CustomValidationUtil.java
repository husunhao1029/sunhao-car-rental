package org.sunhaolab.carrental.utils;

import org.sunhaolab.carrental.exception.ServiceException;

import java.util.Date;

public class CustomValidationUtil {

    public static void timeValidation(String startTime, String endTime) {
        long st = 0;
        long et = 0;
        try {
            st = new Long(startTime);
            et = new Long(endTime);
        } catch (Exception e) {
            new ServiceException("Invalid input, please check startTime and endTime are both timestamp format.");
        }


        if (st >= et) {
            throw new ServiceException("Invalid input, start time should be earlier than end time.");
        }

        if (st * 1000L < new Date().getTime()) {
            throw new ServiceException("Invalid input, start time should be later than current time.");
        }
    }

}
