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
    <link rel="stylesheet" type="text/css" media="screen" href="${home}/assets/plugins/select2/css/select2.min.css">
</head>
<body>

<!-- #PAGE HEADER -->
<c:import url="/header"/>
<!-- END PAGE HEADER -->

<!-- #NAVIGATION -->
<c:import url="/sidebar?menu=${portal.code}"/>
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
                                <c:set var="corp_type" value="${fn:replace(portal.name, '门户', '')}"></c:set>
                                <a href="${home}/corporation/${portal.code}/list"><span>${corp_type}列表</span></a>
                            </li>
                            <li id="btnCreate" class="active">
                                <a href="javascript:"><span>${!empty corporation.id?"修改":"新增"}${corp_type}</span></a>
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
                            <input type="hidden" id="id" name="id" value="${corporation.id}">
                            <input type="hidden" id="portal_id" name="portal_id" value="${corporation.portal_id}">

                            <legend class="hidden">
                                ${corp_type}信息
                            </legend>
                            <fieldset>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required"><c:out
                                            value="${corp_type}"/>名称</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="name" name="name" class="form-control" maxlength="100"
                                               placeholder="请输入门户名称" value="<c:out value="${corporation.name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">英文名称</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="english_name" name="english_name" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入英文名称"
                                               value="<c:out value="${corporation.english_name}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">所属地区</label>
                                    <div class="col-sm-10">
                                        <select id="province_id" name="province_id" class="form-control select2"
                                                style="width: 24.5%"
                                                data-placeholder="请选择省">
                                            <option value=""></option>
                                            <c:forEach items="${provinceOptions}" var="option">
                                                <option value="${option.id}"
                                                    ${option.id==corporation.province_id?"selected=selected":""}
                                                        data-pinyin="${option.pinyin}"
                                                        data-pyabbr="${option.pyabbr}"><c:out
                                                        value="${option.name}"/></option>
                                            </c:forEach>
                                        </select>
                                        <select id="city_id" name="city_id" class="form-control select2"
                                                style="width: 24.5%"
                                                data-placeholder="请选择市">
                                            <option value=""></option>
                                            <c:forEach items="${cityOptions}" var="option">
                                                <option value="${option.id}"
                                                    ${option.id==corporation.city_id?"selected=selected":""}
                                                        data-pinyin="${option.pinyin}"
                                                        data-pyabbr="${option.pyabbr}"><c:out
                                                        value="${option.name}"/></option>
                                            </c:forEach>
                                        </select>
                                        <select id="county_id" name="county_id" class="form-control select2"
                                                style="width: 24.5%"
                                                data-placeholder="请选择县">
                                            <option value=""></option>
                                            <c:forEach items="${countyOptions}" var="option">
                                                <option value="${option.id}"
                                                    ${option.id==corporation.county_id?"selected=selected":""}
                                                        data-pinyin="${option.pinyin}"
                                                        data-pyabbr="${option.pyabbr}"><c:out
                                                        value="${option.name}"/></option>
                                            </c:forEach>
                                        </select>
                                        <select id="township_id" name="township_id" class="form-control select2"
                                                style="width: 24.5%"
                                                data-placeholder="请选择街道">
                                            <option value=""></option>
                                            <c:forEach items="${townshipOptions}" var="option">
                                                <option value="${option.id}"
                                                    ${option.id==corporation.township_id?"selected=selected":""}
                                                        data-pinyin="${option.pinyin}"
                                                        data-pyabbr="${option.pyabbr}"><c:out
                                                        value="${option.name}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">联系地址</label>
                                    <div class="col-sm-10">
                                        <div class='input-group'>
                                            <span class="input-group-addon"><span class="province"><c:out
                                                    value="${corporation.province_name}"/></span><span
                                                    class="city"><c:out
                                                    value="${corporation.city_name}"/></span><span class="county"><c:out
                                                    value="${corporation.county_name}"/></span><span
                                                    class="township"><c:out
                                                    value="${corporation.township_name}"/></span></span>
                                            <input type="text" id="address" name="address" class="form-control"
                                                   maxlength="100"
                                                   placeholder="请输入详细地址"
                                                   value="<c:out value="${corporation.address}"/>">
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">联系人</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="contact_man" name="contact_man" class="form-control"
                                               maxlength="100"
                                               placeholder="请输入联系人" value="<c:out value="${corporation.contact_man}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">联系电话</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="contact_nbr" name="contact_nbr" class="form-control"
                                               maxlength="100"
                                               placeholder="请输入联系电话"
                                               value="<c:out value="${corporation.contact_nbr}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">邮编</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="postcode" name="postcode" class="form-control"
                                               maxlength="100"
                                               placeholder="请输入邮编" value="<c:out value="${corporation.postcode}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">电子邮箱</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="email" name="email" class="form-control"
                                               maxlength="100"
                                               placeholder="请输入电子邮箱" value="<c:out value="${corporation.email}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">传真</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="fax" name="fax" class="form-control"
                                               maxlength="100"
                                               placeholder="请输入传真" value="<c:out value="${corporation.fax}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">网址</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="website" name="website" class="form-control"
                                               maxlength="100"
                                               placeholder="请输入网址" value="<c:out value="${corporation.website}"/>">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2"><c:out value="${corp_type}"/>简介</label>
                                    <div class="col-sm-10">
                                        <textarea id="introduction" name="introduction" class="form-control"
                                                  maxlength="500"
                                                  placeholder="请输入备注"><c:out
                                                value="${corporation.introduction}"/></textarea>
                                    </div>
                                </div>
                                <c:if test="${empty corporation.id}">
                                    <hr>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2 required">管理员账号</label>
                                        <div class="col-sm-10">
                                            <input type="text" id="admin_account" name="admin_account"
                                                   class="form-control"
                                                   maxlength="100" placeholder="请输入管理员账号"
                                                   value="<c:out value="${corporation.admin_account}"/>">
                                            <p class="note"><strong>Note:</strong> 初始密码为000000。</p>
                                        </div>
                                    </div>
                                </c:if>
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
<script type="text/javascript" src="${home}/assets/plugins/select2/js/select2.full.min.js"></script>
<script type="text/javascript" src="${home}/assets/plugins/select2/js/i18n/zh-CN.js"></script>
<script type="text/javascript">
    function doSave() {
        $.ServiceClient.invoke("${home}/corporation/${portal.code}/${!empty corporation.id?"update.json":"create.json"}", {
            data: {
                "id": parseInt($("#id").val()),
                "portal_id": parseInt($("#portal_id").val()),
                "name": $.trim($("#name").val()),
                "english_name": $.trim($("#english_name").val()),
                "province_id": parseInt($("#province_id").val()),
                "city_id": parseInt($("#city_id").val()),
                "county_id": parseInt($("#county_id").val()),
                "township_id": parseInt($("#township_id").val()),
                "contact_man": $("#contact_man").val(),
                "contact_nbr": $("#contact_nbr").val(),
                "address": $("#address").val(),
                "postcode": $("#postcode").val(),
                "fax": $("#fax").val(),
                "email": $("#email").val(),
                "website": $("#website").val(),
                "introduction": $("#introduction").val(),
                "admin_account": $.trim($("#admin_account").val())
            },
            complete: function (data) {
                if (data.success) {
                    layer.alert("${corp_type}保存成功！", {
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

    function doBack() {
        $.goBack("${referer}", "${home}/corporation/${portal.code}/list");
    }

    function paintRegionSelect(el, parent_id, val) {
        //var data = [];
        $(el).empty();
        if (parent_id) {
            $.ServiceClient.invoke("${home}/region/find.json", {
                data: {
                    parent_id: parent_id
                },
                complete: function (data) {
                    $(el).append("<option value=''></option>");
                    if (data.success) {
                        $.each(data.page.rows, function (i, region) {
                            $(el).append('<option value="' + region.id + '" data-pinyin="' + region.pinyin + '" data-pyabbr="' + region.pyabbr + '">' + region.name + '</option>');
                        })
                    }
                    $(el).val(val).trigger("change");
                }
            });
        } else {
            $(el).append("<option value=''></option>");//为显示placeholder
            $(el).val("").trigger("change");
        }
    }

    //初始化验证数据
    function initValidation() {
        return $('#oForm').validate({
            rules: {
                name: {required: true},
                province_id: {required: true},
                city_id: {
                    required: {
                        depends: function () {
                            return $("#province_id").val() != ""
                        }
                    }
                },
                county_id: {
                    required: {
                        depends: function () {
                            return $("#city_id").val() != ""
                        }
                    }
                },
                township_id: {
                    required: {
                        depends: function () {
                            return $("#county_id").val() != ""
                        }
                    }
                },
                address: {required: true},
                contact_man: {required: true},
                contact_nbr: {required: true, mobile: true},
                email: {email: true},
                introduction: {maxlength: 500},
                admin_account: {required: true}
            },
            messages: {
                name: {required: "请输入${corp_type}名称"},
                province_id: {required: "请选择省"},
                city_id: {required: "请选择市"},
                county_id: {required: "请选择县"},
                township_id: {required: "请选择街道"},
                address: {required: "请填写地址"},
                contact_man: {required: "请输入联系人"},
                contact_nbr: {required: "请输入联系电话", mobile: "手机号码格式不正确"},
                email: {email: "请输入合法的电子邮箱"},
                introduction: {maxlength: "企业简介长度不能超过500"},
                admin_account: {required: "请输入管理员账号"}
            }
        });
    }

    $(function () {
        $(".select2").select2({
            matcher: function (params, data) {
                if (!params.term) return data;
                var $element = $(data.element), pinyin = ($element.data("pinyin") || "").replace(/\s/g, '').toLowerCase(),
                        pyabbr = ($element.data("pyabbr") || "").toLowerCase(),
                        text = (data.text || "").toLowerCase(),
                        term = params.term.toLowerCase();
                if (pinyin.indexOf(term) >= 0 || pyabbr.indexOf(term) >= 0 || text.indexOf(term) >= 0) {
                    return data;
                } else {
                    return null;
                }
            }
        }).on("change", function () {
            if (this.id == "province_id") {
                $("#address").prev().find(".province").text($(this).find(":selected").text());
                paintRegionSelect($("#city_id"), $(this).val(), "");
            } else if (this.id == "city_id") {
                $("#address").prev().find(".city").text($(this).find(":selected").text());
                paintRegionSelect($("#county_id"), $(this).val(), "");
            } else if (this.id == "county_id") {
                $("#address").prev().find(".county").text($(this).find(":selected").text());
                paintRegionSelect($("#township_id"), $(this).val(), "");
            } else {
                $("#address").prev().find(".township").text($(this).find(":selected").text());
            }
        });
        var validator = initValidation();
        $(document).on("click", "#btnSave", function (e) {
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