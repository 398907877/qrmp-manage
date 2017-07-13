package com.yanyan.core.filter;

/**
 * 接口调用客户端服务
 * User: Saintcy
 * Date: 2017/4/19
 * Time: 10:11
 */
public interface ApiClientService {
    /**
     * 根据AppId获取api调用客户端
     *
     * @param appId
     * @return
     */
    ApiClient getClientByAppId(String appId);

    /**
     * 客户端是否有权限
     *
     * @param appId
     * @param permission
     * @return
     */
    boolean hasPermission(String appId, String permission);

    /**
     * 获取客户端id关键字，默认为clientId
     * 如portalId: request.setHeader("如portalId", client.getId());
     * 如merchantId: request.setHeader("merchantId", client.getId());
     * 后续报文拦截器可以读取出来回设进pojo类中
     *
     * @return
     */
    String getClientIdKey();
}
