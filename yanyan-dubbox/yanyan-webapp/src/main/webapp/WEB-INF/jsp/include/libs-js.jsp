<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page trimDirectiveWhitespaces="true"%>
<!--Mask the Page When Ajax Loading-->
<div class="sk-mask">
    <div class="sk-wave">
        <div class="sk-rect sk-rect1"></div>
        <div class="sk-rect sk-rect2"></div>
        <div class="sk-rect sk-rect3"></div>
        <div class="sk-rect sk-rect4"></div>
        <div class="sk-rect sk-rect5"></div>
    </div>
</div>

<!-- PACE LOADER - turn this on if you want ajax loading to show (caution: uses lots of memory on iDevices)-->
<script data-pace-options='{ "restartOnRequestAfter": true }' src="${home}/assets/libs/pace/pace.min.js"></script>

<!-- #PLUGINS -->
<!-- jQuery  -->
<script src="${home}/assets/libs/jquery-1.12.4.min.js"></script>

<!-- BOOTSTRAP JS -->
<script src="${home}/assets/libs/bootstrap/js/bootstrap.min.js"></script>

<!-- browser msie issue fix -->
<script src="${home}/assets/libs/jquery-mb-browser/jquery.mb.browser.min.js"></script>

<!-- FastClick: For mobile devices: you can disable this in app.js -->
<script src="${home}/assets/libs/fastclick/fastclick.min.js"></script>

<%//打印引用的页面中的脚本，注意，引用时scope需要为request%>
<c:forEach items="${scripts}" var="script">
${script}
</c:forEach>

<script src="${home}/assets/libs/layer/layer.min.js"></script>

<script src="${home}/assets/libs/ecma.script.plus.min.js"></script>
<script src="${home}/assets/libs/jquery.url.min.js"></script>
<script src="${home}/assets/libs/jquery.service.min.js"></script>

<!--[if IE 8]>
<h1>Your browser is out of date, please update your browser by going to www.microsoft.com/download</h1>
<![endif]-->
<script>$.contextPath = "${home}"</script>
<!-- COMMON APP JS FILE -->
<script src="${home}/assets/js/app.min.js"></script>

<!--Sidebar Menu-->
<script src="${home}/assets/plugins/multiple-accordion/scriptbreaker-multiple-accordion-1.min.js"></script>
<!-- Main Page JS FILE -->
<script src="${home}/assets/js/main.min.js"></script>