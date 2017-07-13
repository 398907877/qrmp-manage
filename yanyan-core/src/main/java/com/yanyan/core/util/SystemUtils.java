package com.yanyan.core.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 提供获取服务器的环境以及状态信息
 * User: LinGuang, Saintcy
 * Date: 2017/4/1
 * Time: 10:18
 */
public class SystemUtils extends org.apache.commons.lang3.SystemUtils {
    private static final List<String> IP_LIST = new ArrayList<String>();
    private static boolean isGetIp = false;
    private static ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 获取本地定义的所有IP
     *
     * @param refresh 强制重新获取
     * @return ArrayList<String>
     */
    public static ArrayList<String> getLocalIPs(boolean refresh) {
        reentrantLock.lock();
        try {
            if (!isGetIp || refresh) {
                try {
                    Enumeration<NetworkInterface> netInterfaces = null;
                    try {
                        netInterfaces = NetworkInterface.getNetworkInterfaces();
                    } catch (Exception ex) {
                    }

                    InetAddress ip = null;

                    while (netInterfaces.hasMoreElements()) {
                        NetworkInterface ni = netInterfaces.nextElement();
                        Enumeration<InetAddress> list = ni.getInetAddresses();
                        while (list.hasMoreElements()) {
                            try {
                                ip = list.nextElement();
                            } catch (Exception ex1) {
                                continue;
                            }
                            String ipAddr = ip.getHostAddress();
                            if (ipAddr.indexOf("127.0.0.1") == -1 && ipAddr.indexOf(":") < 0) {
                                IP_LIST.add(ipAddr);
                            } else {
                                ip = null;
                            }
                        }
                    }
                    if (IP_LIST.size() > 0)
                        isGetIp = true;

                } catch (Exception ex) {
                    isGetIp = false;
                }
            }
            if (!isGetIp) {
                isGetIp = true;
                IP_LIST.add("127.0.0.1");//新加修改测试看看
            }

            ArrayList<String> result = new ArrayList<String>();
            if (isGetIp)
                result.addAll(IP_LIST);
            return result;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * 获取jvm名称
     *
     * @return
     */
    public static String getJvm() {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        return rb.getName();
    }

    /**
     * 获取进程号
     *
     * @return
     */
    public static String getPid() {
        String name = getJvm();
        return name.substring(0, name.indexOf('@'));
    }

    /**
     * 是否是本机IP
     *
     * @param ip
     * @return
     */
    public boolean isLocalIP(String ip) {
        ArrayList<String> ips = getLocalIPs(true);
        return ips.contains(ip);
    }

    /**
     * 获取系统属性列表并排序
     *
     * @return
     */
    public static Map<String, String> getSortedSystemProperties() {
        Map<String, String> properties = new LinkedHashMap<String, String>();
        List<String> sortedNames = new ArrayList<String>();
        java.util.Enumeration<?> names = System.getProperties().propertyNames();
        while (names.hasMoreElements()) {
            sortedNames.add((String) names.nextElement());
        }

        Collections.sort(sortedNames);

        for (String name : sortedNames) {
            properties.put(name, System.getProperty(name));
        }

        return properties;
    }
}
