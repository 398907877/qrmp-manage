package com.yanyan.core.shiro;

/**
 * 用户和密码（包含验证码）令牌类
 * User: Saintcy
 * Date: 2016/3/24
 * Time: 9:39
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {
    /**
     * TODO:这里的字段会自动加到session里面？
     */
    private String captcha;
    private boolean thirdParty;

    public UsernamePasswordToken() {
        super();
    }

    public UsernamePasswordToken(String username, char[] password,
                                 boolean rememberMe, String host, String captcha, boolean thirdParty) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.thirdParty = thirdParty;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isThirdParty() {
        return thirdParty;
    }
}
