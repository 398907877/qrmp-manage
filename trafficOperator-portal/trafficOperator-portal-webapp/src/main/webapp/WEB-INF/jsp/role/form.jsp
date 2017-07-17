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
<c:import url="/sidebar?menu=role"/>
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
                                <li data-id="${portal.id}">
                                    <a href="${home}/role/list?portal_id=${portal.id}"> <span
                                            class=""> <c:out value="${portal.name}"/> </span> </a>
                                </li>
                            </c:forEach>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty role.id?"修改":"新增"}角色</span></a>
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
                            <input type="hidden" id="id" name="id" value="${role.id}">

                            <legend class="hidden">
                                角色信息
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">所属门户</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" id="portal_id" name="portal_id"
                                               value="${role.portal_id}">
                                        <p class="form-control-static"><c:out value="${role.portal_name}"/></p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">角色名称</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="name" name="name" class="form-control" maxlength="20"
                                               placeholder="请输入角色名称" value="<c:out value="${role.name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">角色编码</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="code" name="code" class="form-control" maxlength="20"
                                               placeholder="请输入角色编码" value="<c:out value="${role.code}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <label class="checkbox-inline">
                                            <input type="checkbox" class="checkbox" name="is_show" id="is_show"
                                                   value="1" ${role.is_show==1?"checked='checked'":""}> <span>是否显示</span>
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">排序</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="priority" name="priority" class="form-control"
                                               maxlength="4"
                                               placeholder="请输入自然数排序序号，系统将会根据排序从小到大进行排列" value="${role.priority}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">备注</label>
                                    <div class="col-sm-10">
                                        <textarea id="remarks" name="remarks" class="form-control" maxlength="500"
                                                  placeholder="请输入备注"><c:out value="${role.remarks}"/></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <label class="checkbox-inline">
                                            <input type="checkbox" class="checkbox" name="is_admin" id="is_admin"
                                                   value="1" ${role.is_admin==1?"checked='checked'":""}> <span>默认管理员</span>
                                        </label>
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
        $.ServiceClient.invoke("${home}/role/${!empty role.id?"update.json":"create.json"}", {
            data: {
                "id": parseInt($("#id").val()),
                "name": $("#name").val(),
                "code": $("#code").val(),
                "remarks": $("#remarks").val(),
                "priority": $("#priority").val(),
                "is_show": ($("#is_show").is(":checked") ? 1 : 0),
                "is_admin": ($("#is_admin").is(":checked") ? 1 : 0),
                "portal_id": $("#portal_id").val()
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("角色保存成功！", {
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
        $.goBack("${referer}", "${home}/role/list", {portal_id: $("#portal_id").val()});
    }

    //初始化验证数据
    function initValidation() {
        return $('#oForm').validate({
            rules: {
                name: {required: true},
                code: {
                    required: true,
                    unique: {
                        url: "${home}/role/check_code.json",     //后台处理程序
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
                remarks: {maxlength: 500}
            },
            messages: {
                name: {required: "请输入角色名称"},
                code: {required: "请输入角色编码", unique: "编码已经存在"},
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