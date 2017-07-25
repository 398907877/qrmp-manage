package com.yanyan.web;

import com.yanyan.core.util.NumberUtils;

import java.util.LinkedHashMap;

/**
 * 使用Token模式时，会话中的内容
 * User: Saintcy
 * Date: 2017/4/21
 * Time: 13:27
 */
public class TokenSession extends LinkedHashMap<String, String> {
    public Long getStaffId() {
        return NumberUtils.toLong(this.get("staffId"));
    }

    public void setStaffId(Long staffId) {
        this.put("staffId", staffId + "");
    }

    public String getAccount() {
        return this.get("account");
    }

    public void setAccount(String account) {
        this.put("account", account + "");
    }

    public Long getCorpId() {
        return NumberUtils.toLong(this.get("corpId"));
    }

    public void setCorpId(Long corpId) {
        this.put("corpId", corpId + "");
    }

    public Long getPortalId() {
        return NumberUtils.toLong(this.get("portalId"));
    }

    public void setPortalId(Long portalId) {
        this.put("portalId", portalId + "");
    }
}
