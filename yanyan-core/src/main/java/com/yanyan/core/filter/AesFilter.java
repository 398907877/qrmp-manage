package com.yanyan.core.filter;

import com.yanyan.core.util.AESUtil;
import com.yanyan.core.util.HttpUtils;
import com.yanyan.core.util.NumberUtils;
import com.yanyan.core.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.security.interfaces.RSAPrivateKey;

/**
 * AES加密报文的拦截器
 * User: Saintcy
 * Date: 2017/2/14
 * Time: 11:07
 */
@Slf4j
public class AesFilter implements Filter {
    private RSAPrivateKey rsaPrivateKey;

    public void init(FilterConfig filterConfig) throws ServletException {
        String modulus = StringUtils.trimToNull(filterConfig.getInitParameter("modulus"));
        Assert.notNull(modulus, "modulus can't not be null");
        String privateExponent = StringUtils.trimToNull(filterConfig.getInitParameter("privateExponent"));
        Assert.notNull(privateExponent, "privateExponent can't not be null");
        this.rsaPrivateKey = RSAUtil.generateRSAPrivateKeyFromHex(modulus, privateExponent);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);

        chain.doFilter(decryptRequest((HttpServletRequest) request), responseWrapper);


        encryptResponse(responseWrapper, (String) request.getAttribute("random"));
    }

    public void destroy() {

    }


    private HttpServletRequest decryptRequest(HttpServletRequest request) throws IOException {
        try {
            String random = request.getParameter("random");//加密的随机数
            if (StringUtils.isEmpty(random)) {
                throw new IllegalArgumentException("invalid random");
            }
            log.debug("RSA encrypted random:{}", random);
            random = RSAUtil.decryptFromHex(rsaPrivateKey, random);
            log.debug("RSA decrypted random:{}", random);
            //随机码需要为8位的数字+字母
            if (random.length() != 8 || NumberUtils.isDigits(random) || StringUtils.isAlpha(random)) {
                throw new IllegalArgumentException("invalid random");
            }
            request.setAttribute("random", random);

            String message = HttpUtils.readContent(request, "utf-8");
            log.debug("AES encrypted message:\n{}", message);
            message = AESUtil.decryptFromHex(message, random);
            if (message == null) {
                throw new IllegalArgumentException("decrypt message fail");
            }
            log.debug("AES decrypted message:\n{}", message);
            return new DecryptedServletRequest(request, message);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            log.debug("Illegal arguments", e);
            throw new IllegalArgumentException("invalid request arguments", e);
        }
    }

    public static HttpServletResponse encryptResponse(ResponseWrapper response, String random) throws IOException {
        //response.setContentType("text/plain");
        //response.setCharacterEncoding(HttpUtils.getEncoding(response, "utf-8"));
        String message = new String(response.getDataStream());
        log.debug("plain message:\n{}", message);
        if (response.getStatus() == 200) {//出错了，不加密
            message = AESUtil.encryptToHex(message, random);
            log.debug("AES encrypted message:\n{}", message);
        } else {
            log.debug("Do not encrypt with status: {}", response.getStatus());
        }
        response.getResponse().getWriter().write(message);

        return (HttpServletResponse) response.getResponse();
    }

    private class DecryptedServletRequest extends HttpServletRequestWrapper {
        private ServletInputStream inputStream;

        public DecryptedServletRequest(HttpServletRequest request, String message) throws IOException {
            super(request);

            final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            Writer writer = new OutputStreamWriter(bos, HttpUtils.getEncoding(request, "utf-8"));
            writer.write(message);
            writer.flush();

            inputStream = new ServletInputStream() {
                final ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return this.inputStream;
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

        @Override
        public void write(byte[] arg0, int arg1, int arg2) throws IOException {
            output.write(arg0, arg1, arg2);
        }

        @Override
        public void write(byte[] arg0) throws IOException {
            output.write(arg0);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }

    public class ResponseWrapper extends HttpServletResponseWrapper {
        ByteArrayOutputStream output;
        FilterServletOutputStream filterOutput;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new ByteArrayOutputStream();
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