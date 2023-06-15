package com.xmu.MyDubbo.framework.loadBalance;

import com.xmu.MyDubbo.framework.URL;

import java.util.List;
import java.util.Random;

/**
 * @author：guiqingxin
 * @date：2023/6/1 9:45
 */
public class RandomSelect implements LoadBalance {

    @Override
    public URL select(List<URL> list) {
        Random random = new Random();
        int n = random.nextInt(list.size());
        return list.get(n);
    }
}
