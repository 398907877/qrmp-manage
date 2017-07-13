<%--
  分页栏
  pageNo: 页码
  pageSize: 每页条数
  totalCount: 总条数
  totalPage: 总页数
  target: 分页按钮点击时对应的table对象或选择器
  User: Saintcy
  Date: 2017/1/25
  Time: 9:48
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<span class="pull-left page-info">
    每页
    <select data-target="table" class="page-size">
        <c:forEach items="${rowList}" var="rowNum">
            <option value="${rowNum}" ${pageSize==rowNum?"selected=selected":""}>${rowNum}</option>
        </c:forEach>
    </select>
    条，共<span class="total-count">${totalCount}</span>条
</span>
<span class="pull-right">
    <ul class="pagination">
        <c:set var="midPageNo"
               value="${pageNo < 3 ? 3 : (pageNo > totalPage - 2 ? totalPage - 2 : pageNo)}" scope="request"></c:set>
        <c:set var="prevPageNo" value="${pageNo-1}" scope="request"></c:set>
        <c:set var="firstPageNo" value="${1}" scope="request"></c:set>
        <c:set var="pageNav1No" value="${midPageNo - 2}" scope="request"></c:set>
        <c:set var="pageNav2No" value="${midPageNo - 1}" scope="request"></c:set>
        <c:set var="pageNav3No" value="${midPageNo}" scope="request"></c:set>
        <c:set var="pageNav4No" value="${midPageNo + 1}" scope="request"></c:set>
        <c:set var="pageNav5No" value="${midPageNo + 2}" scope="request"></c:set>
        <c:set var="lastPageNo" value="${totalPage}" scope="request"></c:set>
        <c:set var="nextPageNo" value="${pageNo+1}" scope="request"></c:set>

        <c:if test="${pageNo!=1}">
            <li data-target="table" data-page-no="${firstPageNo}" title="最前页">
                <a href="#">
                    <span>&laquo;</span>
                </a>
            </li>
            <li data-target="table" data-page-no="${prevPageNo}" title="后一页">
                <a href="#">
                    <span>&lsaquo;</span>
                </a>
            </li>
        </c:if>
        <c:if test="${pageNav1No >= 1 && pageNav1No <= totalPage}">
            <li data-target="${empty target?"#table":target}" data-page-no="${pageNav1No}"
                class="${pageNav1No==pageNo?"active":""}"><a href="#">${pageNav1No}</a></li>
        </c:if>
        <c:if test="${pageNav2No >= 1 && pageNav2No <= totalPage}">
            <li data-target="${empty target?"#table":target}" data-page-no="${pageNav2No}"
                class="${pageNav2No==pageNo?"active":""}"><a href="#">${pageNav2No}</a></li>
        </c:if>
        <c:if test="${pageNav3No >= 1 && pageNav3No <= totalPage}">
            <li data-target="${empty target?"#table":target}" data-page-no="${pageNav3No}"
                class="${pageNav3No==pageNo?"active":""}"><a href="#">${pageNav3No}</a></li>
        </c:if>
        <c:if test="${pageNav4No >= 1 && pageNav4No <= totalPage}">
            <li data-target="${empty target?"#table":target}" data-page-no="${pageNav4No}"
                class="${pageNav4No==pageNo?"active":""}"><a href="#">${pageNav4No}</a></li>
        </c:if>
        <c:if test="${pageNav5No >= 1 && pageNav5No <= totalPage}">
            <li data-target="${empty target?"#table":target}" data-page-no="${pageNav5No}"
                class="${pageNav5No==pageNo?"active":""}"><a href="#">${pageNav5No}</a></li>
        </c:if>
        <c:if test="${pageNo!=totalPage}">
            <li data-target="${empty target?"#table":target}" data-page-no="${nextPageNo}" title="后一页">
                <a href="#">
                    <span>&rsaquo;</span>
                </a>
            </li>
            <li data-target="${empty target?"#table":target}" data-page-no="${lastPageNo}" title="最后页">
                <a href="#">
                    <span>&raquo;</span>
                </a>
            </li>
        </c:if>
    </ul>
</span>
