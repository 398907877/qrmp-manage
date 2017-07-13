package com.yanyan.core.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;

import javax.servlet.ServletRequest;

/**
 * 自定义的主体工厂，根据有/无状态判断是否需要创建会话
 * User: Saintcy
 * Date: 2017/4/20
 * Time: 10:13
 */
public class CustomSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context) {
        //首次通过SecurityUtils.getSubject()获取subject的时候token还未存在，只有Request/Response信息
        //当调用subject.login的时候会重新建立真正的subject，但此subject中没有Request/Response信息
        if (context.getAuthenticationToken() != null) {
            //无状态的时候不创建session
            if (context.getAuthenticationToken() instanceof StatelessToken) {
                context.setSessionCreationEnabled(false);
                Subject subject = context.getSubject();//获取SecurityUtils.getSubject()得到的subject
                ServletRequest request = ((WebDelegatingSubject) subject).getServletRequest();//((WebSubjectContext) context).getServletRequest();
                request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, false);//设为不创建session
            } else {
                context.setSessionCreationEnabled(true);
            }
        }

        return super.createSubject(context);
    }
}
