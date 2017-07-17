if ($.validator) {
    $.validator.setDefaults({
        onkeyup: false,
        //errorClass: 'help-block',
        validClass: 'valid',
        ignore: "",//取消默认忽略:hidden
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        unhighlight: function (element) {
            //处理一个group 有多个框的情况，当有一个以上有错误的时候就不能移除
            var $formGroup = $(element).closest('.form-group');
            //validator显示顺序highlight->show label text->unhighlight->hide error text->show error text
            //所以在unhighlight阶段只能找出错误文本不空的，并且不是本身的字段
            if ($formGroup.find(".invalid:not(:empty)").not("#" + element.id + "-error").length == 0) {
                $formGroup.removeClass('has-error');
            }
        },
        errorElement: 'small',
        errorClass: 'invalid help-block',
        errorPlacement: function (error, element) {
            if (element.is(":checkbox,:radio")) {
                error.insertAfter(element.parents("[class*='checkbox']").siblings(":last"));
            } else if (element.is(".select2")) {
                error.insertAfter(element.siblings(":last"));
            } else {
                if (element.parent().is(".input-group,.mix-group")) {
                    error.insertAfter(element.parent());
                } else {
                    error.insertAfter(element);
                }
            }
        }
    });
    $.validator.addMethod("unique", function (value, element, params) {
        if (this.optional(element)) return true;
        var deferred = $.Deferred();//创建一个延迟对象
        $.ajax($.extend(params, {
            async: false,//要指定不能异步,必须等待后台服务校验完成再执行后续代码
            success: function (data) {
                if (data.success) {
                    deferred.resolve();
                } else {
                    /*if (data.errmsg && data.errmsg.indexOf("先登录") >= 0) {//登录过期
                     layer.alert(data.errmsg, {icon: 2});
                     }*/
                    deferred.reject();
                }
            }
        }));
        //deferred.state()有3个状态:pending:还未结束,rejected:失败,resolved:成功
        return deferred.state() == "resolved" ? true : false;
    }, "编号已存在");
    $.validator.addMethod("mobile", function (value, element, params) {
        var reg = new RegExp("(^1[3|5|7|8]{1}[0-9]{9}$)");
        return this.optional(element) || reg.test(value);
    }, "手机号格式不正确");
}