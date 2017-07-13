package com.yanyan.core.util;

import com.google.common.collect.Lists;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http工具类
 * User: Saintcy
 * Date: 2016/3/28
 * Time: 12:07
 */
@Slf4j
public class HttpUtils {
    public static String getClientIp(HttpServletRequest request) {
        if (request != null) {
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }
        return null;
    }

    public static String getClient_type(HttpServletRequest request) {
        DeviceType deviceType = UserAgentUtils.getDeviceType(request);

        return deviceType == null ? "" : deviceType.getName();
    }

    public static String getClient_agent(HttpServletRequest request) {
        Browser browser = UserAgentUtils.getBrowser(request);
        return browser == null ? "" : browser.getName();
    }

    public static String getSystem_host(HttpServletRequest request) {
        return request.getLocalAddr();
    }

    final static Pattern pattern = Pattern.compile("\\S*[?]\\S*");

    /**
     * 获取链接的后缀名
     *
     * @return
     */
    public static String getSuffix(String url) {
        Matcher matcher = pattern.matcher(url);

        String[] spUrl = url.toString().split("/");
        int len = spUrl.length;
        if (len <= 0) {
            return "";
        }
        String endUrl = spUrl[len - 1];

        if (endUrl.indexOf(".") >= 0) {
            if (matcher.find()) {
                String[] spEndUrl = endUrl.split("\\?");
                return spEndUrl[0].split("\\.")[1];
            }
            return endUrl.split("\\.")[1];
        } else {
            return "";
        }

    }

    /**
     * 上传附件
     *
     * @param request
     * @return
     * @throws FileUploadException
     * @throws IOException
     */
    public static List<FileItem> getFileItemList(HttpServletRequest request) throws FileUploadException, IOException {
        FileItemFactory factory = new DiskFileItemFactory();// 为该请求创建一个DiskFileItemFactory对象，通过它来解析请求。执行解析后，所有的表单项目都保存在一个List中。
        ServletFileUpload upload = new ServletFileUpload(factory);
        // upload.setHeaderEncoding("ISO-8859-1");
        List<FileItem> items = upload.parseRequest(request);
        Iterator<FileItem> itr = items.iterator();

        List<FileItem> attaches = Lists.newArrayList();
        while (itr.hasNext()) {
            FileItem item = itr.next();
            if (!item.isFormField()) {// 如果是上传文件，显示文件名。
                attaches.add(item);
            }
        }
        return attaches;
    }

    /**
     * 从HTTP内容类型获取字符类型
     *
     * @param contentType
     * @return
     */
    public static String findCharset(String contentType) {
        if (contentType == null) {
            return null;
        }
        int idx = contentType.indexOf("charset=");
        if (idx != -1) {
            String charset = contentType.substring(idx + 8);
            if (charset.indexOf(";") != -1) {
                charset = charset.substring(0, charset.indexOf(";")).trim();
            }
            if (charset.charAt(0) == '\"') {
                charset = charset.substring(1, charset.length() - 1);
            }
            return charset;
        }
        return null;
    }

    /**
     * 获取请求内容编码
     *
     * @param request
     * @param defaultEncoding
     * @return
     */
    public static String getEncoding(final ServletRequest request,
                                     String defaultEncoding) {
        String contentType = request.getContentType();
        String enc = findCharset(contentType);
        if (enc == null) {
            enc = request.getCharacterEncoding();
        }

        if (enc == null) {
            return defaultEncoding;
        }

        return enc;
    }

    /**
     * 读取http中的流
     *
     * @param request
     * @param enc
     * @return
     * @throws IOException
     */
    public static String readContent(final HttpServletRequest request, String enc)
            throws IOException {
        ServletInputStream input = request.getInputStream();
        int bufferSize = 500;

        int avail = input.available();
        if (avail > bufferSize) {
            bufferSize = avail;
        }
        log.debug("" + avail);
        StringBuilder buf = new StringBuilder();
        final byte[] buffer = new byte[bufferSize];
        int n = 0;
        n = input.read(buffer);
        while (-1 != n) {
            buf.append(new String(buffer, 0, n));
            // logger.debug(new String(buffer, 0, n));
            n = input.read(buffer);
        }
        input.close();
        String content = URLDecoder.decode(buf.toString(), enc);
        // return buf.toString();

        return content;
    }
}
