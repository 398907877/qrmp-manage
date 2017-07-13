<%--
  递归展示树
  User: Saintcy
  Date: 2016/8/15
  Time: 23:28
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul>
<c:forEach var="node" items="${treeNodes}">
    <li data-id="${node.id}"><span><c:if test="${!empty node.children}"><i class="fa fa-fw icon-minus-sign"></i>&nbsp;</c:if><c:if test="${style=='checkbox'}"><i class="fa fa-fw fa-lg ${node.checked?'fa-check-square-o':'fa-square-o'}" role="checkbox"></i>&nbsp;</c:if><c:if test="${style=='radio'}"><i class="fa fa-fw fa-lg ${node.checked?'fa-dot-circle-o':'fa-circle-o'}" role="radio"></i>&nbsp;</c:if><i class="${node.icon}"></i><c:out value="${node.name}"/></span>
    <c:if test="${!empty node.children}">
        <c:set var="treeNodes" value="${node.children}" scope="request" />
        <c:import url="/tree" />
    </c:if>
    </li>
</c:forEach>
</ul>