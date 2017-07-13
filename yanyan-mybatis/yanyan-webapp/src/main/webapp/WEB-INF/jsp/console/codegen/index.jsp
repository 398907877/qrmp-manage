<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" href="${home}/assets/plugins/select2/css/select2.min.css">
    <style type="text/css">
        .basic, .constraint, .edit, .list {
            display: none;
            width: 0px;
        }

        .basic-on .basic, .constraint-on .constraint, .edit-on .edit, .list-on .list {
            display: table-cell;
            width: auto;
        }
    </style>

</head>
<body>

<!-- #PAGE HEADER -->
<c:import url="/header"/>
<!-- END PAGE HEADER -->

<!-- #NAVIGATION -->
<c:import url="/sidebar?menu=codegen"/>
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
                        代码生成器
                        <div class="btn-group pull-right">
                            <button type="button" class="btn btn-default toggle-btn" title="折叠"><i
                                    class="fa fa-minus "></i></button>
                            <button type="button" class="btn btn-default zoom-btn" title="全屏"><i
                                    class="fa fa-expand "></i></button>
                        </div>
                    </div>
                    <div class="panel-body">
                        <form id="oForm" class="form-horizontal" role="form">

                            <legend class="hidden">
                                代码配置信息
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">数据库</label>
                                    <div class="col-sm-6">
                                        <select id="schemaName" name="schemaName" class="form-control select2"
                                                data-placeholder="请选择数据库" data-val="">
                                            <option value=''></option>
                                            <c:forEach items="${schemas}" var="schema">
                                                <option value="${schema.name}">${schema.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="help-block">实体所在数据库</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">表</label>
                                    <div class="col-sm-6">
                                        <select id="tableName" name="tableName" class="form-control select2"
                                                data-placeholder="请选择表">
                                        </select>
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="help-block">实体对应的表</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <span class="help-block">生成结构：{包名}/{模块名}/{分层(data/domain,data/query,persist,service,web/controller)}/{子模块名}/{实体类名(/Query/Dao/Service/Controller)}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">项目包名</label>
                                    <div class="col-sm-6">
                                        <input type="text" id="packageName" name="packageName"
                                               class="form-control" maxlength="100"
                                               placeholder="请输入项目包名"
                                               value="">
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="help-block">项目的顶层包名，一般使用公司网址倒写，如com.oracle</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">模块名</label>
                                    <div class="col-sm-6">
                                        <input type="text" id="moduleName" name="moduleName"
                                               class="form-control" maxlength="100"
                                               placeholder="请输入模块名"
                                               value="">
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="help-block">实体所在模块的包名，如system或system.portal等</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">功能/实体类名</label>
                                    <div class="col-sm-6">
                                        <input type="text" id="name" name="name"
                                               class="form-control" maxlength="100"
                                               placeholder="请输入功能/实体类名"
                                               value="">
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="help-block">使用驼峰式写法，如User、UserGroup</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">功能/实体名称</label>
                                    <div class="col-sm-6">
                                        <input type="text" id="title" name="title"
                                               class="form-control" maxlength="100"
                                               placeholder="请输入功能/实体名称"
                                               value="">
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="help-block">用一个词或一个短语描述功能/实体，如用户、用户组</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">功能描述</label>
                                    <div class="col-sm-6">
                                        <textarea id="desc" name="desc"
                                                  class="form-control" maxlength="500"
                                                  placeholder="请输入功能块的用途"
                                                  value=""></textarea>
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="help-block">功能的用途</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">作者</label>
                                    <div class="col-sm-6">
                                        <input type="text" id="author" name="author"
                                               class="form-control" maxlength="64"
                                               placeholder="请输入代码的作者"
                                               value="">
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="help-block">代码的作者</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">功能风格</label>
                                    <div class="col-sm-10">
                                        <label class="checkbox-inline">
                                            <input type="checkbox" class="checkbox" id="isFlagDelete" checked="checked"
                                                   name="isFlagDelete"
                                                   value=""> <span>标记删除(需要有is_del字段)</span>
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox" class="checkbox" id="isPaginate" checked="checked"
                                                   name="isPaginate"
                                                   value=""> <span>分页</span>
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox" class="checkbox" id="isTree" checked="checked"
                                                   name="isTree"
                                                   value=""> <span>树形结构(需要有parent_id、path、level字段)</span>
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <ul id="fieldPropToggle" class="nav nav-tabs tabs-pull-right in">
                                        <li data-class="list-on">
                                            <a href="#" data-toggle="tab"><span>查询</span></a>
                                        </li>
                                        <li data-class="edit-on">
                                            <a href="#" data-toggle="tab"><span>增改</span></a>
                                        </li>
                                        <li data-class="constraint-on">
                                            <a href="#" data-toggle="tab"><span>约束</span></a>
                                        </li>
                                        <li data-class="basic-on" class="active">
                                            <a href="#" data-toggle="tab"><span>基本</span></a>
                                        </li>
                                    </ul>
                                    <div style="width: 100%; overflow-x: auto">
                                        <table class="table table-striped basic-on">
                                            <thead>
                                            <tr>
                                                <th style="width: 132px">表名</th>
                                                <th style="width: 132px">字段名</th>
                                                <th style="width: 132px">Java属性名</th>
                                                <th style="width: 132px">名称</th>
                                                <th class="basic text-center">Java类型</th>
                                                <th class="basic text-center">传输类型</th>
                                                <th class="basic text-center">描述</th>
                                                <th class="constraint text-center">@NotNull</th>
                                                <th class="constraint text-center">@NotBlank</th>
                                                <th class="constraint text-center">@Size</th>
                                                <th class="constraint text-center">@URL</th>
                                                <th class="constraint text-center">@Email</th>
                                                <th class="constraint text-center">@Mobile</th>
                                                <th class="constraint text-center">groups</th>
                                                <th class="edit text-center">插入</th>
                                                <th class="edit text-center">更新</th>
                                                <th class="edit text-center">检查唯一</th>
                                                <th class="list text-center">条件</th>
                                                <th class="list text-center">关键字</th>
                                                <th class="list text-center">ORDER BY</th>
                                                <th></th>
                                            </tr>
                                            </thead>
                                            <tbody id="fields">

                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </fieldset>
                            <div class="form-group">
                                <div class="col-sm-offset-10 col-sm-2">
                                    <button id="btnSave" class="btn btn-primary">
                                        生成
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
<script type="text/javascript" src="${home}/assets/plugins/select2/js/select2.full.min.js"></script>
<script type="text/javascript" src="${home}/assets/plugins/select2/js/i18n/zh-CN.min.js"></script>
<script type="text/javascript">
    function doSave() {
        $.ServiceClient.invoke("${home}/codegen/generate.json", {
            data: {
                "tableName": $("#tableName").val(),
                "slaveTable": null,
                "name": $.trim($("#name").val()).toLowerCase(),
                "title": $.trim($("#title").val()),
                "desc": $.trim($("#desc").val()),
                "author": $.trim($("#author").val()),
                "packageName": $.trim($("#packageName").val()),
                "moduleName": $.trim($("#moduleName").val()),
                "isFlagDelete": $("#isFlagDelete").is(":checked"),
                "isPaginate": $("#isPaginate").is(":checked"),
                "isTree": $("#isTree").is(":checked"),
                "fields": $.map($("#fields>tr"), function (tr) {
                    var tds = $(tr).children("td"), column = $(tr).find(".dbcolumn>option:selected").data("column");
                    return {
                        "tableName": tds.eq(0).find("select").val(),
                        "columnName": tds.eq(1).find("select").val(),
                        "name": tds.eq(2).find("input").val().toLowerCase(),
                        "title": tds.eq(3).find("input").val(),
                        "type": tds.eq(4).find("select").val(),
                        "transfer": tds.eq(5).find("select").val(),
                        "desc": tds.eq(6).find("textarea").val(),
                        "constraints": $.grep([
                            tds.eq(7).find("input").is(":checked") ? "{name: 'NotNull', message: '{" + $.trim($("#name").val()).toLowerCase() + "." + tds.eq(1).find("select").val() + ".empty}', groups: '" + tds.eq(13).find("select").val() + "'}" : "",
                            tds.eq(8).find("input").is(":checked") ? "{name: 'NotBlank', message: '{" + $.trim($("#name").val()).toLowerCase() + "." + tds.eq(1).find("select").val() + ".blank}', groups: '" + tds.eq(13).find("select").val() + "'}" : "",
                            tds.eq(9).find("input").is(":checked") ? "{name: 'Size', message: '{" + $.trim($("#name").val()).toLowerCase() + "." + tds.eq(1).find("select").val() + ".out.of.size}', max: '" + column.maxlen + "', groups: '" + tds.eq(13).find("select").val() + "'}" : "",
                            tds.eq(10).find("input").is(":checked") ? "{name: 'URL', message: '{" + $.trim($("#name").val()).toLowerCase() + "." + tds.eq(1).find("select").val() + ".invalid.url}', groups: '" + tds.eq(13).find("select").val() + "'}" : "",
                            tds.eq(11).find("input").is(":checked") ? "{name: 'Email, message: '{" + $.trim($("#name").val()).toLowerCase() + "." + tds.eq(1).find("select").val() + ".invalid.email}', groups: '" + tds.eq(13).find("select").val() + "'}'" : "",
                            tds.eq(12).find("input").is(":checked") ? "{name: 'Mobile', message: '{" + $.trim($("#name").val()).toLowerCase() + "." + tds.eq(1).find("select").val() + ".invalid.mobile}', groups: '" + tds.eq(13).find("select").val() + "'}" : "",
                        ], function (n) {
                            return !!n
                        }),
                        "isInsert": tds.eq(14).find("input").is(":checked"),
                        "isUpdate": tds.eq(15).find("input").is(":checked"),
                        "checkUnique": tds.eq(16).find("input").is(":checked"),
                        "queryType": tds.eq(17).find("select").val(),
                        "isKeyword": tds.eq(18).find("input").is(":checked"),
                        "orderBy": tds.eq(19).find("select").val(),
                    }
                })
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("代码生成成功！", {
                        icon: 1, time: 2000, end: function () {
                            //location.assign("${home}/garden/list");
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

    var javaTypes = [
        {name: 'Integer', value: 'java.lang.Integer'},
        {name: 'Long', value: 'java.lang.Long'},
        {name: 'Float', value: 'java.lang.Float'},
        {name: 'Double', value: 'java.lang.Double'},
        {name: 'String', value: 'java.lang.String'},
        {name: 'Date', value: 'java.util.Date'},
        {name: 'byte[]', value: 'java.util.byte[]'}
    ], transfers = [
        {name: '输入输出', value: 'InputOutput'},
        {name: '仅输入', value: 'InputOnly'},
        {name: '仅输出', value: 'OutputOnly'},
        {name: '不传输(临时变量)', value: 'NoTransfer'},
        {name: '<不作为字段>', value: 'None'}
    ], groups = [
        {name: '', value: ''},
        {name: 'Create', value: 'Create.class'},
        {name: 'Update', value: 'Update.class'}
    ], queryTypes = [
        {name: '<无>', value: '0'},
        {name: '精确查询=', value: '1'},
        {name: '范围&lt;=AND&gt=;', value: '2'},
        {name: '集合in', value: '3'},
        {name: '模糊like', value: '4'}
    ], orders = [
        {name: '', value: ''},
        {name: 'ASC', value: 'asc'},
        {name: 'DESC', value: 'desc'}
    ], mysql2java = {
        'TINYINT': 'java.lang.Integer',
        'SMALLINT': 'java.lang.Integer',
        'MEDIUMINT': 'java.lang.Integer',
        'BOOLEAN': 'java.lang.Integer',
        'BIT': 'java.lang.Integer',

        'BIGINT': 'java.lang.Long',
        'ID': 'java.lang.Long',
        'INT': 'java.lang.Long',

        'FLOAT': 'java.lang.Float',

        'DOUBLE': 'java.lang.Double',
        'DECIMAL': 'java.lang.Double',

        'DATE': 'java.util.Date',
        'TIME': 'java.util.Date',
        'DATETIME': 'java.util.Date',
        'TIMESTAMP': 'java.util.Date',
        'YEAR': 'java.util.Date'
    };

    function extractPhase(comment) {
        var i = 0, len = comment.length, c = '', phase = '';
        while (i < len) {
            c = comment.charAt(i);
            if ($.inArray(c, ['，', ',', '。', '.', '（', '(', '\r', '\n']) >= 0) {
                break;
            }
            phase += c;
            i++;
        }

        return phase;
    }

    function underlineToCamel(param) {
        if (!param) {
            return "";
        }
        var len = param.length, sb = '';
        for (var i = 0; i < len; i++) {
            var c = param.charAt(i);
            if (c == "_") {
                if (++i < len) {
                    sb += param.charAt(i).toUpperCase();
                }
            } else {
                sb += c;
            }
        }
        return sb;
    }

    function paintTableSelect(el, schemaName, val) {
        //var data = [];
        $(el).empty();
        if (schemaName) {
            $.ServiceClient.invoke("${home}/codegen/" + schemaName + "/tables.json", {
                data: {},
                complete: function (data) {
                    $(el).append("<option value=''></option>");
                    if (data.success) {
                        $.each(data.tables, function (i, table) {
                            $('<option value="' + table.name + '">' + table.name + (table.comment ? '(' + extractPhase(table.comment) + ')' : '') + '</option>').data('table', table).appendTo(el);
                        })
                    }
                    $(el).val(val).trigger("change");
                }
            });
        } else {
            $(el).append("<option value=''></option>");//为显示placeholder
            $(el).val(val).trigger("change");
        }
    }

    function createFieldHtml(column) {
        var tableNameHtml = '<select class="select2 dbtable" style="width:120px" data-placeholder="">' + (column ? '<option value="' + $("#tableName").val() + '">' + $("#tableName").val() + '</option>' : $("#tableName").html()) + '</select>',
                columnNameHtml = '<select class="select2 dbcolumn"  style="width:120px">' + (column ? '<option value="' + column.name + '">' + column.name + '</option>' : '') + '</select>',
                nameHtml = '<input type=text style="width:120px">',
                typeHtml = '<select class="select2 java-type"  style="width:120px" data-placeholder="">' +
                        $.map(javaTypes, function (type) {
                            return '<option value=' + type.value + '>' + type.name + '</option>';
                        }) +
                        '</select>',
                titleHtml = '<input type=text style="width:120px">',
                descHtml = '<textarea style="width:120px"></textarea>',
                transferHtml = '<select class="select2"  style="width:120px" data-placeholder="">' +
                        $.map(transfers, function (type) {
                            return '<option value=' + type.value + '>' + type.name + '</option>';
                        }) +
                        '</select>',
                groupsHtml = '<select class="select2"  style="width:100px" data-placeholder="">' +
                        $.map(groups, function (type) {
                            return '<option value=' + type.value + '>' + type.name + '</option>';
                        }) +
                        '</select>',
                queryTypeHtml = '<select class="select2" style="width: 120px" data-placeholder="">' +
                        $.map(queryTypes, function (type) {
                            return '<option value=' + type.value + '>' + type.name + '</option>';
                        }) +
                        '</select>',
                orderByHtml = '<select class="select2" style="width: 120px" data-placeholder="">' +
                        $.map(orders, function (type) {
                            return '<option value=' + type.value + '>' + type.name + '</option>';
                        }) +
                        '</select>';


        return '<tr>' +
                '    <td>' + tableNameHtml + '</td>' +
                '    <td>' + columnNameHtml + '</td>' +
                '    <td>' + nameHtml + '</td>' +
                '    <td>' + titleHtml + '</td>' +
                '    <td class="basic text-center">' + typeHtml + '</td>' +
                '    <td class="basic text-center">' + transferHtml + '</td>' +
                '    <td class="basic text-center">' + descHtml + '</td>' +
                '    <td class="constraint text-center"><input type="checkbox"></td>' +
                '    <td class="constraint text-center"><input type="checkbox"></td>' +
                '    <td class="constraint text-center at-size"><input type="checkbox"></td>' +
                '    <td class="constraint text-center"><input type="checkbox"></td>' +
                '    <td class="constraint text-center"><input type="checkbox"></td>' +
                '    <td class="constraint text-center"><input type="checkbox"></td>' +
                '    <td class="constraint text-center">' + groupsHtml + '</td>' +
                '    <td class="edit text-center"><input type="checkbox"></td>' +
                '    <td class="edit text-center"><input type="checkbox"></td>' +
                '    <td class="edit text-center"><input type="checkbox"></td>' +
                '    <td class="list text-center">' + queryTypeHtml + '</td>' +
                '    <td class="list text-center"><input type="checkbox"></td>' +
                '    <td class="list text-center">' + orderByHtml + '</td>' +
                '    <td></td>' +
                '</tr>';
    }

    function paintFieldGrid(el, schemaName, tableName) {
        $(el).empty();
        if (schemaName && tableName) {
            $.ServiceClient.invoke("${home}/codegen/" + schemaName + "/" + tableName + "/columns.json", {
                data: {},
                complete: function (data) {
                    if (data.success) {
                        var tree = 0;
                        $.each(data.columns, function (i, column) {
                            var $tr = $(createFieldHtml(column)).appendTo(el);
                            $tr.find(".select2").select2();
                            $tr.children("td").eq(1).find("select").find("option").data('column', column).end().change();
                            if (column.name == 'is_del') {
                                $("#isFlagDelete").prop("checked", true);
                            } else if (column.name == 'parent_id') {
                                tree++;
                            } else if (column.name == 'path') {
                                tree++;
                            } else if (column.name == 'level') {
                                tree++;
                            }
                        })
                        if (tree == 3) {
                            $("#isTree").prop("checked", true);
                        }
                    }
                }
            });
        }
    }

    function paintColumnSelect(el, schemaName, tableName, val) {
        $(el).empty();
        if (schemaName || tableName) {
            $.ServiceClient.invoke("${home}/codegen/" + schemaName + "/" + tableName + "/columns.json", {
                data: {},
                complete: function (data) {
                    $(el).append("<option value=''></option>");
                    if (data.success) {
                        $.each(data.columns, function (i, column) {
                            $('<option value="' + column.name + '">' + column.name + '</option>').data('column', column).appendTo(el);
                        })
                    }
                    $(el).val(val).trigger("change");
                }
            });
        } else {
            $(el).append("<option value=''></option>");//为显示placeholder
            $(el).val(val).trigger("change");
        }
    }

    function setDomainDefaultValue(table) {
        if (table) {
            $("#name").val(underlineToCamel(table.name.substr(table.name.indexOf("_"))));
            $("#title").val(extractPhase(table.comment));
            $("#desc").val(table.comment);
        } else {
            $("#name").val("");
            $("#title").val("");
            $("#desc").val("");
        }
        $("#isFlagDelete").prop("checked", false);
        $("#isPaginate").prop("checked", true);
        $("#isTree").prop("checked", false);
    }

    function setFieldDefaultValue($tr, column) {
        var tds = $tr.children("td");
        tds.eq(2).find("input").val(column.name);
        tds.eq(3).find("input").val(extractPhase(column.comment));
        tds.eq(4).find("select").val(mysql2java[column.type.toUpperCase()] || 'java.lang.String').change();
        tds.eq(5).find("select").val('InputOutput').change();
        tds.eq(6).find("textarea").val(column.comment);
        tds.eq(7).find("input").prop("checked", false);
        tds.eq(8).find("input").prop("checked", false);
        tds.eq(9).find("input").prop("checked", tds.eq(4).find("select").val() == 'java.lang.String');
        tds.eq(10).find("input").prop("checked", false);
        tds.eq(11).find("input").prop("checked", false);
        tds.eq(12).find("input").prop("checked", false);
        tds.eq(13).find("select").val("").change();
        tds.eq(14).find("input").prop("checked", true);
        tds.eq(15).find("input").prop("checked", true);
        tds.eq(16).find("input").prop("checked", false);
        tds.eq(17).find("select").val("0").change();
        tds.eq(18).find("input").prop("checked", false);
        tds.eq(19).find("select").val("");
    }

    //初始化验证数据
    function initValidation() {
        return $('#oForm').validate({
            rules: {
                name: {required: true},
                title: {required: true},
                author: {required: true},
                tableName: {required: true}

            },
            messages: {
                name: {required: "请输入功能/实体类名"},
                title: {required: "请输入功能/实体名称"},
                author: {required: "请输入作者"},
                tableName: {required: "请选择表"}
            }
        });
    }

    $(function () {
        $(".select2").select2();
        var validator = initValidation();
        $(document).on("click", "#btnSave", function (e) {
            e.preventDefault();
            if (validator.form()) {
                doSave();
            }
        }).on('change', ".select2", function () {
            if (this.id == "schemaName") {
                paintTableSelect($("#tableName"), $(this).val(), "");
            } else if (this.id == "tableName") {
                setDomainDefaultValue($("#tableName").find("option:selected").data("table"));
                paintFieldGrid($("#fields"), $("#schemaName").val(), $("#tableName").val());
            } else if ($(this).hasClass("dbtable")) {
                paintColumnSelect($(this).parents("tr").children("td").eq(1).find("select"));
            } else if ($(this).hasClass("dbcolumn")) {
                setFieldDefaultValue($(this).parents("tr"), $(this).find("option:selected").data("column"));
            } else if ($(this).hasClass("java-type")) {
                $(this).parents("tr").find("at-size").prop("checked", $(this).val() == "java.lang.String");
            }
        }).on('click', "#fieldPropToggle>li", function () {
            $(".table").removeClass("basic-on").removeClass("constraint-on").removeClass("edit-on").removeClass("list-on").addClass($(this).data("class"));
        });
    })
</script>
</body>
</html>