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
    <link rel="stylesheet" href="${home}/assets/plugins/boostrap-tree/bootstrap-tree.custom.css">
    <style>
        .tree {
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            border-radius: 4px;
            -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            background-color: #fbfbfb;
            border: 1px solid #999;
            margin-bottom: 10px;
            max-height: 300px;
            min-height: 20px;
            overflow-y: auto;
            padding: 19px
        }
    </style>
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
                            <li id="btnGrant" class="active">
                                <a href="javascript:"><span>角色授权</span></a>
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
                                角色授权
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">角色名称</label>
                                    <div class="col-sm-10">
                                        <p class="form-control-static"><c:out value="${role.name}"/></p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">权限集合</label>
                                    <div class="col-sm-10">
                                        <button id="btnAll" class="btn btn-link">全选</button>|<button id="btnNone" class="btn btn-link">不选</button>
                                        <div class="tree" data-link-type='{"Y":"p","N":"s"}'>
                                            <ul>
                                                <li data-id="0"><span><i class="fa icon-minus-sign"></i>&nbsp;<i
                                                        class="fa fa-lg fa-globe"></i><c:out
                                                        value="${role.portal_name}"/></span>
                                                    <c:set var="treeNodes" value="${permissions}" scope="request"/>
                                                    <c:import url="/tree?style=checkbox"></c:import>
                                                </li>
                                            </ul>
                                        </div>
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
<script src="${home}/assets/plugins/boostrap-tree/bootstrap-tree.min.js"></script>
<script type="text/javascript">
    function doSave() {
        var permissions = $.map($(".tree").find("[role=checkbox].fa-check-square-o"), function (el) {
            return parseInt($(el).closest("li").data("id"));
        });
        $.ServiceClient.invoke("${home}/role/grant.json", {
            data: {
                "role_id": parseInt($("#id").val()),
                "permissions": permissions
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("角色授权成功！", {
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
        $.goBack("${referer}", "${home}/role/list", {portal_id: ${role.portal_id}});
    }

    $(function () {
        $(document).on("click", "#btnSave", function (e) {
            e.preventDefault();
            doSave();
        }).on("click", "#btnCancel", function (e) {
            e.preventDefault();
            doBack();
        }).on("click", "#btnAll", function(e){
            e.preventDefault();
            $(".tree").find("[role=checkbox]").removeClass('fa-square-o').addClass("fa-check-square-o");
        }).on("click", "#btnNone", function(e){
            e.preventDefault();
            $(".tree").find("[role=checkbox]").removeClass('fa-check-square-o').addClass("fa-square-o");
        });
    })
</script>
</body>
</html>