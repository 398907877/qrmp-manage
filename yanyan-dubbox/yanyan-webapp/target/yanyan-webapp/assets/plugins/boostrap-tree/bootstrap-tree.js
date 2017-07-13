$(document).ready(function () {
    $('.tree > ul').attr('role', 'tree').find('ul').attr('role', 'group');
    $('.tree').find('li:has(ul)').addClass('parent_li').attr('role', 'treeitem').find(' > span').attr('title', 'Collapse this branch').on('click', function (e) {
        var children = $(this).parent('li.parent_li').find(' > ul > li');
        if (children.is(':visible')) {
            children.hide('fast');
            $(this).attr('title', 'Expand this branch').find(' > i').eq(0).addClass('icon-plus-sign').removeClass('icon-minus-sign');
        }
        else {
            children.show('fast');
            $(this).attr('title', 'Collapse this branch').find(' > i').eq(0).addClass('icon-minus-sign').removeClass('icon-plus-sign');
        }
        e.stopPropagation();
    });

    function uncheckAncestors($li) {
        if ($li != null || $li.length > 0) {
            var $parent = $li.parent("ul");
            if ($parent.find(">li>span>[role=checkbox].fa-check-square-o").length <= 0) {//如果都没有被勾选的
                var $pli = $parent.parent("li");
                if ($pli != null && $pli.length > 0) {
                    //取消本身的勾选
                    $pli.find(">span [role=checkbox]").removeClass('fa-check-square-o').addClass("fa-square-o");
                    //更新祖先
                    uncheckAncestors($pli);
                }
            }
        }
    }

    function checkAncestors($li) {
        if ($li != null || $li.length > 0) {
            var $parent = $li.parent("ul"), $pli = $parent.parent("li"), $checkbox = $pli.find("> span [role=checkbox]");
            if ($pli != null && $pli.length > 0 && $checkbox.hasClass("fa-square-o")) {//如果父节点原来未勾选，则勾上
                //取消本身的勾选
                $checkbox.removeClass('fa-square-o').addClass("fa-check-square-o");
                //更新祖先
                checkAncestors($pli);
            }
        }
    }

    $(".tree").find("[role=checkbox]").on('click', function (e) {
        var $li = $(this).closest("li"), linkType = $.extend({
            Y: "ps",//选中时
            N: "ps"//取消选中时
        }, $(this).closest(".tree").data("link-type"));//p：向上联动，s：向下联动
        if ($(this).hasClass("fa-check-square-o")) {
            //更新本身
            $li.find(">span [role=checkbox]").removeClass('fa-check-square-o').addClass("fa-square-o");
            //更新子孙
            if (linkType.N.indexOf("s") >= 0) {
                $li.find(">ul [role=checkbox]").removeClass('fa-check-square-o').addClass("fa-square-o");
            }
            //更新祖先
            if (linkType.N.indexOf("p") >= 0) {
                uncheckAncestors($li);
            }
        } else {
            //更新本身
            $li.find(">span [role=checkbox]").removeClass('fa-square-o').addClass("fa-check-square-o");
            //更新子孙
            if (linkType.Y.indexOf("s") >= 0) {
                $li.find(">ul [role=checkbox]").removeClass('fa-square-o').addClass("fa-check-square-o");
            }
            //更新祖先
            if (linkType.Y.indexOf("p") >= 0) {
                checkAncestors($li);
            }
        }
        e.stopPropagation();
    });
    $(".tree").find("[role=radio]").on('click', function (e) {
        if ($(this).hasClass("fa-circle-o")) {
            $(this).closest(".tree").find("[role=radio].fa-dot-circle-o").removeClass('fa-dot-circle-o').addClass("fa-circle-o");
            $(this).removeClass('fa-circle-o').addClass("fa-dot-circle-o");
        }

        e.stopPropagation();
    })
});