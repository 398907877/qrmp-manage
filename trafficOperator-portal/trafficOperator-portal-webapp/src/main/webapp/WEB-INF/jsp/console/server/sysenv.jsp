<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <title>${APP_NAME}</title>
    <meta name="description" content="">
    <meta name="author" content="tangss">

    <c:import url="/libs-css"/>
    <%//此处插入页面自定义的样式%>

</head>
<body class="">

<!-- #MAIN PANEL -->
<div class="main">

    <!-- #MAIN CONTENT -->
    <div class="content">
        <div class="row">
            <div class="col-sm-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        服务器信息
                    </div>
                    <div class="panel-body">
                        <form class="form-horizontal" role="form">
                            <div class="form-group">
                                <label class="control-label col-sm-4">网络</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        HostName: <c:out value="${netInfo.hostName}"/>, &nbsp;
                                        DefaultGateway: <c:out value="${netInfo.defaultGateway}"/>, &nbsp;
                                        PrimaryDns: <c:out value="${netInfo.primaryDns}"/>, &nbsp;
                                        SecondaryDns: <c:out value="${netInfo.secondaryDns}"/>, &nbsp;
                                        DomainName: <c:out value="${netInfo.domainName}"/> &nbsp;
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">IP</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:forEach items="${ips}" var="ip" varStatus="varStatus">
                                            ${ip}<c:if test="${!varStatus.last}">, </c:if>
                                        </c:forEach>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">操作系统</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${osName}"/>&nbsp;<c:out value="${osArch}"/>&nbsp;<c:out
                                            value="${osVersion}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">CPU</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${cpus[0].vendor}"/>(R)&nbsp;<c:out value="${cpus[0].model}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">内存</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <fmt:formatNumber value="${mem.total}"/>K
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">硬盘</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:forEach items="${fsMap}" var="fs" varStatus="varStatus">
                                            ${fs.key}&nbsp;<fmt:formatNumber minFractionDigits="0" maxFractionDigits="2"
                                                                       value="${fs.value*100}"/>% Used<c:if
                                                test="${!varStatus.last}">, </c:if>
                                        </c:forEach>
                                    </p>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        JVM信息
                    </div>
                    <div class="panel-body">
                        <form class="form-horizontal" role="form">
                            <div class="form-group">
                                <label class="control-label col-sm-4">JVM从启动至今共加载类</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">${classLoading.totalLoadedClassCount}</p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">JVM从启动至今共卸载类</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">${classLoading.totalLoadedClassCount}</p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">JVM当前共加载类</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">${classLoading.totalLoadedClassCount}</p>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <label class="control-label col-sm-4">堆内存情况</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        初始 <fmt:formatNumber value="${heapMemory.init}"/>B,
                                        当前占用 <fmt:formatNumber value="${heapMemory.used}"/>B,
                                        已启用 <fmt:formatNumber value="${heapMemory.committed}"/>B,
                                        最大 <fmt:formatNumber value="${heapMemory.max}"/>B,
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">非堆内存情况</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        初始 <fmt:formatNumber value="${heapMemory.init}"/>B,
                                        当前占用 <fmt:formatNumber value="${heapMemory.used}"/>B,
                                        已启用 <fmt:formatNumber value="${heapMemory.committed}"/>B,
                                        最大 <fmt:formatNumber value="${heapMemory.max}"/>B,
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">待回收对象</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <fmt:formatNumber value="${objectPendingFinalizationCount}"/>个
                                    </p>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12 table-responsive">
                <table id="table" class="table table-striped table-hover no-margin">
                    <thead>
                    <tr>
                        <th width="40%">属性名</th>
                        <th width="60%">属性值</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${properties}" var="property">
                        <tr>
                            <td class="text-left">
                                <c:out value="${property.key}"/>
                            </td>
                            <td class="text-left word-wrap">
                                <c:out value="${property.value}"/>
                            </td>
                            <td class="text-center">
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <!-- END #MAIN CONTENT -->

</div>
<!-- END #MAIN PANEL -->

<!--================================================== -->

<c:import url="/libs-js"/>
<%//此处插入页面自定义的脚本%>
<script type="text/javascript">

</script>
</body>
</html>