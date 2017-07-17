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
<c:import url="/sidebar?menu=logger"/>
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
                            <li class="active">
                                <a href="javascript:"><span>日志设置</span></a>
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
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-1">名称</label>
                                <div class="col-sm-3">
                                    <input id="name" name="name" type="text" class="form-control" placeholder="请输入名称查找">
                                </div>
                                <label class="control-label col-sm-1">级别</label>
                                <div class="col-sm-2">
                                    <select id="level" name="level" class="form-control">
                                        <option value='OFF'>OFF</option>
                                        <option value='ALL'>ALL</option>
                                        <option value='TRACE'>TRACE</option>
                                        <option value='DEBUG'>DEBUG</option>
                                        <option value='INFO'>INFO</option>
                                        <option value='WARN'>WARN</option>
                                        <option value='ERROR'>ERROR</option>
                                    </select>
                                </div>
                                <label class="control-label col-sm-1">输出文件</label>
                                <div class="col-sm-2">
                                    <input id="filename" name="filename" type="text" class="form-control"
                                           placeholder="">
                                </div>
                                <div class="col-sm-2">
                                    <button id="btnCreate" class="btn btn-default">设置</button>
                                    <button id="btnReset" class="btn btn-default">重置</button>
                                </div>
                            </div>
                        </form>
                        <div class="table-responsive">
                            <table id="table" class="table table-striped table-hover no-margin">
                                <thead>
                                <tr>
                                    <th width="40%">名称</th>
                                    <th width="10%">级别</th>
                                    <th width="50%">输出文件</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${loggers}" var="logger">
                                    <tr>
                                        <td class="text-left">
                                            <c:out value="${logger.name}"/>
                                        </td>
                                        <td class="text-left">
                                            <c:out value="${logger.level}"/>
                                        </td>
                                        <td class="text-left">
                                            <c:forEach items="${logger.files}" var="file" varStatus="varStatus">
                                                <c:out value="${file}"/><c:if test="${!varStatus.last}">,</c:if>
                                            </c:forEach>
                                        </td>
                                        <td class="text-center">
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="alert alert-info no-margin text-center no-records ${empty loggers?"":"hidden"}">
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
    function doQuery() {
        $.ServiceClient.invoke("${home}/console/logger/find.json", {
            data: {
                //name: $("#name").val()
            },
            complete: function(data){
                if (data.success) {
                    $(".table").table("data", data.loggers);
                } else {
                    layer.alert(data.errmsg, {icon: 2});
                }
            }
        });
    }

    function doSet() {
        $.ServiceClient.invoke("${home}/console/logger/set.json", {
            data: {
                name: $("#name").val(),
                level: $("#level").val(),
                files: $("#filename").val().split(",")
            },
            complete: function(data){
                if (data.success) {
                    doQuery();
                } else {
                    layer.alert(data.errmsg, {icon: 2});
                }
            }
        });
    }

    function doFilter(name) {
        var keyword = name.toLowerCase();
        var table = $(".table>tbody").get(0);
        var ele;
        for (var r = 0; r < table.rows.length; r++) {
            ele = table.rows[r].cells[0].innerHTML.replace(/<[^>]+>/g, "");
            if (ele.toLowerCase().indexOf(keyword) >= 0) {
                table.rows[r].style.display = '';
            } else {
                table.rows[r].style.display = 'none';
            }
        }
    }

    function getName(el) {
        return $(el).closest("tr").find(".name").text();
    }

    function getId(el) {
        return $(el).closest(".lk-btn-group").data("id");
    }

    $(function () {
        $(document).on("click", "#btnQuery", function (e) {//查询
            e.preventDefault();
            doQuery();
        }).on("click", "#btnCreate", function (e) {//创建
            e.preventDefault();
            doSet();
        }).on("click", "#btnReset", function(e){
            e.preventDefault();
            $("#name").val("");
            $("#level").val("");
            $("#filename").val("");
        }).on("dblclick", ".table>tbody>tr", function(e){
            var $tr = $(this), tds = $tr.children("td");
            $("#name").val($.trim(tds.eq(0).text()));
            $("#level").val($.trim(tds.eq(1).text()));
            $("#filename").val($.trim(tds.eq(2).text()));
        }).on("keypress", "#name", function (e) {
            e.stopPropagation();
        }).on("keyup", "#name", function (e) {//编辑
            doFilter($.trim(this.value));
        });

        //展开时用表格组件加载数据
        $(".table").table({
            inModel: [
                {
                    align: "left",
                    formatter: function (row) {
                        return row.name;
                    }
                }, {
                    align: "left",
                    formatter: function (row) {
                        return row.level;
                    }
                }, {
                    align: "left",
                    formatter: function (row) {
                        var html = "";
                        $.each(row.files, function (i, file) {
                            html += file;
                        })
                        return $("<div>").text(html).html();
                    }
                }, {
                    align: "left",
                    formatter: function (row) {
                        return '';
                    }
                }
            ]
        });
    })
</script>
</body>
</html>