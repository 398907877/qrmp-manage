package com.yanyan.core.shiro;

import org.apache.shiro.web.util.SavedRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 扩展默认的SavedRequest，增加contentType和scheme://domain:port
 * User: Saintcy
 * Date: 2016/6/6
 * Time: 22:33
 */
public class CustomSavedRequest extends SavedRequest {
    private String contentType;
    private String scheme;
    private String domain;
    private int port;

    /**
     * Constructs a new instance from the given HTTP request.
     *
     * @param request the current request to save.
     */
    public CustomSavedRequest(HttpServletRequest request) {
        super(request);
        this.contentType = request.getContentType();
        this.scheme = request.getScheme();
        this.domain = request.getServerName();
        this.port = request.getServerPort();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "CustomSavedRequest{" +
                "contentType='" + contentType + '\'' +
                ", scheme='" + scheme + '\'' +
                ", domain='" + domain + '\'' +
                ", port=" + port +
                "} " + super.toString();
    }
}
