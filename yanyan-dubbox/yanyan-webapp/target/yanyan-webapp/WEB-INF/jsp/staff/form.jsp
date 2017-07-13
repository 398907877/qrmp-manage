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
<c:import url="/sidebar?menu=staff"/>
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
                                <a href="${home}/staff/list"><span>人员列表</span></a>
                            </li>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty staff.id?"修改":"新增"}人员</span></a>
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
                            <input type="hidden" id="id" name="id" value="${staff.id}">
                            <input type="hidden" id="corp_id" name="corp_id" value="${staff.corp_id}">

                            <legend class="hidden">
                                人员信息
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">用户名</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="account" name="account" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${staff.account}"/>">
                                        <p class="note"><strong>Note:</strong> 初始密码为${DEFAULT_PASSWORD}。</p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">角色</label>
                                    <div class="col-sm-10">
                                        <c:forEach items="${roles}" var="role">
                                            <label class="checkbox-inline">
                                                <input type="checkbox" class="checkbox" name="role_id"
                                                       value="${role.id}"
                                                    <c:set var="exit" value="${fn:length(staff.roles)}"></c:set>
                                                <c:forEach items="${staff.roles}" var="r" end="${exit}">
                                                <c:if test="${role.id==r.id}">
                                                       checked="checked"
                                                    <c:set var="exit" value="0"></c:set>
                                                </c:if>
                                                </c:forEach>
                                                <c:if test="${role.is_admin==1&&staff.is_admin==1||role.is_show==0}">
                                                       disabled=disabled
                                                </c:if>
                                                > <span><c:out value="${role.name}"/></span>
                                            </label>
                                        </c:forEach>
                                    </div>
                                </div>
                                <hr>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">真实姓名</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="name" name="name" class="form-control" maxlength="20"
                                               placeholder="请输入人员姓名" value="<c:out value="${staff.name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">拼音</label>
                                    <div class="col-sm-10">
                                        <select id="pinyin" name="pinyin" class="form-control" placeholder="请先输入人员姓名">
                                            <option value="${staff.pinyin}">${staff.pinyin}</option>
                                        </select>
                                        <input type="hidden" id="pyabbr" name="pyabbr" class="form-control"
                                               maxlength="128" value="${staff.pyabbr}">
                                        <p class="note"><strong>Note:</strong> 可能有多音字，请选择合适的拼音组合。</p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">所属部门</label>
                                    <div class="col-sm-10">
                                        <select id="dept_id" name="dept_id" class="form-control" placeholder="请选择部门">
                                            <c:forEach items="${departments}" var="dept">
                                                <c:set value="${fn:split(dept.path, '/')}" var="paths"/>
                                                <c:set var="indent" value=""></c:set>
                                                <c:forEach items="${paths}" varStatus="varStatus">
                                                    <c:if test="${!varStatus.first}">
                                                        <c:set var="indent"
                                                               value="${indent}&nbsp;&nbsp;&nbsp;&nbsp;"></c:set>
                                                    </c:if>
                                                </c:forEach>
                                                <option value="${dept.id}" ${dept.id == staff.dept_id?"selected='selected'":""}>${indent}
                                                    <c:out value="${dept.name}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">职务</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="duty" name="duty" class="form-control" maxlength="128"
                                               placeholder="请输入人员职务" value="<c:out value="${staff.duty}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">手机</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="cellphone" name="cellphone" class="form-control"
                                               maxlength="128"
                                               placeholder="请输入人员手机" value="<c:out value="${staff.cellphone}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">电子邮箱</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="email" name="email" class="form-control" maxlength="128"
                                               placeholder="请输入人员电子邮箱" value="<c:out value="${staff.email}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">排序</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="priority" name="priority" class="form-control"
                                               maxlength="4"
                                               placeholder="请输入自然数排序序号，系统将会根据排序从小到大进行排列" value="${staff.priority}">
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