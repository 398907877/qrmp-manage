<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<body>

<!-- #PAGE HEADER -->
<c:import url="/header"/>
<!-- END PAGE HEADER -->

<!-- #NAVIGATION -->
<c:import url="/sidebar?menu=region"/>
<!-- END NAVIGATION -->

<!-- #MAIN PANEL -->
<div class="main">

    <!-- TOPBAR -->
    <c:import url="/topbar"/>
    <!-- END TOPBAR -->

    <!-- #MAIN CONTENT -->
    <div class="content">
        <div class="row">
            <div class="col-sm-12">
                <div class="panel panel-default">
                    <div class="panel-heading btn-toolbar">
                        <ul class="nav nav-tabs pull-left in path">
                            <li class="${query.parent_id==0?"active":""}" data-id="0">
                                <a href="${home}/region/list"><span>国家列表</span></a>
                            </li>
                            <c:if test="${query.parent_id!=0}">
                                <li class='dropdown path-separator' data-id='0'><a href='javascript:' class='dropdown-toggle'
                                                                    data-toggle='dropdown'><b
                                        class='fa fa-fw fa-caret-right'></b></a>
                                    <ul class='dropdown-menu'>
                                        <li><a href="javascript:"><i class="fa fa-spinner fa-pulse"></i><span>加载中...</span></a></li>
                                    </ul>
                                </li>
                            </c:if>
                            <c:set var="level_name" value="国家"></c:set>
                            <c:if test="${!empty parents}">
                                <c:forEach items="${parents}" var="r" varStatus="varStatus">
                                    <li class="${query.parent_id==r.id?"active":""}" data-id="${r.id}">
                                        <a href="${home}/region/list?parent_id=${r.id}"><span>${r.name}</span></a>
                                    </li>
                                    <c:if test="${!varStatus.last}">
                                        <li class='dropdown path-separator' data-id='${r.id}'><a href='javascript:'
                                                                                  class='dropdown-toggle'
                                                                                  data-toggle='dropdown'><b
                                                class='fa fa-fw fa-caret-right'></b></a>
                                            <ul class='dropdown-menu'>
                                                <li><a href="javascript:"><i class="fa fa-spinner fa-pulse"></i><span>加载中...</span></a></li>
                                            </ul>
                                        </li>
                                    </c:if>
                                    <c:if test="${varStatus.last}">
                                        <c:choose>
                                            <c:when test="${r.level==1}">
                                                <c:set var="level_name" value="省份"></c:set>
                                            </c:when>
                                            <c:when test="${r.level==2}">
                                                <c:set var="level_name" value="地市"></c:set>
                                            </c:when>
                                            <c:when test="${r.level==3}">
                                                <c:set var="level_name" value="区/县"></c:set>
                                            </c:when>
                                            <c:when test="${r.level==4}">
                                                <c:set var="level_name" value="街道/社区"></c:set>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="level_name" value="地区"></c:set>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            <li id="btnCreate">
                                <a href="javascript:"><span>新增${level_name}</span></a>
                            </li>
                        </ul>
                        <div class="btn-group pull-right">
                            <button type="button" class="btn btn-default toggle-btn" title="折叠"><i
                                    class="fa fa-minus "></i></button>
                            <button type="button" class="btn btn-default zoom-btn" title="全屏"><i
                                    class="fa fa-expand "></i></button>
                        </div>
                    </div>
                    <div class="panel-body">
                        <!--form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-2 hidden">关键字</label>
                                <div class="col-sm-8 col-sm-offset-2 col-md-5 col-md-offset-2">
                                    <input id="keyword" type="text" class="form-control" placeholder="请输入名称、编码查找" value="${query.keyword}">
                                </div>
                                <div class="col-sm-2 col-md-5 hidden-xs">
                                    <button id="btnQuery" class="btn btn-primary">查询</button>
                                    <button id="btnCreate" class="btn btn-default">添加</button>
                                </div>
                            </div>
                        </form-->
                        <div class="table-responsive">
                            <table id="table" class="table table-striped table-hover no-margin">
                                <thead>
                                <tr>
                                    <th style="width: 50px">排序</th>
                                    <th>${level_name}名称</th>
                                    <th>${level_name}代码</th>
                                    <th>上级地区</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <c:if test="${page.totalCount>0}">
                                    <tbody>
                                    <c:forEach items="${page.rows}" var="region">
                                        <tr>
                                            <td class="text-left">${region.priority}</td>
                                            <td class="text-left name"><c:out value="${region.name}"/></td>
                                            <td class="text-left"><c:out value="${region.code}"/></td>
                                            <td class="text-left"><c:out value="${region.parent_name}"/></td>
                                            <td class="text-center">
                                                <div class="lk-btn-group" data-id="${region.id}">
                                                    <a href="javascript:" class="update">编辑</a><a href="javascript:"
                                                                                                  class="delete">删除</a><c:if test="${region.level<5}"><a
                                                        class="children" href="javascript:"><c:choose>
                                                    <c:when test="${region.level==1}">省份列表</c:when>
                                                    <c:when test="${region.level==2}">地市列表</c:when>
                                                    <c:when test="${region.level==3}">区/县列表</c:when>
                                                    <c:when test="${region.level==4}">街道/社区列表</c:when>
                                                    <c:otherwise>下级地区列表</c:otherwise>
                                                </c:choose></a></c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                    <tfoot>
                                    <tr class="pagination-bar hidden">
                                        <td colspan="5">
                                            <c:set var="pageNo" value="${page.pageNo}" scope="request"></c:set>
                                            <c:set var="pageSize" value="${page.pageSize}" scope="request"></c:set>
                                            <c:set var="totalCount" value="${page.totalCount}" scope="request"></c:set>
                                            <c:set var="totalPage" value="${page.totalPage}" scope="request"></c:set>
                                            <c:import url="/pagination-bar"></c:import>
                                        </td>
                                    </tr>
                                    </tfoot>
                                </c:if>
                            </table>
                        </div>
                        <div class="alert alert-info no-margin text-center no-records ${empty page or page.totalCount<=0?"":"hidden"}">
                            <i class="fa-fw fa fa-info"></i>
                            没有找到相关记录！
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <!-- END #MAIN CONTENT -->

