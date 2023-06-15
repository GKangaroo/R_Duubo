package com.xmu.MyDubbo.util;

import java.util.regex.Pattern;

import static com.xmu.MyDubbo.common.Constants.NUMBER_PATTERN_STRING;

/**
 * @author：guiqingxin
 * @date：2023/5/9 10:31
 */
public class NumberUtil {

    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_PATTERN_STRING);
    public static boolean isNumber(String s)
    {
        return !StringUtil.isEmpty(s) && NUMBER_PATTERN.matcher(s).matches();
    }
}
