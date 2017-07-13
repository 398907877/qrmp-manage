package com.yanyan.core.shiro;

import org.apache.shiro.session.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Shiro与应用程序接口
 * User: Saintcy
 * Date: 2016/4/4
 * Time: 16:59
 */
public interface ShiroService {
    /**
     * 根据用户名获取用户的角色编码列表
     *
     * @param username 用户名
     * @return
     */
    Set<String> findStringRoles(String username);

    /**
     * 根据用户名获取用户的权限编码列表，格式： 资源编码:权限编码:实例id
     *
     * @param username 用户名称
     * @return
     */
    Set<String> findStringPermissions(String username);

    /**
     * 查找用户
     *
     * @param username
     * @return
     */
    User findUser(String username);

    /**
     * 验证密码
     *
     * @param inputPassword  用户输入的密码
     * @param salt           存储/数据库的盐，User.salt
     * @param storedPassword 存储/数据库的密码，User.password
     * @return
     */
    boolean validatePassword(String inputPassword, byte[] salt, String storedPassword);

    //************************有状态会话需实现以下接口************************//

    /**
     * 添加会话
     *
     * @return 登录会话信息
     */
    void addSession(Session session);

    /**
     * 更新会话
     *
     * @return 登录会话信息
     */
    void updateSession(Session session);

    /**
     * 删除会话
     *
     * @param session
     * @return
     */
    void deleteSession(Session session);

    /**
     * 获取会话信息
     *
     * @return
     */
    Session getSession(String sessionId);

    //******************* 无状态会话需实现以下接口 ********************//

    /**
     * 从request中获取token信息
     *
     * @param request
     * @return
     */
    StatelessToken getToken(HttpServletRequest request);

    /**
     * 验证token是否合法
     *
     * @param token
     * @return
     */
    boolean validateToken(StatelessToken token);

    /**
     * 更新token
     *
     * @param token
     * @param request
     * @param response
     */
    void refreshToken(StatelessToken token, HttpServletRequest request, HttpServletResponse response);
}
