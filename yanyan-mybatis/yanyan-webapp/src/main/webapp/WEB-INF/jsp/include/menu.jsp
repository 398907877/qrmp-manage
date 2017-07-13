<%--
  递归展示菜单
  User: Saintcy
  Date: 2016/8/15
  Time: 23:28
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul>
<c:forEach var="menu" items="${xmenus}">
    <li class="${menu.active?(menu.open?"open active":"active"):""}${menu.hide?" hidden":""}">
        <a href="<c:if test="${empty menu.url}">javascript:</c:if><c:if test="${!empty menu.url}">${home}/${menu.url}</c:if>" target="${menu.target}"><i class="${menu.icon}"></i> <span class="menu-item-parent"><c:out value="${menu.name}"/></span></a>
        <c:if test="${!empty menu.children}">
            <c:set var="xmenus" value="${menu.children}" scope="request" />
            <c:import url="/menu" />
        </c:if>
    </li>
</c:forEach>
</ul>