<%@ page import="java.lang.reflect.Method" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
    String className = request.getParameter("className");
    ClassLoader spec = null;
    if (className != null && !className.endsWith(".xml"))
        className = className.replace(".", "/") + ".class";
    if (className != null) {
        ClassLoader p = Thread.currentThread().getContextClassLoader();
        Method method = ClassLoader.class.getDeclaredMethod("findResource", new Class<?>[]{String.class});
        method.setAccessible(true);
        while (p != null) {
            java.net.URL url = (java.net.URL) method.invoke(p, new Object[]{className});


            out.println("类[" + className + "]在类加载器[" + p + "]内查询结果[" + (url != null ? "<font color=red>" + url.toString() + "</font>" : "") + "]<br><br><br>");
            p = p.getParent();
        }

        try {
            Class cl = Class.forName(className);
            if (cl != null) {
                out.println("<br>========================================================================================================================<br>");
                java.net.URL url = (java.net.URL) method.invoke(cl.getClassLoader(), new Object[]{className});
                out.println("类[" + className + "]在当前jsp内，最终是通过类加载器[<font color=blue>" +
                        cl.getClassLoader() + "</font>]加载，加载路径[" + (url != null ? "<font color=red>" + url.toString() + "</font>" : "") + "]<br><br><br>");
            }
        } catch (Exception ex) {
        }
        return;
    }
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
</head>
<body>
<form action="" method="post" target="result_frame">
    类名：<input name="className" value="" style="width:50%"><input type=submit value="查询">
</form>
<iframe id="result_frame" name="result_frame" style="width:100%;height:80%"></iframe>
</body>
</html>