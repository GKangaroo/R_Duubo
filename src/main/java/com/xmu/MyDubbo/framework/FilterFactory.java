package com.xmu.MyDubbo.framework;

import com.xmu.MyDubbo.framework.filter.Filter;
import com.xmu.MyDubbo.framework.filter.TokenBucket;
import com.xmu.MyDubbo.util.ReadProperties;

public class FilterFactory {
    public static Filter getFilter() {
        String name = ReadProperties.readProperties("filter","TokenBucket");
        StringBuilder fullName = new StringBuilder()
                .append(Filter.class.getPackageName()).append('.').append(name);
        try {
            return (Filter)Class.forName(fullName.toString()).getConstructor().newInstance();
        }
        catch (Exception ignored){

        }
        return new TokenBucket();
    }
}
