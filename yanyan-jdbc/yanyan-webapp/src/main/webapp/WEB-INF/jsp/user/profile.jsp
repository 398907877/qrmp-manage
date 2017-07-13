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
<c:import url="/sidebar?menu=profile"/>
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
                        个人资料
                        <div class="btn-group pull-right">
                            <button type="button" class="btn btn-default toggle-btn" title="折叠"><i
                                    class="fa fa-minus "></i></button>
                            <button type="button" class="btn btn-default zoom-btn" title="全屏"><i
                                    class="fa fa-expand "></i></button>
                        </div>
                    </div>
                    <div class="panel-body">
                        <form id="oForm" class="form-horizontal" role="form">
                            <input type="hidden" id="id" name="id" value="${staff.id}">

                            <legend class="hidden">
                                个人资料
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">登录账号</label>
                                    <div class="col-sm-10">
                                        <p class="form-control-static">
                                            ${staff.account}${staff.is_admin==1?"&nbsp;<span class='label label-info arrowed arrow-left'>默认管理员</span>":""}
                                        </p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">角色</label>
                                    <div class="col-sm-10">
                                        <p class="form-control-static">
                                            <c:forEach items="${staff.roles}" var="role" varStatus="varStatus">
                                                ${role.name} ${!varStatus.last?"/":""}
                                            </c:forEach>
                                        </p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">部门</label>
                                    <div class="col-sm-10">
                                        <p class="form-control-static">
                                            <c:out value="${staff.dept_name}"/>
                                        </p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">姓名</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="name" name="name" class="form-control" maxlength="20"
                                               placeholder="请输入您的姓名" value="<c:out value="${staff.name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">手机号码</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="cellphone" name="cellphone" class="form-control"
                                               maxlength="11" placeholder="请输入您的手机号码" value="<c:out value="${staff.cellphone}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">电子邮箱</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="email" name="email" class="form-control" maxlength="64"
                                               placeholder="请输入您的电子邮箱" value="<c:out value="${staff.email}"/>">
                                    </div>
                                </div>
                            </fieldset>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <button type="submit" class="btn btn-primary">
                                        确认
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
        $.ServiceClient.invoke("${home}/user/update_profile.json", {
            data: {
                "id": parseInt($("#id").val()),
                "name": $.trim($("#name").val()),
                "cellphone": $.trim($("#cellphone").val()),
                "email": $.trim($("#email").val())
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("资料修改成功！", {
                        icon: 1, time: 2000, end: function () {
                            location.reload();
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

    //初始化验证数据
    function initValidation() {
        return $('#oForm').validate({
            rules: {
                name: {required: true},
                email: {email: true},
                cellphone: {mobile: true}
            },
            messages: {
                name: "请输入姓名",
                cellphone: "请输入正确的手机号码",
                email: "请输入合法的邮箱地址"
            }
        });
    }

    $(function () {
        var validator = initValidation();
        $("button:submit").click(function (e) {
            e.preventDefault();
            if (validator.form()) {
                doSave();
            }
        });
    })
</script>
</body>
</html>