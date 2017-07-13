package com.yanyan.core.filter;

import com.google.gson.Gson;
import com.yanyan.core.util.GenericsUtils;
import com.yanyan.core.util.HttpUtils;
import com.yanyan.core.util.JwtUtil;
import com.yanyan.core.util.RSAUtils;
import com.yanyan.core.web.DataResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.*;

/**
 * 提供给外部的api拦截器
 * User: Saintcy
 * Date: 2017/4/18
 * Time: 14:50
 */
@Slf4j
public class ApiFilterFactoryBean implements FactoryBean, InitializingBean {
    private Gson gson;
    private ApiClientService clientService;
    private CacheManager cacheManager;
    private String cacheName;
    private boolean readToken;
    private String tokenKey;

    public void setGson(Gson gson) {
        Assert.notNull(gson, "gson can't be null");
        this.gson = gson;
    }

    public void setClientService(ApiClientService clientService) {
        Assert.notNull(clientService, "clientService can't be null");
        this.clientService = clientService;
    }

    public void setCacheManager(CacheManager cacheManager) {
        Assert.notNull(cacheManager, "clientService can't be null");
        this.cacheManager = cacheManager;
    }

    public void setCacheName(String cacheName) {
        Assert.notNull(cacheName, "cacheName can't be null");
        this.cacheName = cacheName;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public void setReadToken(boolean readToken) {
        this.readToken = readToken;
    }

    public Object getObject() throws Exception {
        return new ApiFilter(gson, clientService, cacheManager, cacheName, readToken, tokenKey);
    }

    public Class<?> getObjectType() {
        return ApiFilter.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(gson, "gson can't be null");
        Assert.notNull(clientService, "clientService can't be null");
        Assert.notNull(cacheManager, "clientService can't be null");
        Assert.notNull(cacheName, "cacheName can't be null");
        if (readToken) {
            Assert.notNull(tokenKey, "tokenKey can't be null");
        }
    }

    @Slf4j
    private static final class ApiFilter extends OncePerRequestFilter {
        private Gson gson;
        private ApiClientService clientService;
        private CacheManager cacheManager;
        private String cacheName;
        private boolean readToken;
        private String tokenKey;


        public ApiFilter(Gson gson, ApiClientService clientService, CacheManager cacheManager, String cacheName, boolean readToken, String tokenKey) {
            this.clientService = clientService;
            this.gson = gson;
            this.cacheManager = cacheManager;
            this.readToken = readToken;
            this.tokenKey = tokenKey;
            this.cacheName = cacheName;
        }

        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
            RequestWrapper requestWrapper = new RequestWrapper(request);
            ResponseWrapper responseWrapper = new ResponseWrapper(response);

            Model error = verifyRequest(requestWrapper);
            if (error == null) {
                try {
                    chain.doFilter(requestWrapper, responseWrapper);
                } catch (Throwable t) {
                    log.error("Call Service error", t);
                    error = DataResponse.failure(t);
                }
            }

            signResponse(requestWrapper, responseWrapper, error);
        }

        private Model verifyRequest(RequestWrapper request) throws IOException {
            String remote = request.getRemoteHost();
            String appid = request.getHeader("appid");
            ApiClient client = getClientByAppId(appid);
            String sequence = request.getHeader("sequence");

            log.debug("[{}]Host {} call service: {}", sequence, remote, client);

            if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(client.getAppSecret())) {
                return DataResponse.failure(DataResponse.ERRCODE_INVALID_APPID, DataResponse.ERRMSG_INVALID_APPID);
            }

            String signature = request.getHeader("signature");
            String encoding = HttpUtils.getEncoding(request, "utf-8");
            String message = new String(request.getDataStream(), encoding);
            log.debug("Incoming message:\n{}", message);
            log.debug("encrypted request signature=>{}", signature);
            boolean verify = false;
            try {
                String decryptedSignature = RSAUtils.decryptByPrivateKey(signature, client.getPrivateSecret());
                log.debug("decrypted request signature=>{}", decryptedSignature);
                String md5HexMessage = DigestUtils.md5Hex(message.getBytes("utf-8"));
                log.debug("md5 hex request message=>{}", md5HexMessage);
                if (StringUtils.equalsIgnoreCase(decryptedSignature, md5HexMessage)) {
                    verify = true;
                }
            } catch (Exception e) {
                log.warn("Can't decrypt signature: {}, appid={}", signature, appid, e);
            }
            if (!verify) {
                return DataResponse.failure(DataResponse.ERRCODE_SIGNATURE_VERIFY_FAIL, DataResponse.ERRMSG_SIGNATURE_VERIFY_FAIL);
            }

            //读取Token中的信息
            if (readToken) {
                String token = request.getHeader("token");
                if (StringUtils.isNotEmpty(token)) {//如果有token，解析token
                    try {
                        //TODO: 后续需要改为注入方式，注入token解析器
                        Claims claims = JwtUtil.parseJWT(gson, tokenKey, token);
                        Map<String, String> session = gson.fromJson(claims.getSubject(), GenericsUtils.getMapType(String.class, String.class, Map.class));
                        if (session != null) {
                            for (Map.Entry<String, String> entry : session.entrySet()) {
                                request.setHeader(entry.getKey(), entry.getValue());
                            }
                        }
                    } catch (Exception e) {
                        //忽略错误，如果token不正确shiro验证时会抛出异常
                        log.warn("parse token error", e);
                    }
                }
            }

            //读取并解码随机数
            try {
                String random = request.getHeader("random");
                if (StringUtils.isNotEmpty(random)) {
                    log.debug("encrypted request random=>{}", random);
                    String decryptedRandom = RSAUtils.decryptByPrivateKey(random, client.getPrivateSecret());
                    log.debug("decrypted request random=>{}", decryptedRandom);
                    request.setHeader("random", decryptedRandom);
                }
            } catch (Exception e) {
                return DataResponse.failure("random解密失败");
            }
            //当前调用者id设进来，方便后续程序读取
            request.setHeader(StringUtils.defaultIfEmpty(clientService.getClientIdKey(), "clientId"), client.getId() + "");

            return null;
        }

