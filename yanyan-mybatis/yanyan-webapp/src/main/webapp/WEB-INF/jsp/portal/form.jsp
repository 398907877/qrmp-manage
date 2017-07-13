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
<c:import url="/sidebar?menu=portal"/>
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
                                <a href="${home}/portal/list"><span>门户列表</span></a>
                            </li>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty portal.id?"修改":"新增"}门户</span></a>
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
                            <input type="hidden" id="id" name="id" value="${portal.id}">

                            <legend class="hidden">
                                门户信息
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">门户名称</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="name" name="name" class="form-control" maxlength="20"
                                               placeholder="请输入门户名称" value="<c:out value="${portal.name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">门户编码</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="code" name="code" class="form-control" maxlength="20"
                                               placeholder="请输入门户编码" value="<c:out value="${portal.code}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">排序</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="priority" name="priority" class="form-control"
                                               maxlength="4"
                                               placeholder="请输入自然数排序序号，系统将会根据排序从小到大进行排列" value="${portal.priority}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">备注</label>
                                    <div class="col-sm-10">
                                        <textarea id="remarks" name="remarks" class="form-control" maxlength="500"
                                                  placeholder="请输入备注"><c:out value="${portal.remarks}"/></textarea>
                                    </div>
                                </div>
                                <hr>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">APP KEY</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="app_key" name="app_key" class="form-control"
                                               maxlength="11" placeholder="请输入APP KEY"
                                               value="<c:out value="${portal.app_key}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">APP SECRET</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="app_secret" name="app_secret" class="form-control"
                                               maxlength="64"
                                               placeholder="请输入APP SECRET"
                                               value="<c:out value="${portal.app_secret}"/>">
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
<script type="text/javascript">
    function doSave() {
        $.ServiceClient.invoke("${home}/portal/${!empty portal.id?"update.json":"create.json"}", {
            data: {
                "id": parseInt($("#id").val()),
                "name": $.trim($("#name").val()),
                "code": $.trim($("#code").val()),
                "priority": parseInt($("#priority").val()) || 0,
                "remarks": $("#remarks").val(),
                "app_key": $.trim($("#app_key").val()),
                "app_secret": $.trim($("#app_secret").val())
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("门户保存成功！", {
                        icon: 1, time: 2000, end: function () {
                            doBack();
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
        $.goBack("${referer}", "${home}/portal/list");
    }

    //初始化验证数据
    function initValidation() {
        return $('#oForm').validate({
            rules: {
                name: {required: true},
                code: {
                    required: true,
                    unique: {
                        url: "${home}/portal/check_code.json",     //后台处理程序
                        type: "get",                                     //数据发送方式
                        dataType: "json",                                 //接受数据格式
                        contentType: "application/json",                  //请求数据格式
                        data: {                                           //要传递的数据
                            id: function () {
                                return $("#id").val();
                            },
                            code: function () {
                                return $("#code").val();
                            }
                        }
                    }
                },
                remarks: {maxlength: 500},
                app_key: {
                    unique: {
                        url: "${home}/portal/check_app_key.json",   //后台处理程序
                        type: "get",                                      //数据发送方式
                        dataType: "json",                                  //接受数据格式
                        contentType: "application/json",                   //请求数据格式
                        data: {                                            //要传递的数据
                            id: function () {
                                return $("#id").val();
                            },
                            app_key: function () {
                                return $("#app_key").val();
                            }
                        }
                    }
                },
                app_secret: {}
            },
            messages: {
                name: {required: "请输入门户名称"},
                code: {required: "请输入门户编码", unique: "编码已经存在"},
                remarks: {maxlength: "备注长度须小于500"},
                app_key: {unique: "APP KEY已经存在"}
            }
        });
    }

    $(function () {
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