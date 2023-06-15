package com.xmu.MyDubbo.framework.filter;

import com.xmu.MyDubbo.util.ReadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class TokenBucket implements Filter{
    private static final Logger logger = LoggerFactory.getLogger(TokenBucket.class);
    private final int capacity;
    private final long rate;
    private AtomicInteger tokens;
    private AtomicLong lastTimestamp;
    private boolean flag;

    public TokenBucket() {
        this.flag = false;
        this.capacity = max(1,
                Integer.parseInt(ReadProperties.readProperties("tokenBucket.capacity","100")));
        this.rate = max(1,
                Integer.parseInt(ReadProperties.readProperties("tokenBucket.rate","100")));
        this.tokens = new AtomicInteger(capacity);
        this.lastTimestamp = new AtomicLong(System.currentTimeMillis());
    }

    @Override
    public boolean isAllowed() {
        refill();
        int value = tokens.get();
        boolean flag = false;
        while (value > 0 && !flag) {
            flag = tokens.compareAndSet(value, value-1);
            value = tokens.get();
        }
        return flag;
    }

    private void refill() {
        long last = lastTimestamp.get();
        long now = 0;
        boolean flag = false;
        while (!flag) {
            now = System.currentTimeMillis();
            flag = lastTimestamp.compareAndSet(last, now);
        }
        long elapsedMs = now - last;
        int addedTokens = min((int) (elapsedMs * rate / 1000), capacity);
        tokens.addAndGet(addedTokens);
    }
}
