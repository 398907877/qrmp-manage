package com.yanyan.core.shiro;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 自定义shiro验证时使用的加密算法
 * User: Saintcy
 * Date: 2016/2/23
 * Time: 17:35
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    private ShiroService shiroService;

    public CustomCredentialsMatcher(ShiroService shiroService) {
        this.shiroService = shiroService;
    }

    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        if (authcToken instanceof org.apache.shiro.authc.UsernamePasswordToken) {
            org.apache.shiro.authc.UsernamePasswordToken token = (org.apache.shiro.authc.UsernamePasswordToken) authcToken;
            Object accountCredentials = getCredentials(info); // 数据库密码
            byte[] salt = null;
            if (info instanceof SimpleAuthenticationInfo) {
                salt = ((SimpleAuthenticationInfo) info).getCredentialsSalt().getBytes();
            }
            if(token instanceof UsernamePasswordToken) {
                if (((UsernamePasswordToken) token).isThirdParty()) { // 微信和第三方登录自动登录
                    return true;
                }
            }
            //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
            //return super.doCredentialsMatch(authcToken, info);
            return shiroService.validatePassword(String.valueOf(token.getPassword()), salt, accountCredentials + "");
        } else if (authcToken instanceof StatelessToken) {
            return shiroService.validateToken((StatelessToken) authcToken);
        } else {
            throw new NotImplementedException("Not Implemented AuthenticationToken Credentials Match of " + authcToken);
        }
    }
}
