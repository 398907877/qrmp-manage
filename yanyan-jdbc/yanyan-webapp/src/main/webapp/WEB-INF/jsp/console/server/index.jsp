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
<c:import url="/sidebar?menu=server"/>
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
                                <a href="#sysenv" data-toggle="tab"><span>环境变量</span></a>
                            </li>
                            <li class="">
                                <a href="#net" data-toggle="tab"><span>网络配置</span></a>
                            </li>
                            <li class="">
                                <a href="#thread" data-toggle="tab"><span>线程监控</span></a>
                            </li>
                            <li class="">
                                <a href="#cpu" data-toggle="tab"><span>CPU监控</span></a>
                            </li>
                            <li class="">
                                <a href="#mem" data-toggle="tab"><span>内存监控</span></a>
                            </li>
                            <li class="">
                                <a href="#disk" data-toggle="tab"><span>磁盘监控</span></a>
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
                        <div class="tab-content">
                            <div role="tabpanel" class="tab-pane active" id="sysenv">
                                <iframe name="sysenv" width="100%" height="100%" frameborder="no" scrolling="no"></iframe>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="net">
                                <iframe name="net" width="100%" height="100%" frameborder="no" scrolling="no"></iframe>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="thread">
                                <iframe name="thread" width="100%" height="100%" frameborder="no" scrolling="no"></iframe>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="cpu">
                                <iframe name="cpu" width="100%" height="100%" frameborder="no" scrolling="no"></iframe>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="mem">
                                <iframe name="mem" width="100%" height="100%" frameborder="no" scrolling="no"></iframe>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="disk">
                                <iframe name="disk" width="100%" height="100%" frameborder="no" scrolling="no"></iframe>
                            </div>
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
    function fixWindow() {
        $(this).css({
            height: $(this).contents().find('body').get(0).scrollHeight
        })
    }

    $(function () {
        $("iframe").load(fixWindow);

        $('a[data-toggle="tab"]').on("show.bs.tab", function (e) {
            var href = $(e.target).attr("href");
            if (href == "#sysenv") {
                $("#sysenv>iframe").attr("src") || $("#sysenv>iframe").attr("src", '${home}/console/server/sysenv');
            } else if (href == "#net") {
                $("#net>iframe").attr("src") || $("#net>iframe").attr("src", '${home}/console/server/net');
            } else if (href == "#thread") {
                $("#thread>iframe").attr("src") || $("#thread>iframe").attr("src", '${home}/console/server/thread');
            } else if (href == "#cpu") {
                $("#cpu>iframe").attr("src") || $("#cpu>iframe").attr("src", '${home}/console/server/cpu');
            } else if (href == "#mem") {
                $("#mem>iframe").attr("src") || $("#mem>iframe").attr("src", '${home}/console/server/mem');
            } else if (href == "#disk") {
                $("#disk>iframe").attr("src") || $("#disk>iframe").attr("src", '${home}/console/server/disk');
            }
        })
        $('a[data-toggle="tab"][href="#sysenv"]').click()
    })
</script>
</body>
</html>