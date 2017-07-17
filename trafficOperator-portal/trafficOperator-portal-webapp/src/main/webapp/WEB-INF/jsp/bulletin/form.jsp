<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" href="${home}/assets/plugins/datepicker/css/bootstrap-datetimepicker.min.css"/>
</head>
<body>

<!-- #PAGE HEADER -->
<c:import url="/header"/>
<!-- END PAGE HEADER -->

<!-- #NAVIGATION -->
<c:import url="/sidebar?menu=bulletin"/>
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
                        <ul class="nav nav-tabs pull-left">
                            <li>
                                <a href="${home}/bulletin/list"><span>公告列表</span></a>
                            </li>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty bulletin.id?"修改":"新增"}公告</span></a>
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
                        <form id="oForm" class="form-horizontal" role="form">
                            <input type="hidden" id="id" name="id" value="${bulletin.id}">
                            <input type="hidden" id="corp_id" name="corp_id" value="${bulletin.corp_id}">

                            <legend class="hidden">
                                公告信息
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">公告标题</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="title" name="title" class="form-control" maxlength="100"
                                               placeholder="请输入公告名称" value="<c:out value="${bulletin.title}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">生效时间</label>
                                    <div class="col-sm-10">
                                        <div class='input-group date'>
                                            <input class="form-control" id="effective_time" name="effective_time"
                                                   placeholder="请选择生效时间"
                                                   value="<fmt:formatDate value="${bulletin.effective_time}" pattern="yyyy/MM/dd HH:mm"/>">
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">失效时间</label>
                                    <div class="col-sm-10">
                                        <div class='input-group date'>
                                            <input class="form-control" id="expiry_time" name="expiry_time"
                                                   placeholder="请选择失效时间"
                                                   value="<fmt:formatDate value="${bulletin.expiry_time}" pattern="yyyy/MM/dd HH:mm"/>">
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">公告内容</label>
                                    <div class="col-sm-10">
                                        <textarea id="content" name="content" class="form-control"
                                                  maxlength="1000" placeholder="请输入公告内容" rows="10"><c:out
                                                value="${bulletin.content}"/></textarea>
                                    </div>
                                </div>
                            </fieldset>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <button id="btnSave" class="btn btn-primary">
                                        确认
                                    </button>
                                    <button id="btnCancel" class="btn btn-default">
                                        取消
                                    </button>
                                </div>
                            </div>
                        </form>
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
<script src="${home}/assets/plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="${home}/assets/plugins/jquery-validation/localization/messages_zh.min.js"></script>
<script src="${home}/assets/plugins/jquery-validation/jquery.validate.custom.min.js"></script>
<script type="text/javascript" src="${home}/assets/plugins/moment/moment.min.js"></script>
<script type="text/javascript" src="${home}/assets/plugins/moment/locale/zh-cn.min.js"></script>
<script type="text/javascript" src="${home}/assets/plugins/datepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript">
    function doSave() {
        $.ServiceClient.invoke("${home}/bulletin/${!empty bulletin.id?"update.json":"create.json"}", {
            data: {
                "id": parseInt($("#id").val()),
                "corp_id": parseInt($("#corp_id").val()),
                "title": $.trim($("#title").val()),
                "effective_time": $("#effective_time").val(),
                "expiry_time": $("#expiry_time").val(),
                "content": $.trim($("#content").val())
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("${corp_type}保存成功！", {
                        icon: 1, time: 2000, end: function () {
                            doBack()
                        }
                    }, function (index) {
                        layer.close(index);
                    });
                } else {
                    layer.alert(data.errmsg, {icon: 2});
                }
            }
        });
    }

    function doBack() {
        $.goBack("${referer}", "${home}/bulletin/list");
    }

    //初始化验证数据
    function initValidation() {
        return $('#oForm').validate({
            rules: {
                title: {required: true},
                content: {required: true}
            },
            messages: {
                title: {required: "请输入公告标题"},
                content: {required: "请输入公告内容"}
            }
        });
    }

    $(function () {
        $(".date").datetimepicker({
            format: "YYYY/MM/DD HH:mm",
            useCurrent: false,
            showClear: true,
            sideBySide: true,
            locale: 'zh-cn',
            widgetPositioning: {
                horizontal: "left"
            }
        })

        var validator = initValidation();
        $(document).on("click", "#btnSave", function (e) {
            e.preventDefault();
            if (validator.form()) {
                doSave();
            }
        }).on("click", "#btnCancel", function (e) {
            e.preventDefault();
            doBack();
        });
    })
</script>
</body>
</html>