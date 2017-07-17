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
<c:import url="/sidebar?menu=resource"/>
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
                        <ul class="nav nav-tabs pull-left in">
                            <c:forEach items="${portals}" var="portal">
                                <li class="${query.portal_id==portal.id?"active":""}" data-id="${portal.id}">
                                    <a href="${home}/resource/list?portal_id=${portal.id}"> <span
                                            class=""> <c:out value="${portal.name}"/> </span> </a>
                                </li>
                            </c:forEach>
                            <li id="btnCreate">
                                <a href="javascript:"><span>新增资源</span></a>
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
                                    <input id="keyword" type="text" class="form-control" placeholder="请输入名称、编码查找">
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
                                    <th>资源名称</th>
                                    <th>资源编码</th>
                                    <th>上级资源</th>
                                    <th>是否显示</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <c:if test="${page.totalCount>0}">
                                    <tbody>
                                    <c:forEach items="${page.rows}" var="resource">
                                        <tr>
                                            <td class="text-left">${resource.priority}</td>
                                            <td class="text-left">
                                                <span class="tree-ctrl" data-id="${resource.id}"
                                                      data-loaded="${resource.hasChildren?false:true}">
                                                <span class="tree-fork ${resource.hasChildren?"fork-close":""}"></span>
                                                <span class="<c:out value="${resource.icon}"/>"></span>
                                                </span>
                                                <span class="name"><c:out value="${resource.name}"/></span>
                                                &nbsp;&nbsp;&nbsp;&nbsp;<a class="create-child" href="javascript:"
                                                                           data-id='${resource.id}'><i
                                                    class="fa fa-plus"></i>增加下级</a>
                                            </td>
                                            <td class="text-left"><c:out value="${resource.code}"/></td>
                                            <td class="text-left"><c:out value="${resource.parent_name}"/></td>
                                            <td class="text-left">${resource.is_show==1?'<label class="label label-success">显示</label>':'<label class="label label-default">隐藏</label>'}</td>
                                            <td class="text-center">
                                                <div class="lk-btn-group" data-id="${resource.id}">
                                                    <a href="javascript:" class="update">编辑</a><a href="javascript:"
                                                                                                  class="delete">删除</a>
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
<script src="${home}/assets/plugins/jquery.table.js"></script>
<script type="text/javascript">
    function doQuery(pageNo, pageSize) {
        location.assign("${home}/resource/list?" + $.param({
                    "keyword": $("#keyword").val(),
                    "pageNo": pageNo,
                    "pageSize": pageSize
                }));
    }

    function doCreate(parent_id) {
        location.assign("${home}/resource/create?portal_id=" + $(".panel-heading .nav-tabs li.active").data("id") + "&parent_id=" + (parent_id || 0));
    }

    function doUpdate(id) {
        location.assign("${home}/resource/update?id=" + id);
    }

    function doDelete(id, callback) {
        $.ServiceClient.invoke("${home}/resource/delete/" + id + ".json", {
            complete: function (data) {
                if (data.success) {
                    //$(".table").table('reload');
                    callback();
                } else {
                    layer.msg(data.errmsg, {icon: 2});
                }
            }
        });
    }

    function doExpand(parent_id, index, callback) {
        $.ServiceClient.invoke("${home}/resource/find.json", {
            data: {
                "portal_id": $(".panel-heading .nav-tabs li.active").data("id"),
                "parent_id": parent_id || 0
            },
            complete: function (data) {
                if (data.success) {
                    $(".table").table('data', data.page.rows, index);
                    callback();
                } else {
                    layer.msg(data.errmsg, {icon: 2});
                }
            }
        });
    }

    function expandTo(path) {
        var ids = path.split("/"), index = 1, current = 0;

        function expand() {
            var id = ids[index], $node = $(".tree-ctrl[data-id='" + id + "']");
            if (!$node.data("loaded")) {
                if (id != current) {
                    current = id;
                    $(".tree-fork", $node).click();
                }
                setTimeout(expand, 100);
            } else {
                index++;
                setTimeout(expand, 100);
            }
        }

        expand();
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
        var colorClasses = ["darken", "red"];

        //展开时用表格组件加载数据
        $(".table").table({
            inModel: [
                {
                    align: "left",
                    formatter: function (row) {
                        return '<span class="txt-color-' + colorClasses[row.path.split("/").length % colorClasses.length] + '">' + row.priority + '</span>';
                    }
                }, {
                    align: "left",
                    formatter: function (row) {
                        var path = row.path.split("/"), indent = "", clazz = "";
                        for (var i = 1; i < path.length - 1; i++) {
                            indent += '<span></span>';
                            clazz += " row" + path[i];
                        }
                        return '<span class="tree-ctrl' + clazz + '" data-id="' + row.id + '" data-loaded="' + (row.hasChildren ? false : true) + '"><span class="tree-indent">' +
                                indent + '</span><span class="tree-fork ' + (row.hasChildren ? "fork-close" : "") + '"></span>' +
                                '<span class="' + row.icon + '"></span></span>'
                                + '<span class="name">' + row.name + '</span>'
                                + '&nbsp;&nbsp;&nbsp;&nbsp;<a class="create-child" href="javascript:" data-id=' + row.id + '><i class="fa fa-plus"></i>增加下级</a>';
                    }
                }, {
                    align: "left",
                    formatter: function (row) {
                        return row.code;
                    }
                }, {
                    align: "left",
                    formatter: function (row) {
                        return row.parent_name;
                    }
                }, {
                    align: "left",
                    formatter: function (row) {
                        return row.is_show == 1 ? '<label class="label label-success">显示</label>' : '<label class="label label-default">隐藏</label>';
                    }
                }, {
                    align: "center",
                    formatter: function (row) {
                        return '<div class="lk-btn-group" data-id="' + row.id + '"><a href="javascript:" class="update">编辑</a><a href="javascript:" class="delete">删除</a></div>';
                    }
                }
            ]
        });

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
            layer.confirm("确认删除资源[" + getName($this) + "]？", {
                icon: 3,
                title: '提示'
            }, function (index) {
                doDelete(getId($this), function () {
                    var $pnode = $this.closest("tr").prev("tr").find(".tree-ctrl"), pid = $pnode.data("id");
                    $this.closest("tr").remove();
                    if ($(".row" + pid).length == 0) {
                        $pnode.find(".tree-fork").removeClass("fork-open");
                    }
                });
                layer.close(index);
            });
        }).on("click", ".create-child", function (e) {//增加下级
            e.preventDefault();
            doCreate($(this).data("id"));
        }).on("click", ".tree-fork", function (e) {//展开/折叠
            var $this = $(this), $node = $(this).parent();
            if ($this.hasClass("fork-close")) {
                if ($node.data("loaded") === false) {
                    doExpand($node.data("id"), $this.closest("tr").get(0).rowIndex - 1, function () {
                        $node.data("loaded", true);
                        $this.removeClass("fork-close").addClass("fork-open");
                    });
                }
            } else if ($this.hasClass("fork-open")) {
                $this.removeClass("fork-open").addClass("fork-close");
                $node.data("loaded", false);
                $(".row" + $node.data("id")).each(function () {
                    $(this).closest("tr").remove();
                });
            }
        });

        expandTo("${path}");
    })
</script>
</body>
</html>