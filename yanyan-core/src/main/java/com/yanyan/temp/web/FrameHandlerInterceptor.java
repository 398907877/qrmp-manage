package com.yanyan.temp.web;

import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 控制层拦截器
 * User: Saintcy
 * Date: 2015/3/3
 * Time: 14:24
 */
public class FrameHandlerInterceptor extends HandlerInterceptorAdapter {
    //    private String tokenKey = "token";//令牌关键字
//    private List<String> freeUrlPatterns;//自由访问的页面列表
//    private boolean useSuffixPatternMatch = true;
//    private boolean useRegisteredSuffixPatternMatch = false;
//    private boolean useTrailingSlashMatch = true;
//    private List<MediaType> pageMediaTypes;//属于页面的媒体类型
//    private UrlPathHelper urlPathHelper;
//    private PathMatcher pathMatcher;
//    private ServletContext servletContext;
//    private StringValueResolver embeddedValueResolver;
//    private List<String> fileExtensions;
//    private ContentNegotiationManager contentNegotiationManager;
//    private final ContentNegotiationManagerFactoryBean cnManagerFactoryBean = new ContentNegotiationManagerFactoryBean();

    /**
     * Set the {@link ContentNegotiationManager} to use to determine requested media types.
     * <p>If not set, ContentNegotiationManager's default constructor will be used,
     * applying a {@link org.springframework.web.accept.HeaderContentNegotiationStrategy}.
     *
     * @see ContentNegotiationManager#ContentNegotiationManager()
     */
    /*public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager;
    }



    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        Assert.notNull(errorsKey, "tokenKey can't not be null");
        this.tokenKey = tokenKey;
    }

    public UrlPathHelper getUrlPathHelper() {
        return urlPathHelper;
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public List<String> getFreeUrlPatterns() {
        return freeUrlPatterns;
    }

    public void setFreeUrlPatterns(List<String> freeUrlPatterns) {
        this.freeUrlPatterns = freeUrlPatterns;
    }

    public boolean isUseSuffixPatternMatch() {
        return useSuffixPatternMatch;
    }

    public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch) {
        this.useSuffixPatternMatch = useSuffixPatternMatch;
    }

    public boolean isUseRegisteredSuffixPatternMatch() {
        return useRegisteredSuffixPatternMatch;
    }

    public void setUseRegisteredSuffixPatternMatch(boolean useRegisteredSuffixPatternMatch) {
        this.useRegisteredSuffixPatternMatch = useRegisteredSuffixPatternMatch;
    }

    public boolean isUseTrailingSlashMatch() {
        return useTrailingSlashMatch;
    }

    public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
        this.useTrailingSlashMatch = useTrailingSlashMatch;
    }

     public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.cnManagerFactoryBean.setServletContext(servletContext);
    }

    public void setEmbeddedValueResolver(StringValueResolver embeddedValueResolver) {
        this.embeddedValueResolver = embeddedValueResolver;
    }

    public void setPageMediaTypes(List<MediaType> pageMediaTypes) {
        this.pageMediaTypes = pageMediaTypes;
    }

        public void afterPropertiesSet() {
        if (pageMediaTypes == null) {
            pageMediaTypes = new ArrayList<MediaType>();
            pageMediaTypes.add(MediaType.TEXT_HTML);//默认html才是页面
        }
        if (this.contentNegotiationManager == null) {
            this.cnManagerFactoryBean.afterPropertiesSet();
            this.contentNegotiationManager = this.cnManagerFactoryBean.getObject();
        }

        if (freeUrlPatterns != null) {
            freeUrlPatterns = resolveEmbeddedValuesInPatterns(freeUrlPatterns);
        }

        if (fileExtensions == null) {
            fileExtensions = getFileExtensions();
        }
        for (String fileExtension : fileExtensions) {
            if (fileExtension.charAt(0) != '.') {
                fileExtension = "." + fileExtension;
            }
            this.fileExtensions.add(fileExtension);
        }
    }

      private String getMatchingPattern(List<String> patterns, String lookupPath) {
        if (patterns != null) {
            for (String pattern : patterns) {
                if (pattern.equals(lookupPath)) {
                    return pattern;
                }
                if (this.useSuffixPatternMatch) {
                    if (!this.fileExtensions.isEmpty() && lookupPath.indexOf('.') != -1) {
                        for (String extension : this.fileExtensions) {
                            if (this.pathMatcher.match(pattern + extension, lookupPath)) {
                                return pattern + extension;
                            }
                        }
                    } else {
                        boolean hasSuffix = pattern.indexOf('.') != -1;
                        if (!hasSuffix && this.pathMatcher.match(pattern + ".*", lookupPath)) {
                            return pattern + ".*";
                        }
                    }
                }
                if (this.pathMatcher.match(pattern, lookupPath)) {
                    return pattern;
                }
                if (this.useTrailingSlashMatch) {
                    if (!pattern.endsWith("/") && this.pathMatcher.match(pattern + "/", lookupPath)) {
                        return pattern + "/";
                    }
                }
            }
        }
        return null;
    }


    public List<String> getFileExtensions() {
        if (isUseRegisteredSuffixPatternMatch() && this.contentNegotiationManager != null) {
            return this.contentNegotiationManager.getAllFileExtensions();
        }
        return new ArrayList<String>();
    }

    protected List<String> resolveEmbeddedValuesInPatterns(List<String> patterns) {
        if (this.embeddedValueResolver == null) {
            return patterns;
        } else {
            List<String> resolvedPatterns = new ArrayList<String>();
            for (int i = 0; i < patterns.size(); i++) {
                resolvedPatterns.add(this.embeddedValueResolver.resolveStringValue(patterns.get(i)));
            }
            return resolvedPatterns;
        }
    }

     protected List<MediaType> getRequestedMediaTypes(HttpServletRequest request) {
        try {
            ServletWebRequest webRequest = new ServletWebRequest(request);

            List<MediaType> acceptableMediaTypes = this.contentNegotiationManager.resolveMediaTypes(webRequest);
            acceptableMediaTypes = acceptableMediaTypes.isEmpty() ?
                    Collections.singletonList(MediaType.ALL) : acceptableMediaTypes;

            List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request);
            Set<MediaType> compatibleMediaTypes = new LinkedHashSet<MediaType>();
            for (MediaType acceptable : acceptableMediaTypes) {
                for (MediaType producible : producibleMediaTypes) {
                    if (acceptable.isCompatibleWith(producible)) {
                        compatibleMediaTypes.add(getMostSpecificMediaType(acceptable, producible));
                    }
                }
            }
            List<MediaType> selectedMediaTypes = new ArrayList<MediaType>(compatibleMediaTypes);
            MediaType.sortBySpecificityAndQuality(selectedMediaTypes);
            if (logger.isDebugEnabled()) {
                logger.debug("Requested media types are " + selectedMediaTypes + " based on Accept header types " +
                        "and producible media types " + producibleMediaTypes + ")");
            }
            return selectedMediaTypes;
        } catch (HttpMediaTypeNotAcceptableException ex) {
            return null;
        }
    }



    */

