<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page trimDirectiveWhitespaces="true"%>
<aside class="sidebar">
    <!-- NAVIGATION : This navigation is also responsive

    To make this navigation dynamic please make sure to link the node
    (the reference to the nav > ul) after page load. Or the navigation
    will not initialize.
    -->
    <nav>
        <!--
        NOTE: Notice the gaps after each icon usage <i></i>..
        Please note that these links work a bit different than
        traditional href="" links. See documentation for details.
        -->

        <c:set var="xmenus" value="${menus}" scope="request"/>
        <c:import url="/menu"></c:import>
    </nav>
    <!--a href="#ajax/difver.html" class="btn btn-primary nav-demo-btn"><i class="fa fa-info-circle"></i> </a-->

    <span class="btn-minify-menu" data-action="minifyMenu"> <i class="fa fa-arrow-circle-left hit"></i> </span>
</aside>