package com.yanyan.core.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 密码重试次数限制
 * User: Saintcy
 * Date: 2016/2/23
 * Time: 17:35
 */
public class RetryLimitHashedCredentialsMatcher extends SimpleCredentialsMatcher implements InitializingBean {

    private int lockTime = 600;//默认锁定10分钟
    private int retryLimit = 5;//默认最多重试5次
    private CacheManager cacheManager;
    private String cacheName;
    private Cache<String, PasswordRetry> passwordRetryCache;
    private CredentialsMatcher delegate;

    public RetryLimitHashedCredentialsMatcher() {

    }

    public CredentialsMatcher getDelegate() {
        return delegate;
    }

    public void setDelegate(CredentialsMatcher delegate) {
        this.delegate = delegate;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public int getRetryLimit() {
        return retryLimit;
    }

    public void setRetryLimit(int retryLimit) {
        this.retryLimit = retryLimit;
    }

    public int getLockTime() {
        return lockTime;
    }

    public void setLockTime(int lockTime) {
        this.lockTime = lockTime;
    }

    public void afterPropertiesSet() throws Exception {
        passwordRetryCache = cacheManager.getCache(cacheName);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        //retry count + 1
        PasswordRetry retry = passwordRetryCache.get(username);
        if (retry == null) {
            retry = new PasswordRetry();
            passwordRetryCache.put(username, retry);
        }
        if (retry.incrementAndGet(lockTime) > retryLimit) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException(lockTime + "");
        }

        boolean matches;
        if (delegate != null) {//交给代理类认证
            matches = delegate.doCredentialsMatch(token, info);
        } else {
            matches = super.doCredentialsMatch(token, info);
        }
        if (matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