        private void signResponse(RequestWrapper request, ResponseWrapper response, Model error) throws IOException {
            String appid = request.getHeader("AppId");
            ApiClient client = getClientByAppId(appid);
            String sequence = request.getHeader("Sequence");
            response.setHeader("Sequence", sequence);
            response.setHeader("Content-Type", request.getHeader("Content-Type"));
            String message;
            if (error == null) {
                if (response.getStatus() == 404) {//没有找到服务，服务地址错误或者服务请求的格式错误，如没有要求返回json
                    response.setStatus(200);
                    response.resetBuffer();
                    message = gson.toJson(DataResponse.failure(DataResponse.ERRCODE_SERVICE_NOT_EXIST, DataResponse.ERRMSG_SERVICE_NOT_EXIST));
                } else if (response.getStatus() == 401) {//验证token失败，由shiro进行验证
                    response.setStatus(200);
                    response.resetBuffer();
                    message = gson.toJson(DataResponse.failure(DataResponse.ERRCODE_TOKEN_VERIFY_FAIL, DataResponse.ERRMSG_TOKEN_VERIFY_FAIL));
                } else if (response.getStatus() == 500) {//其他没有拦截到的错误
                    response.setStatus(200);
                    response.resetBuffer();
                    message = gson.toJson(DataResponse.failure(DataResponse.ERRCODE_UNDEFINED, DataResponse.ERRMSG_UNDEFINED));
                } else {
                    message = new String(response.getDataStream());
                }
            } else {
                message = gson.toJson(error);
            }

            log.debug("Outgoing message:{}", message);
            String signature = DigestUtils.md5Hex(message.getBytes("utf-8"));
            log.debug("md5 hex response message=>{}", signature);
            try {
                signature = RSAUtils.encryptByPrivateKey(signature, client.getPrivateSecret());
                response.setHeader("signature", signature);
                log.debug("encrypted response signature=>{}", signature);
            } catch (Exception e) {
                throw new RuntimeException("sign response error, appid=" + appid, e);
            }

            response.getWriter().write(message);
        }

        private ApiClient getClientByAppId(String appId) {
            Cache cache = cacheManager.getCache(cacheName);
            ApiClient user = cache.get(appId, ApiClient.class);
            if (user == null) {
                user = clientService.getClientByAppId(appId);
                if (user != null) {
                    cache.put(appId, user);
                }
            }
            return user;
        }


