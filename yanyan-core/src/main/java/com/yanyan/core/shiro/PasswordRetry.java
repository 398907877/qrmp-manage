package com.yanyan.core.shiro;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 密码重试
 * User: Saintcy
 * Date: 2017/5/5
 * Time: 17:57
 */
public class PasswordRetry implements Serializable {
    AtomicInteger retryCount = new AtomicInteger(0);
    long lastRetryTime = System.currentTimeMillis();

    public int incrementAndGet(int lockTime) {
        int count = retryCount.incrementAndGet();
        long now = System.currentTimeMillis();
        if (now - lockTime > lastRetryTime) {//超过锁定时间，解锁
            lastRetryTime = now;
            return 0;
        } else {
            lastRetryTime = now;
            return count;
        }
    }
}
