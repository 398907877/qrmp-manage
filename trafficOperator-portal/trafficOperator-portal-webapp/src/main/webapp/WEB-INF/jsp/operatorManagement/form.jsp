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
                                        <input type="text" id="operatorName" name="operatorName" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.operatorName}"/>">
                                        <p class="note"><strong>Note:</strong> 交通运营商名称</p>
                                    </div>
                                </div>
                                
                                
                                
                 
                                
                                
                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">所在地市（省市）</label>
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
                                           <p class="note"><strong>Note:</strong> 请选择您的位置</p>
                                    </div>
                                </div>
                                
                                
                
                                
                                
                                
                                
              
                      
                                
                                
                                
                                
                                                                                    
                                                <div class="form-group">
                                    <label class="control-label col-sm-2 ">运营商简介</label>
                                    <div class="col-sm-10">
                            <textarea class="form-control trim" id="operatorDesc" name="operatorDesc"
                                                  placeholder="简介"><c:out value="${operatorInformation.operatorDesc}"/></textarea>
                                        <p class="note"><strong>Note:</strong>运营商简介</p>
                                    </div>
                                </div>
                                
                                
    
                                
                                
                                
                                
                                                            <div class="form-group">
                                    <label class="control-label col-sm-2 required">服务状态（正常、终止）</label>
                                    <div class="col-sm-10">
                                          <select id="serviceState" name="serviceState" class="form-control select2"
                                                data-placeholder="请选择性别">
                                            <option value="0" ${chauffeur.gender==0?"selected:selected":""}>请选择</option>
                                            <option value="1" ${chauffeur.gender==1?"selected:selected":""}>正常</option>
                                            <option value="2" ${chauffeur.gender==2?"selected:selected":""}>终止</option>
                                        </select>
                                        <p class="note"><strong>Note:</strong> 服务状态（正常、）</p>
                                    </div>
                                </div>
                                
                                
                                
                                           
                                
                                                                                  
                                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">最后修改用户</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="lastModifyUser" name="lastModifyUser" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.lastModifyUser}"/>">
                                        <p class="note"><strong>Note:</strong> 最后修改用户</p>
                                    </div>
                                </div>
                                
                                
                                
                                
                                                                                <div class="form-group">
                                    <label class="control-label col-sm-2 required">修改时间</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="lastModifyTime" name="lastModifyTime" class="form-control"
                                               maxlength="20"
                                               placeholder="请输入人员用户名" value="<c:out value="${operatorInformation.lastModifyTime}"/>">
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



<script type="text/javascript" src="${home}/assets/plugins/select2/js/select2.full.min.js"></script>
<script type="text/javascript" src="${home}/assets/plugins/select2/js/i18n/zh-CN.js"></script>


<script type="text/javascript">


function doBack() {
    $.goBack("${referer}", "${home}/operatorManagement/list");
}


function doSave() {
    $.ServiceClient.invoke("${home}/operatorManagement/${!empty operatorManagement.id?"update.json":"form.json"}", {
        data: {
            "id": parseInt($("#id").val()),
            "operatorName": $("#operatorName").val(),
            "location": $.trim($("#location").val()),
            "operatorDesc": $("#operatorDesc").val(),
            "serviceState": $("#serviceState").val(),
            "lastModifyUser": $("#lastModifyUser").val(),
            "lastModifyTime": $("#lastModifyTime").val(),
            
            
             
            "province_id": $("#province_id").val(),
            "city_id": $("#city_id").val()
            

        },
        complete: function (data) {
            if (data.success) {
                layer.alert("运营商保存成功！", {
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