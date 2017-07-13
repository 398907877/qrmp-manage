package com.yanyan;

import com.yanyan.core.util.PropertiesLoader;
import com.yanyan.core.util.RSAUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.security.KeyPair;

/**
 * 常量
 * User: Saintcy
 * Date: 2015/6/3
 * Time: 16:32
 */
public class Configs {
    //TODO: 需要通过Spring判断当前应用路径
    protected static PropertiesLoader propertiesLoader = new PropertiesLoader("classpath*:config/app.properties");

    public static final String THUMBNAIL_URL_PREFIX  = "/thumbnail";
    public static final String FILE_URL_PREFIX = "/file";
    public static final String REDIRECT_URL_PREFIX = "/redirect";

    public static final String APP_ENV = propertiesLoader.getProperty("app.env");
    public static final boolean LOGIN_CAPTCHA = BooleanUtils.toBoolean(propertiesLoader.getProperty("login.captcha"));
    public static final String APP_PATH = propertiesLoader.getProperty("app.path");
    public static final String BASE_FILE_PATH = propertiesLoader.getProperty("base.file.path");
    public static final String TEMP_FILE_PATH = propertiesLoader.getProperty("temp.file.path");
    public static final String USER_FILE_PATH = propertiesLoader.getProperty("user.file.path");
    public static final String THUMB_FILE_PATH = propertiesLoader.getProperty("thumb.file.path");

    public static final String THUMB_DEFAULT_WIDTH = propertiesLoader.getProperty("thumb.default.width");
    public static final String THUMB_DEFAULT_HEIGHT = propertiesLoader.getProperty("thumb.default.height");

    //TODO: 改成配置加载
    public static final String EMAIL_REGEXP = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";//propertiesLoader.getProperty("email.regexp");
    public static final String MOBILE_REGEXP = "^1[3|5|7|8]{1}[0-9]{9}$";//propertiesLoader.getProperty("mobile.regexp");

    public static final String DEFAULT_PASSWORD = propertiesLoader.getProperty("default.password");

    public static final boolean isProduct() {
        return StringUtils.equalsIgnoreCase(APP_ENV, "product");
    }

    public static final boolean isTest() {
        return StringUtils.equalsIgnoreCase(APP_ENV, "test");
    }

    public static final boolean isDevelop(){
        return StringUtils.equalsIgnoreCase(APP_ENV, "develop");
    }

}
