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
<c:import url="/sidebar?menu=resource"/>
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
                                <li>
                                    <a href="${home}/resource/list?portal_id=${portal.id}"> <span
                                            class=""> <c:out value="${portal.name}"/> </span> </a>
                                </li>
                            </c:forEach>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty resource.id?"修改":"新增"}资源</span></a>
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
                            <input type="hidden" id="id" name="id" value="${resource.id}">

                            <legend class="hidden">
                                资源信息
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">所属门户</label>
                                    <div class="col-sm-10">
                                        <c:if test="${empty resource.id}">
                                            <select id="portal_id" name="portal_id" class="form-control">
                                                <c:forEach items="${portals}" var="portal">
                                                    <option value="${portal.id}" ${resource.portal_id==portal.id?"selected='selected'":""}>${portal.name}</option>
                                                </c:forEach>
                                            </select>
                                        </c:if>
                                        <c:if test="${!empty resource.id}">
                                            <input type="hidden" id="portal_id" name="portal_id"
                                                   value="${resource.portal_id}">
                                            <p class="form-control-static"><c:out value="${resource.portal_name}"/></p>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">资源名称</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="name" name="name" class="form-control" maxlength="50"
                                               placeholder="请输入资源名称" value="<c:out value="${resource.name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">资源编码</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="code" name="code" class="form-control" maxlength="50"
                                               placeholder="请输入资源编码" value="<c:out value="${resource.code}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">上级资源</label>
                                    <div class="col-sm-10">
                                        <select id="parent_id" name="parent_id" class="form-control"
                                                placeholder="请选择上级资源">
                                            <option value="0"></option>
                                            <c:forEach items="${resources}" var="res">
                                                <c:set var="indent" value=""></c:set>
                                                <c:forEach begin="2" end="${res.level}">
                                                    <c:set var="indent"
                                                           value="${indent}&nbsp;&nbsp;&nbsp;&nbsp;"></c:set>
                                                </c:forEach>
                                                <option value="${res.id}" ${res.id == resource.parent_id?"selected='selected'":""}>${indent}
                                                    <c:out value="${res.name}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">资源地址</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="url" name="url" class="form-control" maxlength="100"
                                               placeholder="请输入资源地址" value="<c:out value="${resource.url}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">目标窗口</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="target" name="target" class="form-control" maxlength="100"
                                               placeholder="请输入资源地址" value="<c:out value="${resource.target}"/>" list="list">
                                        <datalist id="list">
                                            <option value="_blank">_blank</option>
                                            <option value="_media">_media</option>
                                            <option value="_parent">_parent</option>
                                            <option value="_search">_search</option>
                                            <option value="_self" selected>_self</option>
                                            <option value="_top">_top</option>
                                        </datalist>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">资源图标</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="icon" name="icon" class="form-control" maxlength="100"
                                               placeholder="请输入资源图标样式" value="<c:out value="${resource.icon}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">排序</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="priority" name="priority" class="form-control"
                                               maxlength="4"
                                               placeholder="请输入自然数排序序号，系统将会根据排序从小到大进行排列" value="${resource.priority}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">是否显示</label>
                                    <div class="col-sm-10">
                                        <label class="radio-inline">
                                            <input type="radio" class="radio" name="is_show" id="is_show1"
                                                   value="1" ${resource.is_show==1?"checked='checked'":""}>
                                            <span>是</span>
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" class="radio" name="is_show" id="is_show0"
                                                   value="0" ${resource.is_show==0?"checked='checked'":""}>
                                            <span>否</span>
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">备注</label>
                                    <div class="col-sm-10">
                                        <textarea id="remarks" name="remarks" class="form-control" maxlength="500"
                                                  placeholder="请输入备注"><c:out value="${resource.remarks}"/></textarea>
                                    </div>
                                </div>
                                <hr>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">权限列表</label>
                                    <div class="col-sm-10">
                                        <table id="permissions" class="table">
                                            <thead>
                                            <tr>
                                                <th>名称</th>
                                                <th>编码</th>
                                                <th>是否显示</th>
                                                <th>备注</th>
                                                <th></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${resource.permissions}" var="perm">
                                                <tr data-id="${perm.id}">
                                                    <td><input type="text"
                                                               value="<c:out value="${perm.name}"/>" ${perm.code=="view"?"disabled='disabled'":""}>
                                                    </td>
                                                    <td><input type="text"
                                                               value="<c:out value="${perm.code}"/>" ${perm.code=="view"?"disabled='disabled'":""}>
                                                    </td>
                                                    <td><select ${perm.code=="view"?"disabled='disabled'":""}>
                                                        <option value="1" ${perm.is_show==1?"selected='selected'":""}>是
                                                        </option>
                                                        <option value="0" ${perm.is_show==0?"selected='selected'":""}>否
                                                        </option>
                                                    </select></td>
                                                    <td><input type="text" value="<c:out value="${perm.remarks}"/>">
                                                    </td>
                                                    <td>

                                                        <div class="lk-btn-group"><i class="fa fa-plus"></i><c:if
                                                                test="${perm.code!='view'}"><i
                                                                class="fa fa-minus"></i><i
                                                                class="fa fa-arrow-up"></i><i
                                                                class="fa fa-arrow-down"></i></c:if></div>

                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                            <tfoot>
                                            <tr>
                                                <td colspan="5">
                                                    <p class="note"><strong>Note:</strong>&nbsp;&nbsp;1、建议编码采用易懂的单个单词，如：create-增加；delete-删除；update-修改；view-查看；audit-审核。
                                                    </p>
                                                    <p class="note">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、[view-查看]权限作为资源的基本访问权限，是必须的，不能删除和修改。</p>
                                                </td>
                                            </tr>
                                            </tfoot>
                                        </table>
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
        var permissions = [], success = true, i = 0;
        $("#permissions").find("tbody tr").each(function () {
            var $tds = $(this).children("td"), permission = {
                "id": parseInt($(this).data("id")) || "",
                "name": $.trim($tds.eq(0).find("input").val()),
                "code": $.trim($tds.eq(1).find("input").val()),
                "is_show": $tds.eq(2).find("select").val(),
                "remarks": $.trim($tds.eq(3).find("input").val()),
                "priority": i++
            };

            if (permission.id || permission.name || permission.code || permission.remarks) {
                if (!permission.name) {
                    $tds.eq(0).append("<small class='invalid help-block'>不能为空</small>")
                }

                if (!permission.code) {
                    $tds.eq(1).append("<small class='invalid help-block'>不能为空</small>")
                }

                permissions.push(permission);
            }
        });

        if (!success) {
            return false;
        }

        $.ServiceClient.invoke("${home}/resource/${!empty resource.id?"update.json":"create.json"}", {
            data: {
                "id": parseInt($("#id").val()),
                "portal_id": parseInt($("#portal_id").val()),
                "name": $.trim($("#name").val()),
                "code": $.trim($("#code").val()),
                "remarks": $("#remarks").val(),
                "parent_id": parseInt($("#parent_id").val()),
                "is_show": ($("#is_show1").is(":checked") ? 1 : 0),
                "priority": parseInt($("#priority").val()) || 0,
                "url": $.trim($("#url").val()),
                "target": $.trim($("#target").val()),
                "icon": $.trim($("#icon").val()),
                "permissions": permissions
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("资源保存成功！", {
                        icon: 1, time: 2000, end: function () {
                            data.id && $("#id").val(data.id)
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
        $.goBack("${referer}", "${home}/resource/list", {portal_id: $("#portal_id").val(), expand_to: $("#id").val()});
    }

    //初始化验证数据
    function initValidation() {
        jQuery.validator.addMethod("notBeSelf", function (value, element) {
            return this.optional(element) || (value != $("#id").val());
        }, "上级资源不能是本身");
        return $('#oForm').validate({
            rules: {
                portal_id: {required: true},
                name: {required: true},
                code: {
                    required: true,
                    unique: {
                        url: "${home}/resource/check_code.json",     //后台处理程序
                        type: "get",                                     //数据发送方式
                        dataType: "json",                                 //接受数据格式
                        contentType: "application/json",                  //请求数据格式
                        data: {                                           //要传递的数据
                            id: function () {
                                return $("#id").val();
                            },
                            portal_id: function () {
                                return $("#portal_id").val();
                            },
                            code: function () {
                                return $("#code").val();
                            }
                        }
                    }
                },
                parent_id: {notBeSelf: true},
                remarks: {maxlength: 500}
            },
            messages: {
                portal_id: {required: "请选择所属门户"},
                name: {required: "请输入资源名称"},
                code: {required: "请输入资源编码", unique: "编码已经存在"},
                remarks: {maxlength: "备注长度须小于500"}
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
        }).on("blur", "#permissions input", function () {
            if ($.trim($(this).val()) != "") {
                $(this).closest("tr").find(".invalid.help-block").remove();
            }
        }).on("click", "#permissions .fa-plus", function () {
            var $row = $(this).closest("tr"),
                    $new = '<tr>' +
                            '<td><input type="text" value=""></td>' +
                            '<td><input type="text" value=""></td>' +
                            '<td><select><option value="1" selected="selected">是</option><option value="0">否</option></select></td>' +
                            '<td><input type="text" value=""></td>' +
                            '<td>' +
                            '<div class="lk-btn-group"><i class="fa fa-plus"></i><i class="fa fa-minus"></i><i class="fa fa-arrow-up"></i><i class="fa fa-arrow-down"></i></div>' +
                            '</td>' +
                            '</tr>';
            $row.after($new);
        }).on("click", "#permissions .fa-minus", function () {
            var $row = $(this).closest("tr");
            if ($("#permissions.table>tbody>tr").size() == 1) {

            } else {
                $row.remove();
            }
        }).on("click", "#permissions .fa-arrow-up", function () {
            var $row = $(this).closest("tr"), $prev = $row.prev("tr");
            if ($prev.size() > 0 && $prev[0].rowIndex != 1) {//view总是排最前面
                $prev.before($row);
            }
        }).on("click", "#permissions .fa-arrow-down", function () {
            var $row = $(this).closest("tr"), $next = $row.next("tr");

            if ($next.size() > 0) {
                $next.after($row);
            }
        });
    })
</script>
</body>
</html>