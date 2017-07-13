/**
 * 表格插件类
 *
 * @author Saintcy Don
 */
(function ($) {

    var Table = function (element, options) {
        this.$element = $(element);
        //this.$header = this.$element.find("thead");
        this.$body = this.$element.find("tbody");
        this.$pagination = this.$element.find(".pagination");
        this.$pageSize = this.$element.find(".page-size");
        this.$totalCount = this.$element.find(".total-count");
        this.options = options;
    }

    Table.DEFAULTS = {
        /**
         * 元素格式
         * {
         *   align:'left|center|right',//文本对齐方式
         *   width: Percent|Number,//宽度
         *   formatter:Function//一个函数，返回格式化后的数据，参数为一行原始用户输入的数据
         * }
         */
        inModel: [],//输入模型，用于setData/insertData
        /**
         * 元素格式
         * {
         *   name: String,//返回的字段名，若都没有名字，则每行是一个数组，如果有名字，则每行是一个对象
             extractor: Function,//一个行数，返回解压后的数据，参数为Jquery tr行对象
         * }
         */
        outModel: []//输出模型，用于getData
    }

    function getParent($this) {
        /*var selector = $this.attr('data-target')

        if (!selector) {
            selector = $this.attr('href')
            selector = selector && /#[A-Za-z]/.test(selector) && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
        }

        var $parent = selector && $(selector)

        return $parent && $parent.length ? $parent : $this.parent()*/
        return $this.closest("table");
    }

    /**
     * 分页函数
     * @param $pagination 分页元素
     * @param totalCount 总页数
     * @param pageNo 当前页
     * @param pageSize 每页大小
     * @param turnCallback 翻页回调
     */
    Table.prototype.paginate = function (totalCount, pageNo, pageSize) {
        var totalPage = Math.ceil(totalCount / pageSize) || 1,
            midPageNo = pageNo < 3 ? 3 : (pageNo > totalPage - 2 ? totalPage - 2 : pageNo),//中间页
            paging = {
                prev: pageNo - 1,
                first: 1,
                pageNav1: midPageNo - 2,
                pageNav2: midPageNo - 1,
                pageNav3: midPageNo,
                pageNav4: midPageNo + 1,
                pageNav5: midPageNo + 2,
                last: totalPage,
                next: pageNo + 1
            };

        this.$pagination.empty();
        if (this.$pageSize.is("select")) {
            this.$pageSize.val(pageSize);
        } else {
            this.$pageSize.html(pageSize);
        }
        this.$totalCount.html(totalCount);

        if (pageNo != 1) {//prev&first
            this.$pagination.append('<li data-target="' + this.$element.attr("id") + '" data-page-no="' + paging.first + '" title="最前页"><a href="#">&laquo;</a></li>');
            this.$pagination.append('<li data-target="' + this.$element.attr("id") + '" data-page-no="' + paging.prev + '" title="上一页"><a href="#">&lsaquo;</a></li>');
        }
        if (paging.pageNav1 >= 1 && paging.pageNav1 <= totalPage) {
            this.$pagination.append('<li data-target="' + this.$element.attr("id") + '" class="' + (paging.pageNav1 == pageNo ? "active" : "") + '" data-page-no="' + paging.pageNav1 + '"><a href="#">' + paging.pageNav1 + '</a></li>');
        }
        if (paging.pageNav2 >= 1 && paging.pageNav2 <= totalPage) {
            this.$pagination.append('<li data-target="' + this.$element.attr("id") + '" class="' + (paging.pageNav2 == pageNo ? "active" : "") + '" data-page-no="' + paging.pageNav2 + '"><a href="#">' + paging.pageNav2 + '</a></li>');
        }
        if (paging.pageNav3 >= 1 && paging.pageNav3 <= totalPage) {
            this.$pagination.append('<li data-target="' + this.$element.attr("id") + '" class="' + (paging.pageNav3 == pageNo ? "active" : "") + '" data-page-no="' + paging.pageNav3 + '"><a href="#">' + paging.pageNav3 + '</a></li>');
        }
        if (paging.pageNav4 >= 1 && paging.pageNav4 <= totalPage) {
            this.$pagination.append('<li data-target="' + this.$element.attr("id") + '" class="' + (paging.pageNav4 == pageNo ? "active" : "") + '" data-page-no="' + paging.pageNav4 + '"><a href="#">' + paging.pageNav4 + '</a></li>');
        }
        if (paging.pageNav5 >= 1 && paging.pageNav5 <= totalPage) {
            this.$pagination.append('<li data-target="' + this.$element.attr("id") + '" class="' + (paging.pageNav5 == pageNo ? "active" : "") + '" data-page-no="' + paging.pageNav5 + '"><a href="#">' + paging.pageNav5 + '</a></li>');
        }
        if (pageNo != totalPage) {
            this.$pagination.append('<li data-target="' + this.$element.attr("id") + '" data-page-no="' + paging.next + '" title="下一页"><a href="#">&rsaquo;</a></li>');
            this.$pagination.append('<li data-target="' + this.$element.attr("id") + '" data-page-no="' + paging.last + '" title="最后页"><a href="#">&raquo;</a></li>');
        }
    };

    /**
     * 设置或获取数据
     * @param rows
     * @param index 插入的位置
     * @returns {Object}
     */
    Table.prototype.data = function (rows, index) {
        if (rows) {
            this._setData(rows, index);
        } else {
            return this._getData();
        }
    }

    /**
     * 设置或获取页数据
     * @returns {{totalCount: Number, pageSize: Number, pageNo: Number, rows: Object}}
     */
    Table.prototype.page = function (page) {
        if (page) {
            this._setPage(page);
        } else {
            return {
                totalCount: this.totalCount(),
                pageSize: this.pageSize(),
                pageNo: this.pageNo(),
                rows: this._getData()
            }
        }
    }

    /**
     * 获取当前页码
     * @returns {Number}
     */
    Table.prototype.pageNo = function () {
        return parseInt($("li.active", this.$pagination).data("page-no")) || 1;
    };

    /**
     * 获取当前每页行数
     * @returns {Number}
     */
    Table.prototype.pageSize = function () {
        return parseInt(this.$pageSize.is("select") ? this.$pageSize.val() : this.$pageSize.text()) || this.$body.find("tr").length;
    };

    /**
     * 获取总条数
     * @returns {Number}
     */
    Table.prototype.totalCount = function () {
        return parseInt(this.$totalCount.text()) || this.$body.find("tr").length;
    };

    /**
     * 重新加载当前页
     */
    Table.prototype.reload = function () {
        this.turnPage($("li.active", this.$pagination));
    }

    /**
     * 设置页面数据，包括分页
     * @param page
     */
    Table.prototype._setPage = function (page) {
        this._setData(page.rows);
        this.paginate(page.totalCount, page.pageNo, page.pageSize);
    }

    /**
     *
     * 设置表格数据
     *
     * @param rows 行数据集合，数组，元素为名/值格式或数据
     * @param index 插入的位置。如果不传入，则覆盖整个表格
     * @private
     */
    Table.prototype._setData = function (rows, index) {
        if (index === undefined || index === null || index < 0) {
            this.$body.empty();
            index = -1;
        }
        if (rows && rows.length) {
            for (var i = 0; i < rows.length; i++) {
                this._insertRow(index + i, rows[i]);
            }
        }
    };

    /**
     * 插入行
     * @param i 插入的位置
     * @param row 插入的行内容
     * @private
     */
    Table.prototype._insertRow = function (i, row) {
        var $tr = $("<tr>").data("data", row), $trs = this.$body.children("tr");
        for (var j = 0; j < this.options.inModel.length; j++) {
            var $td = $("<td>"), model = this.options.inModel[j], formatter = model.formatter, align = model.align || "left", width = model.width || '';
            if ($.isFunction(formatter)) {
                $td.append(formatter(row));
            } else {
                $td.append("&nbsp;");
            }
            $td.css({"text-align": align, "width": width});
            $tr.append($td);
        }
        if (i < 0) {
            this.$body.prepend($tr);
        } else if (i > $trs.length) {
            this.$body.append($tr);
        } else {
            $trs.eq(i).after($tr);
        }
    }

    /**
     * 获取表格数据
     * @returns {Object} 表格数据
     * @private
     */
    Table.prototype._getData = function () {
        var trs = this.$body.find("tr"), rows = [];

        for (var i = 0; i < trs.length; i++) {
            var row = {};
            for (var j = 0; j < this.options.outModel.length; j++) {
                var model = this.options.outModel[j], extractor = model.extractor, name = model.name, value = null;
                if ($.isFunction(extractor)) {
                    value = extractor(trs.eq(i).find("td"));
                }
                if (name) {
                    row[name] = value;
                } else {
                    if ($.isArray(row)) {
                        row.push(value);
                    } else {
                        row = [value];
                    }
                }
            }
            rows.push(row);
        }
        return rows;
    };

    /**
     * 翻页事件
     * @param _relatedTarget 翻页按钮
     * @private
     */
    Table.prototype.turnPage = function (_relatedTarget) {
        var $parent = getParent($(_relatedTarget)), pageNo = parseInt($(_relatedTarget).data("page-no")), relatedTarget = {relatedTarget: _relatedTarget};
        $parent.trigger($.Event('turn.bs.table', relatedTarget), [pageNo, this.pageSize()])
    }

    /**
     * 改变每页条数事件
     * @param _relatedTarget 每页条数下拉框
     * @private
     */
    Table.prototype.changeSize = function (_relatedTarget) {
        var $parent = getParent($(_relatedTarget)), pageSize = $(_relatedTarget).val(), relatedTarget = {relatedTarget: _relatedTarget};
        $parent.trigger($.Event('size.bs.table', relatedTarget), [this.pageNo(), pageSize])
    }

    // TABLE PLUGIN DEFINITION
    // ==========================

    function Plugin(option) {
        var args = Array.prototype.slice.call(arguments, 1),
            returnValue = this;

        this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.table')
            var options = $.extend({}, Table.DEFAULTS, $this.data(), typeof option == 'object' && option)

            if (!data) $this.data('bs.table', (data = new Table(this, options)))
            if (typeof option == 'string') {//methodCall
                if (!data) {
                    return $.error("cannot call methods on " + table + " prior to initialization; " +
                        "attempted to call method '" + option + "'");
                }
                if (!$.isFunction(data[option]) || option.charAt(0) === "_") {
                    return $.error("no such method '" + option + "' for " + table + " widget instance");
                }
                var methodValue = data[option].apply(data, args);
                if (methodValue !== data && methodValue !== undefined) {
                    returnValue = methodValue && methodValue.jquery ?
                        returnValue.pushStack(methodValue.get()) :
                        methodValue;
                    return false;
                }
            }
        })

        return returnValue;
    }


    var old = $.fn.table

    $.fn.table = Plugin
    $.fn.table.Constructor = Table


    // TABLE NO CONFLICT
    // ====================

    $.fn.table.noConflict = function () {
        $.fn.table = old
        return this
    }

    // TABLE DATA-API
    // =================
    var handle = function (e) {
        var $this = $(this), href = $this.attr('href'), $target = $this.closest("table")//$($this.attr('data-target') || (href && href.replace(/.*(?=#[^\s]+$)/, ''))) // strip for ie7
        e.preventDefault();
        Plugin.call($target, e.data, this);
    }

    $(document).on('click.bs.table.data-api', '.pagination li', 'turnPage', handle)
        .on('change.bs.table.data-api', '.page-size', 'changeSize', handle);

})(jQuery);
