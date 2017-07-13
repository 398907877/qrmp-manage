<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <title>500</title>
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
                        <h1 class="error-text tada animated"><i
                                class="fa fa-times-circle text-danger error-icon-shadow"></i> Error 500</h1>
                        <h2 class="font-xl"><strong>唉唷，出错啦！</strong></h2>
                        <br>
                        <p class="lead semi-bold">
                            <strong>您的处理出现了一些问题，我们将尽快修复它。</strong><br><br>
                            <small>
                                请将问题发生的细节反馈予我们，并 <br/>
                                访问 <a href="${pageContext.request.contextPath}/help">帮助中心</a> 获取更多问题详情。
                            </small>
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