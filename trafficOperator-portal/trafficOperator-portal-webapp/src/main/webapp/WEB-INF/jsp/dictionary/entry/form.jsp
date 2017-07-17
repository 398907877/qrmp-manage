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
<c:import url="/sidebar?menu=dictionary"/>
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
                                <a href="${home}/dictionary/entry/list"><span>条目列表</span></a>
                            </li>
                            <li>
                                <a href="${home}/dictionary/group/list"><span>分组列表</span></a>
                            </li>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty entry.id?"修改":"新增"}条目</span></a>
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
                            <input type="hidden" id="id" name="id" value="${entry.id}">

                            <legend class="hidden">
                                条目信息
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">所属分组</label>
                                    <div class="col-sm-10">
                                        <select id="group_id" name="group_id" class="form-control" placeholder="请选择所属分组">
                                            <option value=""></option>
                                            <c:forEach items="${groups}" var="group">
                                            <option value="${group.id}" ${group.id==entry.group_id?"selected='selected'":""}><c:out value="${group.name}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">条目名称</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="name" name="name" class="form-control" maxlength="20"
                                               placeholder="请输入条目名称" value="<c:out value="${entry.name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">条目编码</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="code" name="code" class="form-control" maxlength="128"
                                               placeholder="请输入条目编码" value="<c:out value="${entry.code}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">条目值</label>
                                    <div class="col-sm-10">
                                        <textarea id="value" name="value" class="form-control" maxlength="4000"
                                               placeholder="请输入条目值"><c:out value="${entry.value}"/></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">排序</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="priority" name="priority" class="form-control"
                                               maxlength="4"
                                               placeholder="请输入自然数排序序号，系统将会根据排序从小到大进行排列" value="${entry.priority}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">备注</label>
                                    <div class="col-sm-10">
                                        <textarea id="remarks" name="remarks" class="form-control" maxlength="500"
                                                  placeholder="请输入备注"><c:out value="${entry.remarks}"/></textarea>
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
        $.ServiceClient.invoke("${home}/dictionary/entry/${!empty entry.id?"update.json":"create.json"}", {
            data: {
                "id": parseInt($("#id").val()),
                "name": $.trim($("#name").val()),
                "code": $.trim($("#code").val()),
                "priority": parseInt($("#priority").val()) || 0,
                "remarks": $("#remarks").val(),
                "group_id": $("#group_id").val(),
                "value": $.trim($("#value").val())
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("条目保存成功！", {
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

    function doBack(){
        $.goBack("${referer}", "${home}/dictionary/entry/list");
    }

    //初始化验证数据
    function initValidation() {
        return $('#oForm').validate({
            rules: {
                name: {required: true},
                code: {
                    required: true,
                    unique: {
                        url: "${home}/dictionary/entry/check_code.json",     //后台处理程序
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
                name: {required: "请输入条目名称"},
                code: {required: "请输入条目编码", unique: "编码已经存在"},
                remarks: {maxlength: "备注长度须小于500"}
            }
        });
    }

    $(function () {
        var validator = initValidation();
        $(document).on("click", "#btnSave", function(e){
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