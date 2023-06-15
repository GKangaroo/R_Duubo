package com.xmu.MyDubbo.framework.loadBalance;

import com.xmu.MyDubbo.framework.URL;

import java.util.List;
import java.util.Random;

/**
 * @author：guiqingxin
 * @date：2023/6/1 9:47
 */
public class WeightPolling implements LoadBalance{

    @Override
    public URL select(List<URL> list) {
        int totalWeight = 0;
        boolean sameWeight = true;
        int size = list.size();
        int[] weights = new int[size];
        for (int i = 0; i < size; i++) {
            int weight = list.get(i).getWeight();
            totalWeight += weight;
            weights[i] = totalWeight;
            if (sameWeight && i > 0 && weight != weights[i - 1]) {
                sameWeight = false;
            }
        }
        Random random = new Random();
        int n = random.nextInt(totalWeight);
        if (!sameWeight) {
            for (int i = 0; i < size; i++) {
                if (n < weights[i]) {
                    return list.get(i);
                }
            }
        }
        return list.get(n);
    }
}
