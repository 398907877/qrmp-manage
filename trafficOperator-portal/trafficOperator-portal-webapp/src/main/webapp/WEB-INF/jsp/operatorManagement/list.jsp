<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                            <li class="active">
                                <a href="${home}/operatorManagement/list"><span>运营商列表</span></a>
                            </li>
                            <li id="btnCreate">
                                <a href="javascript:"><span>新增运营商</span></a>
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
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-2 hidden">关键字</label>
                                <div class="col-sm-8 col-sm-offset-2 col-md-5 col-md-offset-2">
                                    <input id="keyword" type="text" class="form-control" placeholder="请输入名称查找"
                                           value="<c:out value="${query.keyword}"/>">
                                </div>
                                <div class="col-sm-2 col-md-5 hidden-xs">
                                    <button id="btnQuery" class="btn btn-primary">查询</button>
                                </div>
                            </div>
                        </form>
                        <div class="table-responsive">
                            <table id="table" class="table table-striped table-hover no-margin">
                                <thead>
                                <tr>
                                    <th  >编号</th>
                                    <th >交通运营商名称</th>
                                    <th >所在地市（省市）</th>
                                    <th >运营商简介</th>
                                     <th >服务状态（正常、终止）</th>
                                      <th >最后修改用户</th>
                                         <th >修改时间</th>
                                            <th >运营商联系人信息</th>
                              
                                    
                                  
                                </tr>
                                </thead>
                                <c:if test="${page.totalCount>0}">
                                
                                
                                    <tbody>
                                    <c:forEach items="${page.rows}" var="operatorInformation">
                                        <tr>
                                            <td class="text-left"><c:out value="${operatorInformation.id}"/></td>
                                            <td class="text-left name"><c:out value="${operatorInformation.operatorName}"/></td>
                                                  <td class="text-left name"><c:out value="${operatorInformation.location}"/></td>
                                                        <td class="text-left name"><c:out value="${operatorInformation.operatorDesc}"/></td>
                                                        
                                                        
                                                        <td class="text-left">
                                                <c:choose>
                                                    <c:when test="${operatorInformation.serviceState==0}"><label class="label label-default">未选择</label></c:when>
                                                    <c:when test="${operatorInformation.serviceState==1}"><label class="label label-success">正常</label></c:when>
                                                    <c:when test="${operatorInformation.serviceState==2}"><label class="label label-warning">停用</label></c:when>
                                 
                                                    <c:otherwise>其他</c:otherwise>
                                                </c:choose>
                                            </td>
                                                        
                                                        
                                          
                                                              
                                                                               
                                                                          <td class="text-left name"><c:out value="${operatorInformation.lastModifyUser}"/></td>
                                                              
                                                         
                                                                    <td class="text-left name">     <fmt:formatDate value="${operatorInformation.lastModifyTime}" pattern="yyyy/MM/dd HH:mm"/></td>
                                                                    
                                                   
                                                                                <td class="text-left name"><a href="#">查看联系人</a></td>
                                            <td class="text-center">
                                                <div class="lk-btn-group" data-id="${operatorInformation.id}">
                                                    <a href="javascript:" class="update">编辑</a><a href="javascript:"
                                                                                                  class="delete">删除</a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                    
                                    
                                    <tfoot>
                                    <tr class="pagination-bar">
                                        <td colspan="10">
                                            <c:set var="pageNo" value="${page.pageNo}" scope="request"></c:set>
                                            <c:set var="pageSize" value="${page.pageSize}" scope="request"></c:set>
                                            <c:set var="totalCount" value="${page.totalCount}" scope="request"></c:set>
                                            <c:set var="totalPage" value="${page.totalPage}" scope="request"></c:set>
                                            <c:import url="/pagination-bar"></c:import>
                                        </td>
                                    </tr>
                                    </tfoot>
                                </c:if>
                            </table>
                        </div>
                        <div class="alert alert-info no-margin text-center no-records ${empty page or page.totalCount<=0?"":"hidden"}">
                            <i class="fa-fw fa fa-info"></i>
                            没有找到相关记录！
                        </div>
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
<script type="text/javascript">
    function doQuery(pageNo, pageSize) {
        location.assign("${home}/operatorManagement/list?" + $.param({
                    "keyword": $("#keyword").val(),
                    "pageNo": pageNo,
                    "pageSize": pageSize
                }));
    }

    function doCreate() {
        location.assign("${home}/operatorManagement/form");
    }

    function doUpdate(id) {
        location.assign("${home}/operatorManagement/update?id=" + id);
    }

    function doDelete(id) {
        $.ServiceClient.invoke("${home}/operatorManagement/delete/" + id + ".json", {
            complete: function (data) {
                if (data.success) {
                    location.reload();
                } else {
                    layer.msg(data.errmsg, {icon: 2});
                }
            }
        });
    }

    function getName(el) {
        return $(el).closest("tr").find(".name").text();
    }

    function getId(el) {
        return $(el).closest(".lk-btn-group").data("id");
    }

    function getPageNo($table) {
        return parseInt($(".pagination li.active", $table).data("page-no")) || 1;
    }

    function getPageSize($table) {
        return parseInt($(".pagination-bar .page-size", $table).val()) || $("tbody", $table).find("tr").length;
    }

    $(function () {

    	
    	
        $(document).on("click", "#btnQuery", function (e) {//查询
            
 
        	   e.preventDefault();

 
            var $table = $(".table");
            doQuery(1, getPageSize($table));
        }).on("click", ".pagination li", function (e) {//翻页
            e.preventDefault();
            var $table = $(this).closest(".table");
            doQuery($(this).data("page-no"), getPageSize($table));
        }).on("change", ".pagination-bar .page-size", function (e) {//每页条数
            e.preventDefault();
            var $table = $(this).closest(".table");
            doQuery(getPageNo($table), getPageSize($table));
        }).on("click", "#btnCreate", function (e) {//创建
            e.preventDefault();
            doCreate();
        }).on("click", ".update", function (e) {//编辑
            doUpdate(getId($(this)));
        }).on("click", ".delete", function (e) {//删除
            e.preventDefault();
            var $this = $(this);
            layer.confirm("确认删除公告[" + getName($this) + "]？", {
                icon: 3,
                title: '提示'
            }, function (index) {
                doDelete(getId($this));
                layer.close(index);
            });
        });
    })
</script>
</body>
</html>