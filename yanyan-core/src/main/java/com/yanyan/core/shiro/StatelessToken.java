package com.yanyan.core.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 无状态的token
 * User: Saintcy
 * Date: 2017/4/20
 * Time: 14:17
 */
public class StatelessToken implements AuthenticationToken {
    private String username;
    private String digest;
    private boolean clearCache;//是否清理缓存

    public StatelessToken(String username, String digest) {
        this.username = username;
        this.digest = digest;
    }

    public StatelessToken(String username, String digest, boolean clearCache) {
        this.username = username;
        this.digest = digest;
        this.clearCache = clearCache;
    }

    public boolean isClearCache() {
        return clearCache;
    }

    public void setClearCache(boolean clearCache) {
        this.clearCache = clearCache;
    }

    public Object getPrincipal() {
        return username;
    }

    public Object getCredentials() {
        return digest;
    }
}
