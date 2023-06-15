package com.xmu.MyDubbo.common;

import java.util.concurrent.TimeUnit;

/**
 * @author：guiqingxin
 * @date：2023/5/9 10:40
 */
public class Constants {
    public static final String NUMBER_PATTERN_STRING = "^\\d+$";//数字串

    public static final long DEFAULT_HEART_BEAT_TIMEOUT = TimeUnit.SECONDS.toMillis(15);//服务默认心跳超时时间

    public static final long DEFAULT_IP_DELETE_TIMEOUT = TimeUnit.SECONDS.toMillis(30);//默认心跳超时多少秒删除服务

    public static final long DEFAULT_HEART_BEAT_INTERVAL = TimeUnit.SECONDS.toMillis(5);//心跳几秒一次
}
