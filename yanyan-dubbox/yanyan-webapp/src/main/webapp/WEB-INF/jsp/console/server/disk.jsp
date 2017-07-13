<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <style type="text/css">
        .chart {
            height: 300px
        }
    </style>

</head>
<body class="">

<!-- #MAIN PANEL -->
<div class="main">

    <!-- #MAIN CONTENT -->
    <div class="content">

    </div>
    <!-- END #MAIN CONTENT -->

</div>
<!-- END #MAIN PANEL -->

<!--================================================== -->

<c:import url="/libs-js"/>
<%//此处插入页面自定义的脚本%>
<script src="${home}/assets/plugins/echart/echarts.min.js"></script>
<script type="text/javascript">
    var myCharts = [], option = {
        title: {
            text: '磁盘使用情况',
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c}K ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['已使用', '未使用']
        },
        series: [
            {
                name: '磁盘',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: [
                    {value: 0, name: '已使用'},
                    {value: 100, name: '未使用'}
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    function doQuery() {
        $.ServiceClient.invoke("${home}/console/server/disk.json", {
            quiet: true,
            data: {
                //name: $("#name").val()
            },
            complete: function (data) {
                if (data.success) {
                    var disks = data.disks;

                    for (var i = 0; i < disks.length; i++) {
                        if ($("#chart" + i).length == 0) {
                            $('<div class="row"><div id="chart' + i + '" class="col-sm-6 chart"></div><div id="chart' + (i + 1) + '" class="col-sm-6 chart"></div></div>').appendTo(".content")
                        }
                        option.title.text = disks[i].name
                        option.title.subtext = disks[i].type;
                        option.series[0].name = disks[i].name
                        option.series[0].data = [
                            {value: disks[i].usage.used, name: '已使用'},
                            {value: disks[i].usage.free, name: '未使用'},
                        ]
                        if (myCharts[i] == null) {
                            myCharts[i] = echarts.init(document.getElementById('chart' + i))
                        }
                        //$("#content").append('<div id="chart" style="width: 100%;height:480px;"></div>');

                        myCharts[i].setOption(option);
                    }
                    for (; i < myCharts.length; i++) {
                        myCharts.splice(i, 1);
                        var $row = $("#chart" + i).parent();
                        $("#chart" + i).empty();
                        var empty = true;
                        $row.children(".chart").each(function () {
                            if ($.trim($(this).html()) != "") {
                                empty = false;
                            }
                        })
                        if (empty) {
                            $row.remove();
                        }
                    }

                    parent.$("iframe").trigger("load");
                } else {
                    //layer.alert(data.errmsg, {icon: 2});
                }
            }
        });
    }

    $(function () {
        doQuery();
        setInterval(doQuery, 30000);
    })
</script>
</body>
</html>