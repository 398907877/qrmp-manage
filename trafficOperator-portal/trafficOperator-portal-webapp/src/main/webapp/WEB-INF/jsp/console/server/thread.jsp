<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<body class="">

<!-- #MAIN PANEL -->
<div class="main">

    <!-- #MAIN CONTENT -->
    <div class="content">
        <div class="row bg-color-white">
            <div class="col-sm-12 text-center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <button class="btn btn-primary all-detail">获取所有线程执行详情</button>
                    </div>
                </form>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12 table-responsive">
                <table id="table" class="table table-striped table-hover no-margin">
                    <thead>
                    <tr>
                        <th width="10%">线程ID</th>
                        <th width="45%">线程名</th>
                        <th width="10%" class="text-center">运行状态</th>
                        <th width="15%" class="text-right">CPU时间ms</th>
                        <th width="10%" class="text-right">CPU占用率%</th>
                        <th width="10%"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${threads}" var="thread">
                        <tr>
                            <td class="text-left">
                                <c:out value="${thread.id}"/>
                            </td>
                            <td class="text-left word-wrap name">
                                <c:out value="${thread.name}"/>
                            </td>
                            <td class="text-center">
                                <c:out value="${thread.state}"/>
                            </td>
                            <td class="text-right">
                                <fmt:formatNumber value="${thread.cpuTime}"/>
                            </td>
                            <td class="text-right">
                                <fmt:formatNumber minFractionDigits="2" maxFractionDigits="2"
                                                  value="${thread.cpuPercent*100}"/>
                            </td>
                            <td class="text-center">
                                <div class="lk-btn-group" data-id="${thread.id}">
                                    <a href="javascript:" class="detail">执行详情</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <!-- END #MAIN CONTENT -->

</div>
<!-- END #MAIN PANEL -->

<!--================================================== -->

<c:import url="/libs-js"/>
<%//此处插入页面自定义的脚本%>
<script src="${home}/assets/plugins/jquery.table.js"></script>
<script type="text/javascript">
    function doQuery() {
        $.ServiceClient.invoke("${home}/console/server/threads.json", {
            quiet: true,
            data: {
                //name: $("#name").val()
            },
            complete: function (data) {
                if (data.success) {
                    $(".table").table("data", data.threads);
                    parent.$("iframe").trigger("load");
                } else {
                    //layer.alert(data.errmsg, {icon: 2});
                }
            }
        });
    }

    function getName(el) {
        return $(el).closest("tr").find(".name").text();
    }

    function getId(el) {
        return $(el).closest(".lk-btn-group").data("id");
    }

    $(function () {
        //展开时用表格组件加载数据
        $(".table").table({
            inModel: [
                {
                    align: "left",
                    formatter: function (row) {
                        return row.id;
                    }
                }, {
                    align: "left",
                    formatter: function (row) {
                        return row.name;
                    }
                }, {
                    align: "center",
                    formatter: function (row) {
                        return row.state;
                    }
                }, {
                    align: "right",
                    formatter: function (row) {
                        return formatInt(row.cpuTime);
                    }
                }, {
                    align: "right",
                    formatter: function (row) {
                        return formatFloat(row.cpuPercent * 100, 2);
                    }
                }, {
                    align: "center",
                    formatter: function (row) {
                        return '<div class="lk-btn-group" data-id="' + row.id + '">' +
                                ' <a href="javascript:" class="detail">执行详情</a> ' +
                                '</div>'
                    }
                }
            ]
        });

        var opener = top || window;

        $(document).on("click", ".detail", function (e) {
            e.preventDefault();
            var index = opener.layer.open({
                type: 2,
                title: "线程[" + getName($(this)) + "]详情",
                shadeClose: true,
                shade: 0.8,
                area: [opener.$(opener).width() + "px", opener.$(opener).height() + "px"],
                content: [$.contextPath + "/console/server/thread_detail?id=" + getId($(this)), 'yes'],
                scrollbar: false,
                maxmin: true
            })
            opener.layer.full(index);
        }).on("click", ".all-detail", function (e) {
            e.preventDefault();
            var index = opener.layer.open({
                type: 2,
                title: "线程[所有]详情",
                shadeClose: true,
                shade: 0.8,
                area: [opener.$(opener).width() + "px", opener.$(opener).height() + "px"],
                content: [$.contextPath + "/console/server/thread_detail", 'yes'],
                scrollbar: false,
                maxmin: true
            })
            opener.layer.full(index);
        })

        setInterval(doQuery, 1000);
    })
</script>
</body>
</html>