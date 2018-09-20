package com.caiogallo.guardarecibo.utils;

import java.util.Calendar;

public class Utils {

    public static Integer getMonthOfYear(){
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static Integer getYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

}
