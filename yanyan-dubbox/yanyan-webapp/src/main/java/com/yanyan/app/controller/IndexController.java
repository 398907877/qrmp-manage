package com.yanyan.app.controller;

import com.google.gson.Gson;
import com.yanyan.core.BusinessException;
import com.yanyan.core.shiro.StatelessToken;
import com.yanyan.core.util.AESUtil;
import com.yanyan.core.util.JwtUtil;
import com.yanyan.core.util.PasswordUtils;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.vo.LoginModel;
import com.yanyan.service.system.StaffService;
import com.yanyan.web.TokenSession;
import com.yanyan.core.web.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * app系统首页，只是用于样例展示，并没有配置扫描
 * User: Saintcy
 * Date: 2017/4/22
 * Time: 0:03
 */
@Slf4j
//@Controller
//@RequestMapping("/app")
public class IndexController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private Gson gson;
    @Value("${token.signature.key}")
    private String tokenKey;
    @Value("${token.maxAge}")
    private Long tokenMaxAge;

    @RequestMapping("/login")
    @ResponseBody
    public Model login(@RequestBody LoginModel login, @RequestHeader("random") String random) {
        Subject subject = SecurityUtils.getSubject();
        try {
            Staff staff = staffService.getStaffByAccount(login.getUsername());
            if (staff == null) {
                throw new UnknownAccountException();
            } else if (staff.getIs_lock() == 1) {
                throw new LockedAccountException();
            }
            //需要根据随机码使用AES方式解密密码
            String plainPassword = AESUtil.decryptFromHex(login.getPassword(), random);
            if (PasswordUtils.validatePassword(plainPassword, staff.getSalt().getBytes(), staff.getPassword())) {

                //无状态的会话信息，这些字段放入jwt的subject中，后续客户端传上来时，由过滤器解析并放入header中，字段名称与TokenSession对象字段一致
                TokenSession session = new TokenSession();
                session.setAccount(staff.getAccount());
                session.setCorpId(staff.getCorp_id());
                session.setPortalId(staff.getPortal_id());
                session.setStaffId(staff.getId());
                String jwtSubject = gson.toJson(session);
                //生成jwt形式的摘要信息
                String digest = JwtUtil.createJWT(gson, tokenKey, UUID.randomUUID().toString(), jwtSubject, tokenMaxAge);
                StatelessToken statelessToken = new StatelessToken(login.getUsername(), digest);
                //调用shiro登录
                subject.login(statelessToken);
                //TODO: 登录成功后是否需要记录日志？

                return DataResponse.success("token", digest);//返回给客户端
            } else {
                throw new UnknownAccountException();
            }
        } catch (LockedAccountException e) {
            throw new BusinessException("login.account.locked", "该用户禁止登陆");
        } catch (UnknownAccountException e) {
            throw new BusinessException("login.account.unknown", "用户名/密码错误");
        } catch (IncorrectCredentialsException e) {
            throw new BusinessException("login.credentials.incorrect", "用户名/密码错误");
        } catch (ExcessiveAttemptsException e) {
            throw new BusinessException("login.attempts.excessive", "密码重试超出系统限制，请在" + (NumberUtils.toInt(e.getMessage()) / 60) + "分钟后重试。");
        } catch (Exception e) {
        	
        
            //其他错误，比如锁定，如果想单独处理请单独catch 处理
            log.error("", e);
            throw new BusinessException("login.error", "其他错误:" + e.getMessage());
        }
    }

}