</div>
<!-- END #MAIN PANEL -->

<!-- #PAGE FOOTER -->
<c:import url="/footer"/>
<!--================================================== -->

<c:import url="/libs-js"/>
<%//此处插入页面自定义的脚本%>
<script type="text/javascript">
    function doQuery(pageNo, pageSize) {
        location.assign("${home}/region/list?" + $.param({
                    "portal_id": $(".panel-heading .nav-tabs li.active").data("id"),
                    "pageNo": pageNo,
                    "pageSize": pageSize
                }));
    }

    function doCreate() {
        location.assign("${home}/region/create?parent_id=" + $(".panel-heading .nav-tabs li.active").data("id"));
    }

    function doUpdate(id) {
        location.assign("${home}/region/update?id=" + id);
    }

    function doDelete(id) {
        $.ServiceClient.invoke("${home}/region/delete/" + id + ".json", {
            complete: function (data) {
                if (data.success) {
                    location.reload();
                } else {
                    layer.msg(data.errmsg, {icon: 2});
                }
            }
        });
    }

    function doListChildren(id) {
        location.assign("${home}/region/list?parent_id=" + id);
    }

    function doDropdown($dropdown) {
        if (!$dropdown.data("loaded") && !$dropdown.data("loading")) {
            $dropdown.data("loading", true);
            $.ServiceClient.invoke("${home}/region/find.json", {
                data: {
                    "parent_id": $dropdown.data("id") || 0
                },
                complete: function (data) {
                    if (data.success) {
                        var $menu = $dropdown.find(".dropdown-menu");
                        $menu.empty();
                        $.each(data.page.rows, function (i, region) {
                            $('<li><a href="${home}/region/list?parent_id=' + region.id + '">' + region.name + '</a></li>').appendTo($menu);
                        });
                        $dropdown.data("loaded", true);
                    } else {
                        //layer.msg(data.errmsg, {icon: 2});
                    }
                    $dropdown.data("loading", false);
                }
            });
        }
    }

    function getName(el) {
        return $(el).closest("tr").find(".name").text();
    }

    function getId(el) {
        return $(el).closest(".lk-btn-group").data("id");
    }

    function getPageNo($table) {
        return parseInt($(".pagination li.active", $table).data("page-no")) || 1;
    }

    function getPageSize($table) {
        return parseInt($(".pagination-bar .page-size", $table).val()) || $("tbody", $table).find("tr").length;
    }

    $(function () {
        $(document).on("click", "#btnQuery", function (e) {//查询
            e.preventDefault();
            var $table = $(".table");
            doQuery(1, getPageSize($table));
        }).on("click", ".pagination li", function (e) {//翻页
            e.preventDefault();
            var $table = $(this).closest(".table");
            doQuery($(this).data("page-no"), getPageSize($table));
        }).on("change", ".pagination-bar .page-size", function (e) {//每页条数
            e.preventDefault();
            var $table = $(this).closest(".table");
            doQuery(getPageNo($table), getPageSize($table));
        }).on("click", "#btnCreate", function (e) {//创建
            e.preventDefault();
            doCreate();
        }).on("click", ".update", function (e) {//编辑
            doUpdate(getId($(this)));
        }).on("click", ".delete", function (e) {//禁用
            var $this = $(this);
            layer.confirm("确认删除地区[" + getName($this) + "]？", {
                icon: 3,
                title: '提示'
            }, function (index) {
                doDelete(getId($this));
                layer.close(index);
            });
        }).on("click", ".children", function (e) {//下级地区
            doListChildren(getId($(this)));
        }).on("show.bs.dropdown", ".dropdown", function (e) {
            doDropdown($(this));
            $(this).find(".fa-caret-right").removeClass("fa-caret-right").addClass("fa-caret-down");
        }).on("hide.bs.dropdown", ".dropdown", function (e){
            $(this).find(".fa-caret-down").removeClass("fa-caret-down").addClass("fa-caret-right");
        });
    })
</script>
</body>
</html>