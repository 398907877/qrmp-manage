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
    <link rel="stylesheet" href="${home}/assets/plugins/bootstrap-tagsinput/bootstrap-tagsinput.custom.css">
    <link rel="stylesheet" href="${home}/assets/plugins/bootstrap-typeahead/bootstrap-typeahead.custom.css">

</head>
<body>

<!-- #PAGE HEADER -->
<c:import url="/header"/>
<!-- END PAGE HEADER -->

<!-- #NAVIGATION -->
<c:import url="/sidebar?menu=department"/>
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
                            <li>
                                <a href="${home}/department/list"><span>部门列表</span></a>
                            </li>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty department.id?"修改":"新增"}部门</span></a>
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
                            <input type="hidden" id="id" name="id" value="${department.id}">
                            <input type="hidden" id="corp_id" name="corp_id" value="${department.corp_id}">

                            <legend class="hidden">
                                部门信息
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">部门名称</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="name" name="name" class="form-control" maxlength="20"
                                               placeholder="请输入部门名称" value="<c:out value="${department.name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">拼音</label>
                                    <div class="col-sm-10">
                                        <select id="pinyin" name="pinyin" class="form-control" placeholder="请先输入部门名称">
                                            <option value="${department.pinyin}">${department.pinyin}</option>
                                        </select>
                                        <input type="hidden" id="pyabbr" name="pyabbr" class="form-control"
                                               maxlength="128" value="${department.pyabbr}">
                                        <p class="note"><strong>Note:</strong> 可能有多音字，请选择合适的拼音组合。</p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">上级部门</label>
                                    <div class="col-sm-10">
                                        <select id="parent_id" name="parent_id" class="form-control"
                                                placeholder="请选择上级部门">
                                            <option value="0"></option>
                                            <c:forEach items="${departments}" var="dept">
                                                <c:set var="indent" value=""></c:set>
                                                <c:forEach begin="2" end="${dept.level}">
                                                    <c:set var="indent"
                                                           value="${indent}&nbsp;&nbsp;&nbsp;&nbsp;"></c:set>
                                                </c:forEach>
                                                <option value="${dept.id}" ${dept.id == department.parent_id?"selected='selected'":""}>${indent}
                                                    <c:out value="${dept.name}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">部门主管</label>
                                    <div class="col-sm-10">
                                        <select id="manager_id" name="manager_id" class="form-control tagsinput"
                                                placeholder="输入人员姓名拼音或手机号查找" multiple>
                                            <c:forEach items="${department.managers}" var="manager">
                                                <option value="${manager.id}" selected>${manager.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">排序</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="priority" name="priority" class="form-control"
                                               maxlength="4"
                                               placeholder="请输入自然数排序序号，系统将会根据排序从小到大进行排列" value="${department.priority}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">备注</label>
                                    <div class="col-sm-10">
                                        <textarea id="remarks" name="remarks" class="form-control" maxlength="500"
                                                  placeholder="请输入备注"><c:out value="${department.remarks}"/></textarea>
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
<script type="application/javascript"
        src="${home}/assets/plugins/bootstrap-tagsinput/bootstrap-tagsinput.min.js"></script>
<script type="application/javascript" src="${home}/assets/plugins/bootstrap-typeahead/typeahead.bundle.min.js"></script>
<script type="text/javascript">
    var recommendManagers = [];
    <c:forEach items="${managers}" var="staff">
    recommendManagers.push({
        id: <c:out value="${staff.id}"/>,
        name: "<c:out value="${staff.name}"/>",
        account: "<c:out value="${staff.account}"/>",
        dept_name: "<c:out value="${staff.dept_name}"/>",
        cellphone: "<c:out value="${staff.cellphone}"/>"
    });
    </c:forEach>
    function doSave() {
        $.ServiceClient.invoke("${home}/department/${!empty department.id?"update.json":"create.json"}", {
            data: {
                "id": parseInt($("#id").val()),
                "corp_id": parseInt($("#corp_id").val()),
                "name": $.trim($("#name").val()),
                "pinyin": $.trim($("#pinyin").val()),
                "pyabbr": $.trim($("#pyabbr").val()),
                "remarks": $("#remarks").val(),
                "parent_id": parseInt($("#parent_id").val()),
                "manager_id": $("#manager_id").val(),
                "priority": parseInt($("#priority").val()) || 0
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("保存成功！", {
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
        $.goBack("${referer}", "${home}/department/list", {expand_to: $("#id").val()});
    }

    function doGetPinyin($pinyin, words) {
        $pinyin.empty();
        if (!words) return;
        $.ServiceClient.invoke("${home}/pinyin/" + encodeURI(words) + ".json", {
            quiet: true,
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
        jQuery.validator.addMethod("notBeSelf", function (value, element) {
            return this.optional(element) || (value != $("#id").val());
        }, "上级部门不能是本身");

        return $('#oForm').validate({
            rules: {
                name: {required: true},
                parent_id: {notBeSelf: true},
                remarks: {maxlength: 500}
            },
            messages: {
                name: {required: "请输入部门名称"},
                //parent_id: {required: "请选择上级部门"},
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
        }).on("change", "#name", function (e) {
            doGetPinyin($("#pinyin"), $(this).val());
        }).on("change", "#pinyin", function () {
            $("#pyabbr").val($(this).find(":selected").data("simple"));
        });

        var display = function (item) {
            var dept_name = item.dept_name || '', name = item.name || '', account = item.account || '',
                    cellphone = item.cellphone || '', text = '';
            dept_name && (text += "[" + dept_name + "]")
            name && (text += name)
            account && (text += "(" + account + ")")
            cellphone && (text += cellphone)
            return text || item
        }

        $(".tagsinput").tagsinput({
            itemValue: 'id',
            itemText: 'name',
            trimValue: true,
            itemTitle: display,
            typeaheadjs: [{
                minLength: 0//没有输入关键字时加载默认人员
            }, {
                templates: {
                    notFound: function () {
                        return "&nbsp;没有找到人员";
                    },
                    pending: function () {
                        return '&nbsp;<i class="fa fa-spinner fa-spin"></i>&nbsp;数据加载中...'
                    }
                },
                matcher: function (item) {
                    var name = item.name || '', account = item.account || '',
                            cellphone = item.cellphone || '', pinyin = item.pinyin || '', pyabbr = item.pyabbr || '';
                    return ~name.toLowerCase().indexOf(this.query.toLowerCase()) || ~account.toLowerCase().indexOf(this.query.toLowerCase()) || ~cellphone.toLowerCase().indexOf(this.query.toLowerCase()) || ~pinyin.toLowerCase().indexOf(this.query.toLowerCase()) || ~pyabbr.toLowerCase().indexOf(this.query.toLowerCase());
                },
                display: display,
                source: function (query, sync, async) {
                    if (query) {
                        $.ServiceClient.invoke("${home}/staff/find.json", {
                            quiet: true,//不触发全局事件，则不会有遮罩
                            data: {
                                keyword: query,
                                pageSize: 20,//最多查出20个
                                pageNo: 1
                            },
                            success: function (data) {
                                async(data.page.rows);
                            }
                        })
                    } else {
                        //没有查询条件的时候，输出推荐的人员
                        //这里需要异步，否则会停留在pending
                        setTimeout(function () {
                            async(recommendManagers)
                        }, 1);
                    }
                }
            }]
        });
    })
</script>
</body>
</html>