    /**
     * 判断是否登录
     *
     * @param token
     * @return
     */
    private boolean isLoggedOn(String token) {
        //todo
        return true;
    }

    /**
     * 判断是否有访问权限
     *
     * @param token
     * @param rightId 权限集合
     * @return 只要有其中一个权限则返回true
     */
    private boolean hasRight(String token, String[] rightId) {
        //todo
        return true;
    }

    /**
     * Return the more specific of the acceptable and the producible media types
     * with the q-value of the former.
     */
    /*private MediaType getMostSpecificMediaType(MediaType acceptType, MediaType produceType) {
        produceType = produceType.copyQualityValue(acceptType);
        return MediaType.SPECIFICITY_COMPARATOR.compare(acceptType, produceType) < 0 ? acceptType : produceType;
    }*/

    /**
     * 判断返回的是否是页面，代码参考自{@link org.springframework.web.servlet.view.ContentNegotiatingViewResolver}
     *
     * @param request
     * @return
     */
    /*protected boolean isReturnPageView(HttpServletRequest request) {
        List<MediaType> requestedMediaTypes = getRequestedMediaTypes(request);

        for (MediaType requestedMediaType : requestedMediaTypes) {
            for (MediaType pageMediaType : pageMediaTypes) {
                if (requestedMediaType.isCompatibleWith(pageMediaType)) {
                    return true;
                }
            }
        }

        return false;
    }*/
    /**
     * 分别实现预处理、后处理（调用了Service并返回ModelAndView，但未进行页面渲染）、返回处理（已经渲染了页面）
     * 在preHandle中，可以进行编码、安全控制等处理；
     * 在postHandle中，有机会修改ModelAndView；
     * 在afterCompletion中，可以根据ex是否为null判断是否发生了异常，进行日志记录。
     */

    /*private void checkCredentials(HttpServletRequest request, HandlerMethod handler) {
        Auth auth = handler.getMethodAnnotation(Auth.class);
        Auth.Level level = Auth.Level.LOGIN;//默认需要权限
        if (auth == null) {//没定义权限，则只能有两种情况，需要登录，和不需要登录(因为需要权限，则必然会定义权限ID)
            if (urlPathHelper == null || pathMatcher == null) {
                ApplicationContext context = WebApplicationContextUtils.findWebApplicationContext(servletContext);
                if (urlPathHelper == null) {
                    urlPathHelper = context.getBean(UrlPathHelper.class);
                }
                if (urlPathHelper == null) {
                    urlPathHelper = new UrlPathHelper();
                }

                if (pathMatcher == null) {
                    pathMatcher = context.getBean(PathMatcher.class);
                }
                if (pathMatcher == null) {
                    pathMatcher = new AntPathMatcher();
                }
            }
            String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
            if (getMatchingPattern(freeUrlPatterns, lookupPath) != null) {//自由访问
                level = Auth.Level.NONE;
            }
        } else {//注解优先
            level = auth.value();
        }

        if (level != Auth.Level.NONE) {//需要认证
            String token = StringUtils.trimToEmpty(request.getParameter(tokenKey));
            if (level == Auth.Level.LOGIN) {
                if (!(StringUtils.isNotEmpty(token) && isLoggedOn(token))) {
                    throw new NotLoggedOnException();
                }
            } else {
                String[] rightId = auth.id();
                if (rightId == null || rightId.length == 0) {
                    Method method = handler.getMethod();
                    rightId = new String[]{handler.getBeanType().getName() + "." + method.getName()};
                }
                if (!(StringUtils.isNotEmpty(token) && isLoggedOn(token) && hasRight(token, rightId))) {
                    throw new NotAuthorizedException();
                }
            }
        }
    }*/
}
