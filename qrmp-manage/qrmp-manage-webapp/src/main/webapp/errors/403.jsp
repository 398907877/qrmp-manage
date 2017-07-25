<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <title>403</title>
    <meta name="description" content="">
    <meta name="author" content="tangss">
    <c:import url="/libs-css"></c:import>
    <!--在此处插入自定义的样式-->
</head>

<body>
<!-- #MAIN CONTENT -->
<div class="content">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="row">
                <div class="col-sm-12">
                    <div class="text-center error-box">
                        <h1 class="error-text bounceInDown animated"> Error 403 </h1>
                        <h2 class="font-xl"><strong><i class="fa fa-fw fa-frown-o fa-lg text-warning"></i> 禁止访问</strong>
                        </h2>
                        <br>
                        <p class="lead">您没有权限访问该页面.</p>
                        <p class="font-md"><b>您的用户组或个人没有得到管理员的授权.</b></p>
                        <br>
                        <p>
                            <a href="javascript: history.back()">回到上一页</a> /
                            <a href="${pageContext.request.contextPath}/index.jsp">返回首页</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- END #MAIN CONTENT -->
<!-- 在此处插入自定义的JS -->
</body>
</html>