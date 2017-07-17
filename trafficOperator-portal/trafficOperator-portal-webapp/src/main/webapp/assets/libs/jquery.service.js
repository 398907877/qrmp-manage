/**
 * Ajax调用服务插件，支持Deferred
 * User: Saintcy
 * Date: 13-4-26
 * Time: 下午4:28
 */
(function ($) {
    var _uid = 0,
        msie = /msie/.test(navigator.userAgent.toLowerCase()),
        /**
         * 创建一个iframe
         */
        _createFrame = function () {
            var serviceIframeNamePrefix = "__service_iframe__", index = _uid++;
            return $("<iframe id='" + serviceIframeNamePrefix + index + "' name='" + serviceIframeNamePrefix + index
                + "' width='0' height='0' style='display:none'></iframe>").appendTo("body");
        };

    var ServiceHelper = function (url, setting) {
        var _configs = $.extend({}, $.ServiceClient.defaults, setting);

        //识别通过后缀的请求类型
        var endIndex = url.indexOf("?");
        if (endIndex < 0) {
            endIndex = url.indexOf("#");
        }
        if (endIndex < 0) {
            endIndex = url.length;
        }
        var startIndex = url.lastIndexOf(".", endIndex), ext = (url.substring(startIndex + 1, endIndex) || "").toLowerCase();

        if (ext == "json") {
            _configs.contentType = "application/json;charset=utf-8";
        } else if (ext == "xml") {
            _configs.contentType = "application/xml;charset=utf-8";
        } else if (ext == "html") {
            _configs.contentType = "text/html;charset=utf-8";
        } else if (ext == "txt") {
            _configs.contentType = "text/plain;charset=utf-8";
        }

        _configs.dataType = _configs.dataType || ext;

        if (_configs.processData === undefined) {
            _configs.processData = true;
        }

        var _contentType = (_configs.contentType || "").toLowerCase();
        //自动将非字符串对象 转换成 相应的字符串
        if (_configs.processData && typeof _configs.data !== 'string') {
            if (_contentType.indexOf("json")) {
                _configs.data = JSON.stringify(_configs.data);
            } else if (_contentType.indexOf("xml")) {
                //暂不支持
            }
        }

        this.options = _configs;

        //回调函数
        this.callback = function (data, dtd) {
            if ($.isFunction(_configs.complete)) {
                try {
                    _configs.complete(data);
                } catch (e) {
                    $.error(e);
                }
            }

            if (data && !data.success) {
                if ($.isFunction(_configs.error)) {
                    try {
                        _configs.error(data);
                    } catch (e) {
                        $.error(e);
                    }
                }
            } else {
                if ($.isFunction(_configs.success)) {
                    try {
                        _configs.success(data)
                    } catch (e) {
                        $.error(e);
                    }
                }
            }

            try {
                if (data && !data.success) {
                    dtd.reject(data);
                } else {
                    dtd.resolve(data);
                }
            } catch (e) {
                $.error(e);
            }
        };
    };

    $.ServiceClient = {};

    $.ServiceClient.defaults = {
        timeout: 300000,//30s
        processData: true,
        type: 'POST',
        ignore401: true//忽略403错误，框架才能拦截弹出登录框
        //cache: true,//POST的时候默认不为true
        //contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    };

    /**
     * ajax调用服务
     * @param url {String}服务名
     * @param settings.data {Object} 发送到服务器的数据。
     * @param settings.type {String} post/get/delete/put
     * @param settings.callback {Function} 执行成功时的处理函数，return false则不再调用，return true则继续调用
     * @param settings.username 用于响应HTTP访问认证请求的用户名
     * @param settings.password 用于响应HTTP访问认证请求的密码
     * @param settings.timeout {Number} 超时时长
     * @param settings.contentType {String} 发送信息至服务器时内容编码类型(默认: "application/x-www-form-urlencoded")
     * @param settings.dataType {String} 预期服务器返回的数据类型。
     * @param settings.async {Boolean} 是否异步调用，默认true
     * @param settings.processData {Boolean}
     */
    $.ServiceClient.invoke = function (url, settings) {
        var _helper = new ServiceHelper(url, settings), dtd = $.Deferred(), options = $.extend({}, $.ServiceClient.defaults, _helper.options, {
            complete: false,
            success: function (data, textStatus, jqXHR) {
                _helper.callback(data, dtd);
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (options.ignore401 && XMLHttpRequest.status == 401) {
                    return;//不回调
                }
                _helper.callback({
                    success: false,
                    errcode: "http-" + XMLHttpRequest.status,
                    errmsg: errorThrown ? errorThrown.toString() : (textStatus == "timeout" ? "请求超时" : "请求失败")
                }, dtd);
            }
        });
        try {
            $.ajax(url, options);
        } catch (e) {
            _helper.callback({
                success: false,
                code: "ajax-" + (e.number & 0xFFFF),
                message: e.description
            }, dtd);
        }
        return dtd.promise();
    };

    /**
     * 用iframe方式提交表单，建议先提交附件，再用ajax提交
     * @param form  表单ID|表单name|dom|jquery
     * @param settings 同invoke
     */
    /*$.ServiceClient.submit = function (form, settings) {
     var _form, dfd = $.Deferred();
     if (typeof form == "string") {
     _form = $("#" + form).eq(0);
     if (_form.size() == 0) {
     _form = $("[name=" + form + "]").eq(0);
     }
     } else {
     _form = $(form);
     }

     var _helper = new ServiceHelper(_form.get(0).action, settings);
     jQuery.active++;
     jQuery.event.trigger("ajaxStart");
     var _frame = _createFrame(), _attrbak = { //备份form原始属性
     target: _form.attr("target"),
     action: _form.attr("action"),
     enctype: _form.attr("enctype"),
     method: _form.attr("method"),
     encoding: _form.attr("encoding")
     }, _msgInput = _form.find(":input[name=message]").eq(0);
     if (_msgInput.size() == 0) { //表单里面不能含有name=message的input
     _msgInput = $("<input type=hidden name='message'/>");
     _msgInput.appendTo(_form).val(_helper.options.data); // 将报文数据填入表单中
     }

     _form.attr("target", _frame.attr("name"));
     _form.attr("action", _helper.options.url);
     if (_form.attr("encoding")) {
     _form.attr('encoding', 'multipart/form-data');
     } else {
     _form.attr("enctype", "multipart/form-data");
     }
     _form.attr("method", "post");

     var _frDoc = null, _timeoutTimerId, _bTimeout = false, _processed = false,
     __restore = function () {
     if (_attrbak.target) {
     _form.attr("target", _attrbak.target);
     } else {
     _form.removeAttr("target");
     }
     if (_attrbak.action) {
     _form.attr("action", _attrbak.action);
     } else {
     _form.removeAttr("action");
     }
     if (_attrbak.enctype) {
     _form.attr("enctype", _attrbak.enctype);
     } else {
     _form.removeAttr("enctype");
     }
     if (_attrbak.encoding) {
     _form.attr("encoding", _attrbak.encoding);
     } else {
     _form.removeAttr("encoding");
     }
     if (_attrbak.method) {
     _form.attr("method", _attrbak.method);
     } else {
     _form.removeAttr("method");
     }
     //删除message字段
     _msgInput.remove();
     //iframe移除
     _frame.remove();
     },
     __checkSubmit = function () {//检查提交结果
     try {
     if (_processed) {
     return;
     }
     if (_bTimeout) {
     return;
     } else {
     if (msie) {
     if (_frame.get(0).readyState == "complete") {
     try {
     _frDoc = _frame.get(0).contentWindow.document;
     } catch (e) {
     _helper.callback({
     success: false,
     code: "ajax-" + (e.number & 0xFFFF),
     message: e.description
     }, dfd);
     _processed = true;
     }
     }
     } else {
     _frDoc = _frame.get(0).contentWindow.document;
     }

     if (!_processed && _frDoc && _frDoc.readyState == "complete") {
     // var body = frDoc.documentElement.lastChild;
     var body = _frDoc.getElementsByTagName("BODY").item(0);
     if (body) {
     var data = $.trim(body.innerText);//childNodes[0].
     if (data.charAt(0) == "{") {
     if (_helper.options.processData) {
     _helper.callback($.parseJSON(data), dfd);
     } else {
     _helper.callback(data, dfd);
     }
     } else if (data.indexOf("<?xml") == 0) {
     if (_helper.options.processData) {
     _helper.callback($.parseXML(data), dfd);
     } else {
     _helper.callback(data, dfd);
     }
     } else {// 服务端错误
     _helper.callback({
     success: false,
     code: "http-500",
     message: "请求错误"
     }, dfd);
     }
     _processed = true;
     }
     }
     }

     if (_processed) {
     window.clearTimeout(_timeoutTimerId);
     } else {
     // setTimeout(__checkSubmit, 30);
     }
     } catch (e) {
     window.clearTimeout(_timeoutTimerId);
     _helper.callback({
     success: false,
     code: "ajax-" + (e.number & 0xFFFF),
     message: e.description
     }, dfd);
     } finally {
     //还原表单
     __restore();
     jQuery.active--;
     jQuery.event.trigger("ajaxStop");
     }
     },
     __doTimeout = function () {
     _bTimeout = true;
     //还原表单
     __restore();
     _helper.callback({
     success: false,
     code: "http-408",
     message: "请求超时"
     }, dfd);
     };


     if (msie) {
     _frame.get(0).onreadystatechange = __checkSubmit;
     } else {
     _frame.get(0).onload = __checkSubmit;
     }
     //if (configs.sync) {// 同步不支持

     //} else {
     // setTimeout(__checkSubmit, 30);
     _timeoutTimerId = setTimeout(__doTimeout, _helper.timeout);//超时定时器
     //}
     _form.submit();//提交表单

     return dfd.promise();
     };*/

})(jQuery);