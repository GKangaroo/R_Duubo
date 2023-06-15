package com.xmu.MyDubbo.common;

/**
 * @author：guiqingxin
 * @date：2023/5/9 10:58
 */
public class PreservedMetadataKeys {
    /**
     * The key to indicate the registry source of service instance, such as Dubbo, SpringCloud, etc.
     */
    public static final String REGISTER_SOURCE = "preserved.register.source";

    public static final String HEART_BEAT_TIMEOUT = "preserved.heart.beat.timeout";

    public static final String IP_DELETE_TIMEOUT = "preserved.ip.delete.timeout";

    public static final String HEART_BEAT_INTERVAL = "preserved.heart.beat.interval";
}
