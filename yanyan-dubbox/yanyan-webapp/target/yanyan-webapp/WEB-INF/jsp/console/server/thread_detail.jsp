<%@ page language="java" pageEncoding="UTF-8"
         contentType="text/html; charset=UTF-8" %>
<%@ page import="com.yanyan.core.util.SystemUtils,org.apache.commons.lang3.time.DateFormatUtils" %>
<%@ page import="java.lang.reflect.Method" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%


    Map<Long, Long[]> threadUseTimeMap = new HashMap<Long, Long[]>();
    java.lang.management.ThreadMXBean tm = java.lang.management.ManagementFactory
            .getThreadMXBean();
    long[] threadIds = tm.getAllThreadIds();
    for (int i = 0; i < threadIds.length; i++) {
        long userTime = tm.getThreadCpuTime(threadIds[i]);
        threadUseTimeMap.put(threadIds[i], new Long[]{userTime, System.currentTimeMillis()});
    }


    String threadId = request.getParameter("id");
    String export = request.getParameter("export");

    Method method = Thread.class.getDeclaredMethod("getThreads");
    method.setAccessible(true);
    Thread[] threads = (Thread[]) method.invoke(Thread.class);
    Map<Long, Thread> threadMap = new HashMap<Long, Thread>();

    if (threads != null)
        for (int i = 0; i < threads.length; i++) {
            threadMap.put(threads[i].getId(), threads[i]);
        }


    int dumpTimes = 3;
    if ("1".equals(export)) dumpTimes = 4;

    StringBuffer sbMsg = new StringBuffer();


    threadIds = tm.getAllThreadIds();


    java.lang.management.ThreadInfo[] tInfos = null;//tm.getThreadInfo(threadIds,Integer.MAX_VALUE);


    sbMsg.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
    sbMsg.append("<meta http-equiv=\"pragma\" content=\"no-cache\"><meta http-equiv=\"cache-control\" content=\"no-cache\">");
    sbMsg.append("<meta http-equiv=\"expires\" content=\"0\">");

    sbMsg.append("<title>所有线程详情</title></head><body style=\"font-size: 14px;font-family: Tahoma;overflow:auto;margin:5px;background-color:#F5F8FD\">\r\n");

    sbMsg.append("当前进程：" + SystemUtils.getJvm() + "<br>\r\n");
    java.lang.management.OperatingSystemMXBean operatingSystemMXBean = java.lang.management.ManagementFactory
            .getOperatingSystemMXBean();

    sbMsg.append("当前JAVA版本：" + System.getProperty("java.runtime.version") + " " + System.getProperty("sun.arch.data.model") + "位，启动位置:" + System.getProperty("sun.boot.library.path") + "<br>\r\n");

    sbMsg.append("当前操作系统：" + operatingSystemMXBean.getName() + " "
            + operatingSystemMXBean.getArch() + " "
            + operatingSystemMXBean.getVersion() + "<br>\r\n");

    java.lang.management.ClassLoadingMXBean classLoadingMXBean = java.lang.management.ManagementFactory
            .getClassLoadingMXBean();
    sbMsg.append("从JVM启动至今共加载类:" + classLoadingMXBean.getTotalLoadedClassCount() + "个<br>\r\n");
    sbMsg.append("从JVM启动至今共卸载类:" + classLoadingMXBean.getUnloadedClassCount() + "个<br>\r\n");
    sbMsg.append("当前共加载类:" + classLoadingMXBean.getLoadedClassCount() + "个<br>\r\n");


    java.lang.management.MemoryMXBean mm = java.lang.management.ManagementFactory.getMemoryMXBean();
    java.lang.management.MemoryUsage hm = mm.getHeapMemoryUsage();
    java.lang.management.MemoryUsage noneHm = mm.getNonHeapMemoryUsage();
    sbMsg.append("堆内存情况:");
    sbMsg.append("初始=" + hm.getInit() + "(" + (hm.getInit() >> 10) + "K," + (hm.getInit() >> 10 >> 10) + "M), ");
    sbMsg.append("当前占用=" + hm.getUsed() + "(" + (hm.getUsed() >> 10) + "K," + (hm.getUsed() >> 10 >> 10) + "M), ");
    sbMsg.append("已启用=" + hm.getCommitted() + "(" +
            (hm.getCommitted() >> 10) + "K," + (hm.getCommitted() >> 10 >> 10) + "M), ");
    sbMsg.append("最大=" + hm.getMax() + "(" + (hm.getMax() >> 10) + "," + (hm.getMax() >> 10 >> 10) + "M) <br>\r\n");


    sbMsg.append("非堆内存情况:");
    sbMsg.append("初始=" + noneHm.getInit() + "(" + (noneHm.getInit() >> 10) + "K," + (noneHm.getInit() >> 10 >> 10) + "M), ");
    sbMsg.append("当前占用=" + noneHm.getUsed() + "(" + (noneHm.getUsed() >> 10) + "K," + (noneHm.getUsed() >> 10 >> 10) + "M), ");
    sbMsg.append("已启用=" + noneHm.getCommitted() + "(" +
            (noneHm.getCommitted() >> 10) + "K," + (noneHm.getCommitted() >> 10 >> 10) + "M), ");
    sbMsg.append("最大=" + noneHm.getMax() + "(" + (noneHm.getMax() >> 10) + "," + (noneHm.getMax() >> 10 >> 10) + "M)<br><br>\r\n\r\n");


    for (int p = 0; p < dumpTimes; p++) {

        if (threadId == null)
            tInfos = tm.getThreadInfo(tm.getAllThreadIds(), Integer.MAX_VALUE);
        else
            tInfos = tm.getThreadInfo(new long[]{Long.parseLong(threadId.trim())}, Integer.MAX_VALUE);


        sbMsg.append("<div style=\"width:100%;background-color:gray;color:white;\">第" + (p + 1) + "次 dump 开始 " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ",共计" + tInfos.length + "个线程</div>\r\n");
        sbMsg.append("<div style=\"color:white;background-color:black;\">");

        tm = java.lang.management.ManagementFactory.getThreadMXBean();

        long nowTime = System.currentTimeMillis();

        for (int i = 0; i < tInfos.length; i++) {
            java.lang.management.ThreadInfo tInfo = tInfos[i];

            long userTime = tm.getThreadCpuTime(tInfo.getThreadId());

            StackTraceElement[] stacks = tInfo.getStackTrace();

            //if(Thread.currentThread().getId()==tInfo.getThreadId())continue;


            long lastCpuTime = nowTime;
            long last_time = nowTime;
            Long[] aTmpTime = threadUseTimeMap.get(tInfo.getThreadId());
            if (aTmpTime != null) {
                lastCpuTime = aTmpTime[0].longValue();
                last_time = aTmpTime[1].longValue();
            }
            threadUseTimeMap.put(tInfo.getThreadId(), new Long[]{userTime, nowTime});
            double cpuPer = 0;
            if (nowTime - last_time > 0) {
                cpuPer = Math.floor((userTime - lastCpuTime) * 100 / ((nowTime - last_time) * 1000 * 1000));
            }


            String color = "lightgray";
            if (tInfo.getThreadState() == Thread.State.BLOCKED) {
                color = "lightred";
            } else if (tInfo.getThreadState() == Thread.State.RUNNABLE) {
                color = "lightgreen";
            }

            String lockMsg = "";
            if (tInfo.getLockOwnerId() >= 0) {
                lockMsg = "<font color=lightred>被" + tInfo.getLockOwnerName() + "(thread=" + tInfo.getLockOwnerId() + ")锁定</font>";
            }


            if (threadMap.get(tInfo.getThreadId()) != null) {
                Thread thread = threadMap.get(tInfo.getThreadId());
                lockMsg = lockMsg + "&nbsp;本线程类加载器[" + thread.getContextClassLoader() + "]";
            }


            if (p > 0) {
                if (cpuPer > 50) {
                    lockMsg = lockMsg + "&nbsp;CPU利用率:<font color=red>" + cpuPer + "%</font>";
                } else {
                    lockMsg = lockMsg + "&nbsp;CPU:" + cpuPer + "%";
                }
            }

            sbMsg.append("<font color=lightblue>" + tInfo.getThreadName() + "(id=" + tInfo.getThreadId() + ")</font>,状态[<font color=" + color + ">" + tInfo.getThreadState() + "</font>]" + lockMsg + "<br>\r\n");


            if (stacks != null) {
                for (int j = 0; j < stacks.length; j++) {
                    sbMsg.append("&nbsp;&nbsp;&nbsp;&nbsp;" + stacks[j].toString() + "<br>\r\n");
                }
            } else {
                sbMsg.append("&nbsp;&nbsp;&nbsp;&nbsp;no statck");
            }
            sbMsg.append("<br><br>\r\n\r\n");

        }
        sbMsg.append("</div>");
        sbMsg.append("<div style=\"width:100%;background-color:gray;color:white;\">第" + (p + 1) + "次 dump 结束 " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "</div><br><br><br><br>\r\n\r\n\r\n\r\n");


        try {
            if (p < dumpTimes - 1) {
                if ("1".equals(export)) Thread.sleep(3000);
                else Thread.sleep(1000);
            }
        } catch (Exception ex) {
        }

    }


    sbMsg.append("</body></html>");
    if ("1".equals(export)) {
        response.setContentType("html/plain");
        response.setHeader("Content-disposition", "attachment;filename=\"dump_" + SystemUtils.getPid() + "_" + DateFormatUtils.format(new Date(), "(yyyyMMddHHmmss)") + ".htm\"");
        java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(response.getOutputStream());
        byte[] data = sbMsg.toString().getBytes();
        response.setContentLength(data.length);

        try {
            bos.write(data);
            bos.close();
        } catch (Exception ex) {
        }
        return;
    } else out.println(sbMsg);
%>
