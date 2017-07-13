var drawMenu = function () {
        $(".sidebar nav ul").accordion({
            accordion: true,
            speed: 300,
            "closedSign": '<em class="fa fa-plus-square-o"></em>',
            "openedSign": '<em class="fa fa-minus-square-o"></em>'
        });
    },
    drawBreadCrumb = function (branches) {
        var bread_crumb = $(".topbar > .breadcrumb"), trunks = $("nav li.active > a"), depth = trunks.length;
        bread_crumb.empty();
        bread_crumb.append($("<li>首页</li>"));
        //打印右侧主菜单中的路径
        trunks.each(function () {
            bread_crumb.append($("<li></li>").html($.trim($(this).clone().children(".badge").remove().end().text())));//去掉后面的未读标记
            if (depth > 0) {
                document.title += "-" + bread_crumb.find("li:last-child").text();
            }
        });
        //打印工作区分支菜单的路径
        if (branches) {
            $.each(branches, function (i, n) {
                if(n) {
                    bread_crumb.append($("<li></li>").html(n));
                    document.title = bread_crumb.find("li:last-child").text();
                }
            })
        }
    },
    mountActions = function () {
        var actions = {
            "userLogout": function ($btnLogout) {
                function logout() {
                    window.location = $btnLogout.attr("href")
                }

                $.root_.addClass("animated fadeOutUp"), setTimeout(logout, 1000);
            }, "launchFullscreen": function (element) {
                if ($.root_.hasClass("full-screen")) {
                    $.root_.removeClass("full-screen");
                    document.exitFullscreen ? document.exitFullscreen() : document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.webkitExitFullscreen && document.webkitExitFullscreen()
                } else {
                    $.root_.addClass("full-screen");
                    element.requestFullscreen ? element.requestFullscreen() : element.mozRequestFullScreen ? element.mozRequestFullScreen() : element.webkitRequestFullscreen ? element.webkitRequestFullscreen() : element.msRequestFullscreen && element.msRequestFullscreen()
                }
            }, "minifyMenu": function ($btnMinify) {
                if (!$.root_.hasClass("menu-on-top")) {
                    $.root_.toggleClass("minified");
                    $.root_.removeClass("hidden-menu");
                    $("html").removeClass("hidden-menu-mobile-lock");
                    $btnMinify.effect("highlight", {}, 500);
                }
            }, "toggleMenu": function () {
                if ($.root_.hasClass("menu-on-top")) {
                    $.root_.hasClass("menu-on-top")
                    if ($(window).width() < 979) {
                        $("html").toggleClass("hidden-menu-mobile-lock");
                        $.root_.toggleClass("hidden-menu");
                        $.root_.removeClass("minified");
                    }
                } else {
                    $("html").toggleClass("hidden-menu-mobile-lock");
                    $.root_.toggleClass("hidden-menu");
                    $.root_.removeClass("minified");
                }
            }, "toggleActivity": function ($header) {
                $header.find(".activity .badge").each(function () {
                    var $badge = $(this);
                    if ($badge.hasClass("bg-color-red")) {
                        $badge.removeClass(function (index, css) {
                            return (css.match(/(^|\s)bg-color-\S+/g) || []).join(' ');
                        });
                        $badge.text("0");
                    }
                })

                var $dropdown = $header.find(".activity .ajax-dropdown");

                if ($dropdown.is(":visible")) {
                    $dropdown.fadeOut(150);
                    $header.find(".activity").removeClass("active");
                } else {
                    $dropdown.fadeIn(150);
                    $header.find(".activity").addClass("active");
                }
            }
        };

        /* $('input[name="activity"]').change(function () {
         var a = $(this);
         url = a.attr("id"), container = $(".ajax-notifications"), loadURL(url, container), a = null
         })*/

        $.root_.on("click", '[data-action="userLogout"]', function (e) {
            var $btnLogout = $(this);
            actions.userLogout($btnLogout);
            e.preventDefault();
        }).on("click", '[data-action="launchFullscreen"]', function (e) {
            actions.launchFullscreen(document.documentElement);
            e.preventDefault();
        }).on("click", '[data-action="minifyMenu"]', function (e) {
            var $btn = $(this);
            actions.minifyMenu($btn);
            e.preventDefault();
            $btn = null;
        }).on("click", '[data-action="toggleMenu"]', function (e) {
            actions.toggleMenu();
            e.preventDefault();
        }).on("click", '[data-action="searchMobile"]', function (e) {
            e.preventDefault();
            $.root_.addClass("search-mobile");
        }).on("click", '[data-action="cancelSearchMobile"]', function (e) {
            e.preventDefault();
            $.root_.removeClass("search-mobile");
        }).on("click", '[data-action="toggleActivity"]', function (e) {
            e.preventDefault();
            actions.toggleActivity($(".page-header"));
        });

        $(document).mouseup(function (e) {
            if (!$(".ajax-dropdown").is(e.target) || 0 !== $(".ajax-dropdown").has(e.target).length) {
                $(".ajax-dropdown").fadeOut(150);
                $(".page-header .activity").removeClass("active");
            }
        });

        var $activityBadge = $(".activity > .badge");
        if (parseInt($activityBadge.text()) > 0) {
            $activityBadge.addClass("bg-color-red bounceIn animated")
        }
    };

$(function () {
    if ($(".sidebar").size() > 0) {
        drawMenu();
    }
    if ($(".topbar").size() > 0) {
        drawBreadCrumb();
    }
    if ($(".page-header").size()) {
        mountActions();
    }
});
