$.root_ = $('body');

var fastClick = true,
    thisDevice = null,
    ismobile = /iphone|ipad|ipod|android|blackberry|mini|windows\sce|palm/i.test(navigator.userAgent.toLowerCase()),
    _maskTo = $("body"),
    initApp = function () {
        if (ismobile) {
            $.root_.addClass("mobile-detected");
            thisDevice = "mobile";
            if (ismobile && fastClick) {
                $.root_.addClass("needsclick");
                FastClick.attach(document.body);
            }
        } else {
            $.root_.addClass("desktop-detected fixed-page-footer");
            thisDevice = "desktop";
        }
        if (window.parent && window.parent != window) {
            $.root_.addClass("iframe-detected");
        }
        if ($(window).width() < 979) {
            $.root_.addClass("mobile-view-activated");
            $.root_.removeClass("minified")
        } else {
            $.root_.hasClass("mobile-view-activated") && $.root_.removeClass("mobile-view-activated")
        }

        try {
            if ((typeof top.$) === "function") {//在最顶部显示加载动画
                _maskTo = top.$("body");
            }
        } catch (e) {
        }

        //Panel fullscreen/normal, collapse/expand
        $(document).on("click", ".panel .zoom-btn", function () {
            var $panel = $(this).closest(".panel").toggleClass("panel-fullscreen");

            function resize() {
                $panel.find(".panel-body").css({
                    height: $panel.height() - $panel.find(".panel-heading").outerHeight() - $panel.find(".panel-footer").outerHeight(),
                    width: $panel.width()
                })
            }

            if ($panel.hasClass("panel-fullscreen")) {
                $(this).attr("title", "恢复").find(".fa").removeClass("fa-expand").addClass("fa-compress");
                $('body').addClass("no-overflow");
                $panel.find(".panel-heading").addClass("navbar-fixed-top");
                $panel.find(".panel-footer").addClass("navbar-fixed-bottom");
                $(window).on("resize", resize).resize();
            } else {
                $(this).attr("title", "全屏").find(".fa").removeClass("fa-compress").addClass("fa-expand");
                $('body').removeClass("no-overflow");
                $panel.find(".panel-heading").removeClass("navbar-fixed-top");
                $panel.find(".panel-footer").removeClass("navbar-fixed-bottom");
                $(window).off("resize", resize);
                $panel.find(".panel-body").css({
                    width: "auto",
                    height: "auto"
                });
            }
        }).on("click", ".panel .toggle-btn", function () {
            var $panel = $(this).closest(".panel"), $content = $panel.find(".panel-body,.panel-footer");
            if ($panel.hasClass("panel-collapse")) {
                $panel.removeClass("panel-collapse");
                $content.slideDown("slow");
                $(this).attr("title", "折叠").find(".fa").removeClass("fa-plus").addClass("fa-minus");
            } else {
                $content.slideUp("slow", function () {
                    $panel.addClass("panel-collapse");
                });
                $(this).attr("title", "展开").find(".fa").removeClass("fa-minus").addClass("fa-plus");
            }
        }).ajaxComplete(function (event, xhr, settings) {//ajax请求弹出登录界面
            try {
                var topLayer = top.layer || layer;
                if (xhr.status === 401) {   //验证是否为登陆状态
                    //topLayer.closeAll('dialog');//关闭所有信息框
                    if (topLayer._login_opend === undefined) {
                        topLayer._login_opend = true;//layer打开是异步的，如果
                        topLayer._login_opend = topLayer.open({//打开登录页面
                            type: 2,
                            title: false,
                            shadeClose: true,
                            shade: 0.8,
                            area: ['380px', '480px'],
                            content: [$.contextPath + "/login?style=mini", 'no'],
                            success: function (layero) {
                                topLayer.setTop(layero);
                            },
                            end: function () {
                                topLayer._login_opend = undefined;
                            }
                        });
                    }
                }
            } catch (e) {
                console.log(e)
            }
        }).ajaxSend(function (event, xhr, settings) {
            var topLayer = top.layer || layer;
            if (topLayer._login_opend !== undefined && parent.layer.getFrameIndex(window.name) != topLayer._login_opend) {//如果未登陆，就不再请求
                xhr.abort();
            }else {
                if (!settings.quiet && !_maskTo.hasClass("ajax-loading")) {//显示加载动画
                    _maskTo.addClass("ajax-loading");
                }
            }
        }).ajaxStart(function () {

        }).ajaxStop(function () {
            _maskTo.removeClass("ajax-loading");//关闭加载动画
        });
    };

$(function () {
    initApp();
});