        private class ListEnumeration<T> implements Enumeration<T> {
            private int current;
            private List<T> list;

            public ListEnumeration(List<T> list) {
                this.list = list;
            }

            public boolean hasMoreElements() {
                return current < list.size();
            }

            public T nextElement() {
                return list.get(current++);
            }
        }

        private class FilterServletInputStream extends ServletInputStream {
            private DataInputStream inputStream;

            public FilterServletInputStream(InputStream inputStream) {
                this.inputStream = new DataInputStream(inputStream);
            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            //兼容Servlet API 3.0
            public boolean isFinished() {
                return false;
            }

            public boolean isReady() {
                return false;
            }

            public void setReadListener(ReadListener arg0) {

            }
        }


        private class RequestWrapper extends HttpServletRequestWrapper {
            private byte[] data;
            private ByteArrayInputStream input;
            private FilterServletInputStream inputStream;
            private Map<String, String> headers;

            public RequestWrapper(HttpServletRequest request) throws IOException {
                super(request);
                data = IOUtils.toByteArray(request.getInputStream());
                input = new ByteArrayInputStream(data);
                headers = new HashMap<String, String>();
            }

            @Override
            public ServletInputStream getInputStream() throws IOException {
                if (inputStream == null) {
                    inputStream = new FilterServletInputStream(input);
                }
                return this.inputStream;
            }

            public void setHeader(String name, String value) {
                headers.put(name.toLowerCase(), value);
            }

            public String getHeader(String name) {
                String value = headers.get(name.toLowerCase());
                if (value == null) {
                    return super.getHeader(name);
                }
                return value;
            }

            public Enumeration<String> getHeaders(String name) {
                String value = getHeader(name);
                if (StringUtils.isEmpty(value)) {
                    return super.getHeaders(name);
                } else {
                    return new ListEnumeration<String>(Collections.singletonList(value));
                }
            }

            public Enumeration<String> getHeaderNames() {
                List<String> list = new ArrayList<String>();

                for (String key : headers.keySet()) {
                    list.add(key);
                }

                Enumeration<String> enumeration = super.getHeaderNames();
                while (enumeration.hasMoreElements()) {
                    String name = enumeration.nextElement();
                    if (!headers.containsKey(name.toLowerCase())) {
                        list.add(name);
                    }
                }
                return new ListEnumeration<String>(list);
            }

            public byte[] getDataStream() {
                return data;
            }
        }

        public class FilterServletOutputStream extends ServletOutputStream {
            DataOutputStream output;

            public FilterServletOutputStream(OutputStream output) {
                this.output = new DataOutputStream(output);
            }

            @Override
            public void write(int arg0) throws IOException {
                output.write(arg0);
            }

            //兼容Servlet API 3.0
            public boolean isReady() {
                return false;
            }

            public void setWriteListener(WriteListener arg0) {

            }
        }


        public class ResponseWrapper extends HttpServletResponseWrapper {
            ByteArrayOutputStream output;
            FilterServletOutputStream filterOutput;
            int status = 200;//总是输出200

            public ResponseWrapper(HttpServletResponse response) {
                super(response);
                output = new ByteArrayOutputStream();
                super.setStatus(status);
            }

            @Override
            public void resetBuffer() {
                if (!super.isCommitted()) {
                    super.resetBuffer();
                }
                output = new ByteArrayOutputStream();
                filterOutput = new FilterServletOutputStream(output);
            }

            @Override
            public void setStatus(int sc) {
                //禁止设定status，避免重定向到网页
                //super.setStatus(sc);
                this.status = sc;
            }

            @Override
            public int getStatus() {
                return status;
            }

            @Override
            public void sendError(int sc) throws IOException {
                //禁止重定向
                setStatus(sc);
                //super.sendError(sc);
            }

            @Override
            public void sendError(int sc, String msg) throws IOException {
                //禁止重定向
                setStatus(sc);
                //super.sendError(sc, msg);
            }

            @Override
            public void sendRedirect(String location) throws IOException {
                //禁止重定向
                //setStatus();
                //super.sendRedirect(location);
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                if (filterOutput == null) {
                    filterOutput = new FilterServletOutputStream(output);
                }
                return filterOutput;
            }

            public byte[] getDataStream() {
                return output.toByteArray();
            }
        }
    }
}
