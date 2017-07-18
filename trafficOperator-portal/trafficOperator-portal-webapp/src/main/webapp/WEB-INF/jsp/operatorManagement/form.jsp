<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<body>

<!-- #PAGE HEADER -->
<c:import url="/header"/>
<!-- END PAGE HEADER -->

<!-- #NAVIGATION -->
<c:import url="/sidebar?menu=yysxxgl"/>
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
                                <a href="${home}/operatorManagement/list"><span>运营商列表</span></a>
                            </li>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty operatorInformation.id?"修改":"新增"}运营商</span></a>
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
                            <input type="hidden" id="id" name="id" value="${operatorInformation.id}">
                            

                            <legend class="hidden">
                                运营商信息2
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">交通运营商名称</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="account" name="account" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.account}"/>">
                                        <p class="note"><strong>Note:</strong> 交通运营商名称</p>
                                    </div>
                                </div>
                                
                                
                                
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">所在地市（省市）</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="account" name="account" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.account}"/>">
                                        <p class="note"><strong>Note:</strong> 所在地市（省市）</p>
                                    </div>
                                </div>
                                
                                
                                
                                
                                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">运营商简介</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="account" name="account" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.account}"/>">
                                        <p class="note"><strong>Note:</strong> 运营商简介</p>
                                    </div>
                                </div>
                                
                                
                                
                                
                                                             
                                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">服务状态（正常、终止）</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="account" name="account" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.account}"/>">
                                        <p class="note"><strong>Note:</strong> 服务状态（正常、终止）</p>
                                    </div>
                                </div>
                                
                                
                                
                                
                                                                                  
                                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">最后修改用户</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="account" name="account" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.account}"/>">
                                        <p class="note"><strong>Note:</strong> 最后修改用户</p>
                                    </div>
                                </div>
                                
                                
                                
                                
                                                                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">修改时间</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="account" name="account" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.account}"/>">
                                        <p class="note"><strong>Note:</strong> 修改时间</p>
                                    </div>
                                </div>
                                
                                
                                
                                
                                                               
                                
                                
                                                                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">新增联系人</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="account" name="account" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.account}"/>">
                                        <p class="note"><strong>Note:</strong>新增联系人</p>
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
        $.ServiceClient.invoke("${home}/staff/${!empty staff.id?"update.json":"create.json"}", {
            data: {
                "id": parseInt($("#id").val()),
                "corp_id": parseInt($("#corp_id").val()),
                "account": $.trim($("#account").val()),
                "role_id": $.map($("input[name=role_id]:checked"), function (input, i) {
                    return parseInt(input.value)
                }),
                "name": $.trim($("#name").val()),
                "pinyin": $("#pinyin").val(),
                "pyabbr": $("#pyabbr").val(),
                "dept_id": parseInt($("#dept_id").val()),
                "duty": $.trim($("#duty").val()),
                "cellphone": $.trim($("#cellphone").val()),
                "email": $.trim($("#email").val()),
                "priority": parseInt($("#priority").val()) || 0

            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("人员保存成功！", {
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
        $.goBack("${referer}", "${home}/staff/list");
    }

    function doGetPinyin($pinyin, words) {
        $pinyin.empty();
        if (!words) return;
        $.ServiceClient.invoke("${home}/pinyin/" + encodeURI(words) + ".json", {
            data: {},
            complete: function (data) {
                if (data.success) {
                    $pinyin.append($.map(data.pinyinList, function (py) {
                        return '<option value="' + py.complete + '" data-simple="' + py.simple + '">' + py.complete + '</option>';
                    }).join()).change();
                } else {
                    //layer.alert(data.errmsg, {icon: 2});
                }
            }
        });
    }

    //初始化验证数据
    function initValidation() {
        return $('#oForm').validate({
            rules: {
                account: {
                    required: true,
                    unique: {
                        url: "${home}/staff/check_account.json",     //后台处理程序
                        type: "get",                                     //数据发送方式
                        dataType: "json",                                 //接受数据格式
                        contentType: "application/json",                  //请求数据格式
                        data: {                                           //要传递的数据
                            id: function () {
                                return $("#id").val();
                            },
                            account: function () {
                                return $("#account").val();
                            }
                        }
                    }
                },
                role_id: {required: true},
                name: {required: true},
                dept_id: {required: true},
                cellphone: {
                    required: true,
                    mobile: true,
                    unique: {
                        url: "${home}/staff/check_cellphone.json",     //后台处理程序
                        type: "get",                                     //数据发送方式
                        dataType: "json",                                 //接受数据格式
                        contentType: "application/json",                  //请求数据格式
                        data: {                                           //要传递的数据
                            id: function () {
                                return $("#id").val();
                            },
                            cellphone: function () {
                                return $("#cellphone").val();
                            }
                        }
                    }
                },
                email: {required: true, email: true}
            },
            messages: {
                account: {required: "请输入人员用户名", unique: "用户名已经存在"},
                role_id: {required: "请选择人员角色"},
                name: {required: "请输入人员姓名"},
                dept_id: {unique: "请输入人员所属部门"},
                cellphone: {required: "请输入手机号码", mobile: "手机号码格式不正确"},
                email: {required: "请输入电子邮箱", email: "电子邮箱格式不正确"}
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
        }).on("change", "#name", function (e) {
            doGetPinyin($("#pinyin"), $(this).val());
        }).on("change", "#pinyin", function () {
            $("#pyabbr").val($(this).find(":selected").data("simple"));
        });
    })
</script>
</body>
</html>