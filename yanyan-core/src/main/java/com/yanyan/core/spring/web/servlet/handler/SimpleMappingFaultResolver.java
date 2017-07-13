package com.yanyan.core.spring.web.servlet.handler;

import com.yanyan.core.Fault;
import com.yanyan.core.spring.ModelAndViewException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 简单的自定义错误处理器
 * User: Saintcy
 * Date: 2015/4/23
 * Time: 11:39
 */
public class SimpleMappingFaultResolver extends SimpleMappingExceptionResolver {
    private String exceptionAttribute = "fault";

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
                                              Object handler, Exception ex) {
        ModelAndView mav = super.doResolveException(request, response, handler, ex);

        return mav;
    }

    @Override
    protected ModelAndView getModelAndView(String viewName, Exception ex) {
        ModelAndView mv;
        if (ex instanceof ModelAndViewException) {//将模型数据返回
            mv = ((ModelAndViewException) ex).getModelAndView();
        } else {
            mv = new ModelAndView(viewName);
        }

        if (this.exceptionAttribute != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
            }

            Fault fault = new Fault(ex);
            mv.addObject(this.exceptionAttribute, fault);
        }
        return mv;
    }


    @Override
    protected String determineViewName(Exception ex, HttpServletRequest request) {
        String viewName = super.determineViewName(ex, request);//其他错误根据配置

        if (StringUtils.startsWith(viewName, "refresh:")) {//返回到原页面
            if (logger.isDebugEnabled()) {
                logger.debug("Refresh current page");
            }
            int extIndex = StringUtils.lastIndexOf(request.getRequestURI(), ".");
            if (extIndex >= 0) {
                viewName = StringUtils.substring(request.getRequestURI(), StringUtils.length(request.getContextPath()), extIndex);
            } else {
                viewName = StringUtils.substring(request.getRequestURI(), StringUtils.length(request.getContextPath()));
            }
        }

        return viewName;
    }
}
