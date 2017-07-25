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
<c:import url="/sidebar?menu=password"/>
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
                        密码修改
                        <div class="btn-group pull-right">
                            <button type="button" class="btn btn-default toggle-btn" title="折叠"><i
                                    class="fa fa-minus "></i></button>
                            <button type="button" class="btn btn-default zoom-btn" title="全屏"><i
                                    class="fa fa-expand "></i></button>
                        </div>
                    </div>
                    <div class="panel-body">
                        <form id="oForm" role="form">
                            <input type="hidden" id="staff_id" value="${staffId}">

                            <legend class="hidden">
                                密码修改
                            </legend>
                            <div class="form-group">
                                <label class="control-label">当前登录用户</label>
                                <p id="name" class="form-control-static"><c:out value="${staffName}"/></p>
                            </div>
                            <div class="form-group">
                                <label class="control-label">原始密码</label>
                                <input type="password" class="form-control" id="old_password" name="old_password"
                                       maxlength="16" placeholder="请输入您当前的登录密码">

                            </div>
                            <div class="form-group">
                                <label class="control-label">新登录密码</label>
                                <input type="password" class="form-control" id="new_password" name="new_password"
                                       maxlength="16" placeholder="6~16位字符，字母区分大小写，不能包含空格。">
                            </div>
                            <div class="form-group">
                                <label class="control-label">确认新登录密码</label>
                                <input type="password" class="form-control" id="confirm_password"
                                       name="confirm_password" maxlength="16" placeholder="请再次确认您的新登录密码">
                            </div>

                            <button type="submit" class="btn btn-primary">保存修改</button>
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
<!-- RSA Encrypt and Decrypt -->
<script src="${home}/assets/plugins/rsa/Barrett.min.js" charset="UTF-8"></script>
<script src="${home}/assets/plugins/rsa/BigInt.min.js" charset="UTF-8"></script>
<script src="${home}/assets/plugins/rsa/RSA.min.js" charset="UTF-8"></script>
<script type="text/javascript">
    function encryptPassword(password, publicExponent, modulus) {
        setMaxDigits(130);
        var key = new RSAKeyPair(publicExponent, '', modulus);
        password = encryptedString(key, password); //不支持汉字

        return password;
    }

    function getKey() {
        return $.ServiceClient.invoke("${home}/login_key.json", {
            data: {}
        });
    }

    function doSave() {
        $.when(getKey()).then(function (data) {
            return $.ServiceClient.invoke("${home}/user/change_password.json", {
                data: {
                    "new_password": encryptPassword($("#new_password").val(), data.publicKey.publicExponent, data.publicKey.modulus),
                    "old_password": encryptPassword($("#old_password").val(), data.publicKey.publicExponent, data.publicKey.modulus),
                    "staff_id": parseInt($("#staff_id").val())
                }
            });
        }).done(function (data) {
            $("input").val("");
            layer.alert("密码修改成功！", {
                icon: 1, time: 2000, end: function () {
                    location.reload();
                }
            }, function (index) {
                layer.close(index);
            });
        }).fail(function (data) {
            layer.alert(data.errmsg, {icon: 2});
        });
    }

    //初始化验证数据
    function initValidation() {
        $.validator.addMethod("pwd", function (value, element, params) {
            var reg = params;
            return reg.test(value);
        }, "包含有非法字符");

        return $('#oForm').validate({
            rules: {
                old_password: {required: true},
                new_password: {required: true, minlength: 6, pwd: /^[A-Za-z0-9_-]{6,16}$/},
                confirm_password: {required: true, equalTo: "#new_password"}
            },
            messages: {
                old_password: "请输入当前登录密码",
                new_password: {
                    required: "请设置您的新登录密码",
                    minlength: "密码长度至少需要6位",
                    pwd: "包含有空格或其他不可见字符"
                },
                confirm_password: {
                    required: "请再次确认您的新登录密码",
                    equalTo: "确认新密码与新登录密码不一致"
                }
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