/**
 * 表单插件类
 *
 * @author Saintcy Don
 */
(function ($) {

    $.fn.extend({

        /**
         *
         * 得到JSON对象
         *
         * @param prefix 字段前缀。比如qz_name，前缀为qz_, 则得到的json值为 name。用于一个页面内有多个名字相同的字段时，用前缀加以区分
         * @param tags 需要获取数据的标签列表，多个用逗号隔开。默认只选择TEXTAREA,INPUT,SELECT三种类型的节点
         * @returns {Object}
         */
        getJSON: function (prefix, tags) {
            tags = tags || "TEXTAREA,INPUT,SELECT";
            prefix = prefix || "";
            var data = {}, arrTag = [ "TEXTAREA", "INPUT", "SELECT" ];

            function setObject(name, value) {
                if (data[name]) {// 若已经存在，则变成数组
                    data[name] = $.makeArray(data[name]).concat($.makeArray(value));
                } else {
                    data[name] = value;
                }
            }

            $(this).find(tags).each(function () {
                var name = this.id;
                if (name) {
                    name = name.substring(prefix.length);
                    if ($.inArray(this.tagName, arrTag) >= 0) {
                        if (this.type == "radio" || this.type == "checkbox") {
                            if (this.name) { /* 多选单选时需要用name的名字 */
                                name = this.name.substring(prefix.length);
                            }
                            if (this.checked) {
                                if (this.type == "checkbox" && this.name) {
                                    setObject(name, [$(this).val()]); // 针对checkbox，若组选（有name值），则选中一个也得是数组
                                } else {
                                    setObject(name, $(this).val());
                                }
                            } else {
                                setObject(name, null);
                            }
                        } else if (this.type == "image") {
                            setObject(name, this.src);
                        } else {
                            setObject(name, $(this).val());
                        }
                    } else if (this.tagName == 'IMG') {
                        setObject(name, this.src);
                    } else {
                        setObject(name, this.innerHTML);
                    }
                }
            });
            return data;
        },

        /**
         *
         * 给表单赋值，单选多选框需要用name
         *
         * @param json {Object} js对象
         * @param prefix {String} 字段前缀
         * @param untrigger {Boolean} 不触发变更事件
         * @returns {*}
         */
        setJSON: function (json, prefix, untrigger) {
            if (!json)
                return $(this);
            prefix = prefix || "";
            var $this = this;
            $.each(json, function (name, value) {
                if (typeof (value) == "object" && !$.isArray(value)) {
                    $this.setJSON(value, untrigger);
                } else {
                    value = value !== 0 ? (value || "") : value;
                    name = (prefix + name).replace(/\./g, "\\.").replace(/\[/g, "\\[").replace(/]/g, "\\]").replace(/:/g, "\\:");
                    var el = $this.find("#" + name);
                    if (el.size() == 0) {
                        el = $this.find("[name=" + name + "]:checkbox");
                    }
                    if (el.size() == 0) {
                        el = $this.find("[name=" + name + "][value='" + value + "']:radio");
                    }
                    if (el.size() > 0) {
                        if (el.size() == 1) {
                            var dom = el.get(0), tagName = dom.tagName;
                            if (tagName == "INPUT" || tagName == "SELECT" || tagName == "TEXTAREA") {
                                if (dom.type == 'checkbox' || dom.type == 'radio') {
                                    if (untrigger) {
                                        dom.checked = true;
                                    } else {
                                        dom.checked = false;
                                        el.click();
                                    }
                                } else if (dom.type == "image") {
                                    el.attr("src", value);
                                } else {
                                    el.val(value);
                                }
                            } else if (tagName == "IMG") {
                                el.attr("src", value);
                            } else {
                                el.html(value);
                            }
                        } else {// 多选
                            value = $.makeArray(value);
                            el.each(function () {
                                if ($.inArray(this.value, value) >= 0) {
                                    if (untrigger) {
                                        this.checked = true;
                                    } else {
                                        this.checked = false;
                                        this.click();
                                    }
                                } else {
                                    if (untrigger) {
                                        this.checked = false;
                                    } else {
                                        this.checked = true;
                                        this.click();
                                    }
                                }
                            });
                        }
                        if (!untrigger) {
                            el.change();
                        }
                    }
                }
            });
            return $(this);
        },

        /**
         * 清空表单
         * checkbox全部不选
         * radio选中第一个
         * select单选选中第一个，多选都不选
         * text,password,hidden,textarea,image值清空
         * @param tags 需要清空的标签列表
         * @param untrigger 不触发变更事件
         */
        clear: function (tags, untrigger) {
            tags = tags || "TEXTAREA,INPUT,SELECT";

            $(this).find(tags).each(function () {
                var $el = $(this);
                if (this.tagName == 'INPUT') {
                    if (this.type == "checkbox") {
                        if (untrigger) {
                            this.checked = false;
                        } else {
                            this.checked = true;
                            $el.click();
                        }
                    } else if (this.type == "radio") {
                        if ($("input[name=" + this.name.replace(/\./g, "\\.").replace(/\[/g, "\\[").replace(/]/g, "\\]").replace(/:/g, "\\:") + "]").get(0) == this) {
                            if (untrigger) {
                                this.checked = true;
                            } else {
                                $el.click();
                            }
                        }
                    } else if (this.type == "image") {
                        this.src = "";
                    } else {
                        if (this.type == "file") {
                            var $newel = $el.clone(true);
                            $newel.val('');//IE9以上允许置空，所以需要清空
                            $el.replaceWith($newel);
                            $el = $newel;
                        } else {
                            $el.val("");
                        }
                    }
                } else if (this.tagName == 'TEXTAREA') {
                    $el.val("");
                } else if (this.tagName == 'SELECT') {
                    if (this.multiple) {
                        this.value = null;
                    } else {
                        this.value = "";
                        //this.selectedIndex = 0; // 选中第一个
                    }
                } else if (this.tagName == 'IMG') {
                    this.src = "";
                } else {
                    $el.empty();
                }

                if (!untrigger)
                    $el.change();
            });
        },

        /* 限制只能输入浮点数 */
        floatOnly: function (vSelector, sign) {
            $(this).find(vSelector).each(function () {
                var $this = $(this);
                $this.css("ime-mode", "disabled");// 禁止输入法

                $this.bind("paste", function () {
                    return clipboardData.getData('text').match(/^[\d]*$/) != null;
                });

                $this.bind("dragenter", function () {
                    return false;
                });

                if (sign) {/* 有符号的 */
                    $this.bind("keypress", function (e) {
                        var k = e.which;
                        return e.charCode == 0 || (k >= 48 && k <= 57) || ( k == 46 && this.value != "" && this.value.indexOf(".") == -1) || k == 45;
                    }).keyup(function (e) {
                        var k = e.which;
                        if (k == 189) {
                            this.value = (this.value.indexOf("-") == 0 ? "-" : "") + this.value.replace(/-/g, "");
                        }
                    });
                } else {
                    $this.bind("keypress", function (e) {
                        var k = e.which;
                        //e.charCode==0在IE外的浏览器说明没有字符输入，只是按下功能键盘
                        return e.charCode == 0 || (k >= 48 && k <= 57) || ( k == 46 && this.value != "" && this.value.indexOf(".") == -1);
                    });
                }
            });
        },

        /* 限制只能输入整数 */
        integerOnly: function (vSelector, sign) {
            $(this).find(vSelector).each(function () {
                var $this = $(this);
                $this.css("ime-mode", "disabled"); // 禁止输入法

                $this.bind("paste", function () {
                    return clipboardData.getData('text').match(/^[\d]*$/) == null;
                });

                $this.bind("dragenter", function () {
                    return false;
                });

                if (sign) {/* 有符号的 */
                    $this.bind("keypress", function () {
                        var k = e.which;
                        return e.charCode == 0 || (k >= 48 && k <= 57) || k == 45;
                    }).bind("keyup", function (e) {
                        var k = e.which;
                        if (k == 189) {
                            this.value = (this.value.indexOf("-") == 0 ? "-" : "") + this.value.replace(/-/g, "");
                        }
                    });
                } else {
                    $this.bind("keypress", function (e) {
                        var k = e.which;
                        return e.charCode == 0 || (k >= 48 && k <= 57);
                    });
                }
            });
        },

        /* 限制只能输入字母 */
        letterOnly: function (vSelector) {
            $(this).find(vSelector).each(function () {
                var $this = $(this);
                $this.css("ime-mode", "disabled"); // 禁止输入法
                $this.bind("paste", function () {
                    return clipboardData.getData('text').match(/^[A-Za-z]*$/) == null;
                });

                $this.bind("dragenter", function () {
                    return false;
                });

                $this.bind("keypress", function (e) {
                    var k = e.which;
                    return e.charCode == 0 || (k >= 65 && k <= 90) || (k >= 97 && k <= 122);
                });
            });
        },

        /* 限制只能输入字母数字 */
        wordOnly: function (vSelector) {
            $(this).find(vSelector).each(function () {
                var $this = $(this);
                $this.css("ime-mode", "disabled"); // 禁止输入法
                $this.bind("paste", function () {
                    return clipboardData.getData('text').match(/^[A-Za-z0-9]*$/) == null;
                });

                $this.bind("dragenter", function () {
                    return false;
                });

                $this.bind("keypress", function (e) {
                    var k = e.which;
                    return e.charCode == 0 || (k >= 65 && k <= 90) || (k >= 97 && k <= 122) || (k >= 48 && k <= 57);
                });
            });
        },

        /**
         * 在属性上设置maxlength="60"
         */
        maxlen: function (vSelector) {
            $(this).find(vSelector).each(function () {
                var $this = $(this),
                    checkLenFun = function () {
                        var max = parseInt($this.attr("maxlength")) || 0;
                        if (!isNaN(max) && max > -1) {
                            var cnt = max - $this.val().length;
                            if (cnt < 0) {
                                $this.html($this.val().substr(0, max));
                            }
                        }
                        return true;
                    };

                $this.bind("keypress", function () {
                    var max = parseInt($this.attr("maxlength")) || 0;
                    return e.charCode == 0 || max > $this.val().length;
                }).bind("keyup", checkLenFun);

            });
        }
    });// jQuery.fn.extend

    $.extend({
        /**
         * $.form 找出所有注册的子元素
         */
        form: function (vSelector) {
            return $(vSelector);
        }
    });// jQuery.extend

})(jQuery);
