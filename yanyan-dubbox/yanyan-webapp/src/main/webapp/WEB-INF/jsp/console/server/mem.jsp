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

</head>
<body class="">

<!-- #MAIN PANEL -->
<div class="main">

    <!-- #MAIN CONTENT -->
    <div class="content">
        <div id="chart" style="width: 100%;height:480px;"></div>
    </div>
    <!-- END #MAIN CONTENT -->

</div>
<!-- END #MAIN PANEL -->

<!--================================================== -->

<c:import url="/libs-js"/>
<%//此处插入页面自定义的脚本%>
<script src="${home}/assets/plugins/echart/echarts.min.js"></script>
<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('chart')), maxLength = 100, d = new Date(), ultZeroize = function (v, l) {
        var z = "";
        l = l || 2;
        v = String(v);
        for (var i = 0; i < l - v.length; i++) {
            z += "0";
        }
        return z + v;
    }, dateToStr = function (date) {
        return [date.getFullYear(), ultZeroize(date.getMonth() + 1), ultZeroize(date.getDate())].join('-') + " " + [ultZeroize(date.getHours()), ultZeroize(date.getMinutes()), ultZeroize(date.getSeconds())].join(':');
    }, initData = function () {
        var data = [], date = new Date(d - maxLength * 1000);
        for (var i = 0; i < maxLength; i++) {
            data.push([dateToStr(date), 0]);
            date = new Date(date.getTime() + 1000);
        }
        return data;
    }, option = {
        title: {
            text: '内存使用率'
        },
        legend: {
            data: ['mem'],
            orient: 'vertical',
            x: 'right'
        },
        grid: {
            //top: 110,
            left: 15,
            right: 15,
            //height: 160
        },
        xAxis: {
            type: 'time',
            splitLine: {
                show: true
            }
        },
        yAxis: {
            type: 'value',
            splitLine: {
                show: true
            },
            axisLabel: {
                inside: true,
                formatter: '{value}\n'
            },
            min: 0,
            max: 100,
            z: 10
        },
        series: [
            {
                name: 'mem',
                type: 'line',
                smooth: true,
                showSymbol: false,
                data: initData()
            }
        ]
    };

    function doQuery() {
        $.ServiceClient.invoke("${home}/console/server/mem.json", {
            quiet: true,
            data: {
                //name: $("#name").val()
            },
            complete: function (data) {
                if (data.success) {
                    var date = new Date();
                    var now = dateToStr(date);
                    var mem = data.mem;
                    var data0 = option.series[0].data;
                    data0.shift();
                    data0.push([now.toString(), parseFloat((mem.usedPercent).toFixed(2))]);
                    option.title.subtext  = "Total: " + formatInt(mem.total) + "K, Used: " + formatInt(mem.actualUsed) + "K, Free: " + formatInt(mem.actualFree) + "K"

                    myChart.setOption(option);
                } else {
                    //layer.alert(data.errmsg, {icon: 2});
                }
            }
        });
    }

    $(function () {
        doQuery();
        setInterval(doQuery, 1000);
    })
</script>
</body>
</html>