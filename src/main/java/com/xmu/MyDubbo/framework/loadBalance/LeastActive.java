package com.xmu.MyDubbo.framework.loadBalance;

import com.xmu.MyDubbo.framework.URL;

import java.util.List;
import java.util.Random;

/**
 * @author：guiqingxin
 * @date：2023/6/1 9:52
 */
public class LeastActive implements LoadBalance{

    @Override
    public URL select(List<URL> list) {
        int leastActive = -1;
        int leastCount = 0;
        int[] leastIndexs = new int[list.size()];
        int totalWeight = 0;
        int firstWeight = 0;
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
            if (leastActive == -1 || list.get(i).getActiveCount() < leastActive) {
                leastActive = list.get(i).getActiveCount();
                leastCount = 1;
                leastIndexs[0] = i;
                continue;
            }
            if (list.get(i).getActiveCount() == leastActive) {
                leastIndexs[leastCount++] = i;
            }
        }
        if (leastCount == 1) {
            list.get(leastIndexs[0]).setActiveCount(list.get(leastIndexs[0]).getActiveCount() + 1);
            return list.get(leastIndexs[0]);
        }
        if (!sameWeight && totalWeight > 0) {
            int offsetWeight = new Random().nextInt(totalWeight);
            for (int i = 0; i < leastCount; i++) {
                int leastIndex = leastIndexs[i];
                if (offsetWeight < weights[leastIndex]) {
                    list.get(leastIndex).setActiveCount(list.get(leastIndex).getActiveCount() + 1);
                    return list.get(leastIndex);
                }
            }
        }
        list.get(leastIndexs[0]).setActiveCount(list.get(leastIndexs[0]).getActiveCount() + 1);
        return list.get(leastIndexs[0]);
    }
}
