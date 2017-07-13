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
        <c:forEach items="${networks}" var="network" varStatus="varStatus">
            <c:if test="${varStatus.index%2==0}">
                <div class="row">
            </c:if>
            <div class="col-sm-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <c:out value="${network.name}"/> - <c:out value="${network.description}"/>
                    </div>
                    <div class="panel-body">
                        <form class="form-horizontal" role="form">
                            <div class="form-group">
                                <label class="control-label col-sm-4">Type</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${network.type}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Hwaddr</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${network.hwaddr}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Address</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${network.address}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Destination</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${network.destination}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Broadcast</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${network.broadcast}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Netmask</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${network.netmask}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Flags</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${network.flags}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Mtu</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${network.mtu}"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Metric</label>
                                <div class="col-sm-8">
                                    <p class="form-control-static">
                                        <c:out value="${network.metric}"/>
                                    </p>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <c:if test="${varStatus.index%2==1||varStatus.last}">
                </div>
            </c:if>
        </c:forEach>
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