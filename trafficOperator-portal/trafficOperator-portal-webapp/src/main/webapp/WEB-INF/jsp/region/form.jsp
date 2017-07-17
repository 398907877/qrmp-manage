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
<c:import url="/sidebar?menu=region"/>
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
                        <ul class="nav nav-tabs pull-left in path">
                            <li data-id="0">
                                <a href="${home}/region/list"><span>国家列表</span></a>
                            </li>
                            <c:if test="${region.parent_id!=0}">
                                <li class='dropdown path-separator' data-id='0'><a href='javascript:'
                                                                                   class='dropdown-toggle'
                                                                                   data-toggle='dropdown'><b
                                        class='fa fa-fw fa-caret-right'></b></a>
                                    <ul class='dropdown-menu'>
                                        <li><a href="javascript:"><i
                                                class="fa fa-spinner fa-pulse"></i><span>加载中...</span></a></li>
                                    </ul>
                                </li>
                            </c:if>
                            <c:set var="level_name" value="国家"></c:set>
                            <c:if test="${!empty parents}">
                                <c:forEach items="${parents}" var="r" varStatus="varStatus">
                                    <li data-id="${r.id}">
                                        <a href="${home}/region/list?parent_id=${r.id}"><span>${r.name}</span></a>
                                    </li>
                                    <c:if test="${!varStatus.last}">
                                        <li class='dropdown path-separator' data-id='${r.id}'><a href='javascript:'
                                                                                                 class='dropdown-toggle'
                                                                                                 data-toggle='dropdown'><b
                                                class='fa fa-fw fa-caret-right'></b></a>
                                            <ul class='dropdown-menu'>
                                                <li><a href="javascript:"><i class="fa fa-spinner fa-pulse"></i><span>加载中...</span></a>
                                                </li>
                                            </ul>
                                        </li>
                                    </c:if>
                                    <c:if test="${varStatus.last}">
                                        <c:choose>
                                            <c:when test="${r.level==1}">
                                                <c:set var="level_name" value="省份"></c:set>
                                            </c:when>
                                            <c:when test="${r.level==2}">
                                                <c:set var="level_name" value="地市"></c:set>
                                            </c:when>
                                            <c:when test="${r.level==3}">
                                                <c:set var="level_name" value="区/县"></c:set>
                                            </c:when>
                                            <c:when test="${r.level==4}">
                                                <c:set var="level_name" value="街道/社区"></c:set>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="level_name" value="地区"></c:set>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty region.id?"修改":"新增"}${level_name}</span></a>
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
                            <input type="hidden" id="id" name="id" value="${region.id}">

                            <legend class="hidden">
                                ${level_name}信息
                            </legend>
                            <fieldset>
                                <div class="form-group ${region.parent_id==0?"hidden":""}">
                                    <label class="control-label col-sm-2">上级地区</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" id="parent_id" name="parent_id"
                                               value="${region.parent_id}">
                                        <p class="form-control-static"><c:out value="${region.parent_name}"/></p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">${level_name}代码</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="code" name="code" class="form-control" maxlength="20"
                                               placeholder="请输入${level_name}编码" value="<c:out value="${region.code}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">${level_name}名称</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="name" name="name" class="form-control" maxlength="20"
                                               placeholder="请输入${level_name}名称" value="<c:out value="${region.name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">拼音</label>
                                    <div class="col-sm-10">
                                        <select id="pinyin" name="pinyin" class="form-control"
                                                placeholder="请先输入${level_name}名称">
                                            <option value="${region.pinyin}">${region.pinyin}</option>
                                        </select>
                                        <input type="hidden" id="pyabbr" name="pyabbr" class="form-control"
                                               maxlength="128" value="${region.pyabbr}">
                                        <p class="note"><strong>Note:</strong> 可能有多音字，请选择合适的拼音组合。</p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">排序</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="priority" name="priority" class="form-control"
                                               maxlength="4"
                                               placeholder="请输入自然数排序序号，系统将会根据排序从小到大进行排列" value="${region.priority}">
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
        $.ServiceClient.invoke("${home}/region/${!empty region.id?"update.json":"create.json"}", {
            data: {
                "id": parseInt($("#id").val()),
                "name": $("#name").val(),
                "code": $("#code").val(),
                "parent_id": $("#parent_id").val(),
                "pinyin": $("#pinyin").val(),
                "pyabbr": $("#pyabbr").val(),
                "priority": $("#priority").val()
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("${level_name}保存成功！", {
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
        $.goBack("${referer}", "${home}/region/list", {parent_id: $("#parent_id").val()});
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

    function doDropdown($dropdown) {
        if (!$dropdown.data("loaded") && !$dropdown.data("loading")) {
            $dropdown.data("loading", true);
            $.ServiceClient.invoke("${home}/region/find.json", {
                data: {
                    "parent_id": $dropdown.data("id") || 0
                },
                complete: function (data) {
                    if (data.success) {
                        var $menu = $dropdown.find(".dropdown-menu");
                        $menu.empty();
                        $.each(data.page.rows, function (i, region) {
                            $('<li><a href="${home}/region/list?parent_id=' + region.id + '">' + region.name + '</a></li>').appendTo($menu);
                        });
                        $dropdown.data("loaded", true);
                    } else {
                        //layer.msg(data.errmsg, {icon: 2});
                    }
                    $dropdown.data("loading", false);
                }
            });
        }
    }

    //初始化验证数据
    function initValidation() {
        return $('#oForm').validate({
            rules: {
                name: {required: true},
                code: {
                    required: true,
                    unique: {
                        url: "${home}/region/check_code.json",     //后台处理程序
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
                name: {required: "请输入${level_name}名称"},
                code: {required: "请输入${level_name}编码", unique: "编码已经存在"}
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
        }).on("change blur", "#name", function (e) {
            doGetPinyin($("#pinyin"), $(this).val());
        }).on("change", "#pinyin", function () {
            $("#pyabbr").val($(this).find(":selected").data("simple"));
        }).on("show.bs.dropdown", ".dropdown", function (e) {
            doDropdown($(this));
            $(this).find(".fa-caret-right").removeClass("fa-caret-right").addClass("fa-caret-down");
        }).on("hide.bs.dropdown", ".dropdown", function (e) {
            $(this).find(".fa-caret-down").removeClass("fa-caret-down").addClass("fa-caret-right");
        });
    })
</script>
</body>
</html>