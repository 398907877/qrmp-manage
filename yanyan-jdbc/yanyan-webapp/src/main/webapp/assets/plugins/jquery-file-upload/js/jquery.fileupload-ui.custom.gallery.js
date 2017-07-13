/**
 * Created by Saintcy on 2016/9/4.
 */
;(function (factory) {
    'use strict';
    if (typeof define === 'function' && define.amd) {
        // Register as an anonymous AMD module:
        define([
            'jquery'
        ], factory);
    } else if (typeof exports === 'object') {
        // Node/CommonJS:
        factory(
            require('jquery')
        );
    } else {
        // Browser globals:
        factory(
            window.jQuery
        );
    }
}(function ($) {
    'use strict';
    $.widget('blueimp.fileupload', $.blueimp.fileupload, {
        options: {
            fileTypes: /^image\/(gif|jpg|jpeg|png|bmp)$/i,
            maxFileSize: 10000000, // 10MB
            single: false,//单张图片
            addButtonPosition: 'head',//增加按钮的位置head/tail/none
            removable: true,//是否可删除
            imageItemClasses: "thumbnail",//图片的gallery-item的样式
            add: function (e, data) {
                if (e.isDefaultPrevented()) {
                    return false;
                }
                var $this = $(this),
                    that = $this.data('blueimp-fileupload') ||
                        $this.data('fileupload'),
                    options = that.options,
                    upload = true;
                data.context = $this.closest(".gallery");
                $.each(data.files, function (index, file) {
                    if ((options.fileTypes && file.type && !options.fileTypes.test(file.type))) {
                        var fileTypes = options.fileTypes.toString(), start = fileTypes.indexOf("(") + 1, end = fileTypes.lastIndexOf(")");
                        layer.alert("文件格式必须为" + fileTypes.substring(start, end), {icon: 2});
                        upload = false;
                        return false;
                    }
                    if (($.type(options.maxFileSize) === 'number' &&
                        file.size > options.maxFileSize)) {
                        layer.alert("文件大小不能超过" + formatFileSize(options.maxFileSize), {icon: 2});
                        upload = false;
                        return false;
                    }
                });

                if (upload) {
                    if ((that._trigger('added', e, data) !== false)) {
                        data.submit();
                    }
                } else {
                    data.abort();
                }
            },
            send: function (e, data) {
                if (e.isDefaultPrevented()) {
                    return false;
                }
                var that = $(this).data('blueimp-fileupload') ||
                    $(this).data('fileupload');
                if (data.context && data.dataType &&
                    data.dataType.substr(0, 6) === 'iframe') {
                    // Iframe Transport does not support progress events.
                    // In lack of an indeterminate progress bar, we set
                    // the progress to 100%, showing the full animated bar:
                    data.context
                        .find('.progress').addClass(
                            !$.support.transition && '.progress-striped .active'
                        )
                        .attr('aria-valuenow', 100)
                        .children().first().css(
                        'width',
                        '100%'
                    );
                }
                return that._trigger('sent', e, data);
            },
            done: function (e, data) {
                var $this = $(this),
                    that = $this.data('blueimp-fileupload') ||
                        $this.data('fileupload'),
                    options = that.options;
                if (e.isDefaultPrevented()) {
                    return false;
                }
                if (data.result.success) {
                    $.each(data.result.files, function (index, file) {
                        that._renderPhoto(data.context, file, options.urlContext);
                    });
                    that._renderAddButton(data.context)
                } else {
                    layer.alert(data.result.message, {icon: 2});
                }
                that._trigger('completed', e, data);
                that._trigger('finished', e, data);
            },
            fail: function (e, data) {
                var $this = $(this),
                    that = $this.data('blueimp-fileupload') ||
                        $this.data('fileupload'),
                    options = that.options;
                if (e.isDefaultPrevented()) {
                    return false;
                }

                data.abort();
                layer.alert("上传失败", {icon: 2});
                $.each(data.files, function (file) {
                    console.error(file.error || data.errorThrown || data.i18n('unknownError'));
                });
                that._trigger('failed', e, data);
                that._trigger('finished', e, data);
            },
            progressall: function (e, data) {
                if (e.isDefaultPrevented()) {
                    return false;
                }
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $('.progress-bar', data.context).css(
                    'width',
                    progress + '%'
                ).text(formatBitrate(data.bitrate) + ' | ' +
                    formatTime(
                        (data.total - data.loaded) * 8 / data.bitrate
                    ) + ' | ' +
                    formatPercentage(
                        data.loaded / data.total
                    ) + ' | ' +
                    formatFileSize(data.loaded) + ' / ' +
                    formatFileSize(data.total));
            },
            start: function (e) {
                var $this = $(this),
                    that = $(this).data('blueimp-fileupload') ||
                        $(this).data('fileupload');
                that._renderProgress($this.closest(".gallery"));
            },
            stop: function (e) {
                var $this = $(this),
                    that = $(this).data('blueimp-fileupload') ||
                        $(this).data('fileupload');
                that._removeProgress($this.closest(".gallery"));
            }
        },
        _initEventHandlers: function () {
            this._super();
            var _this = this, $gallery = this.element.closest(".gallery");
            this._renderAddButton($gallery);
            $gallery.on("click", ".gallery-img", function (e) {
                e.preventDefault();
                e.stopPropagation();
                var thisFile = $(this).closest(".gallery-item").data("file"), start = 0, index = -1, data = $.map(_this.files(), function (file) {
                    index++;
                    if (file == thisFile) {
                        start = index
                    }
                    return {//相册包含的图片，数组格式
                        "alt": file.name,
                        "pid": index, //图片id
                        "src": _this.options.urlContext + file.fileUrl, //原图地址
                        "thumb": _this.options.urlContext + file.thumbnailUrl //缩略图地址
                    }
                });
                layer.photos({
                    photos: {
                        "title": "", //相册标题
                        "id": 123, //相册id
                        "start": start, //初始显示的图片序号，默认0
                        "data": data
                    }
                })
            }).on("click", ".action.remove", function (e) {
                e.preventDefault();
                e.stopPropagation();
                if (_this.options.single) {
                    $(this).closest(".gallery").find(".gallery-item:first").show()
                }
                $(this).closest(".gallery-item").remove();
            })
        },
        _renderProgress: function (context) {
            return $('<div class="fileupload-progress">' +
                '<div class="progress"><div class="progress-bar bg-color-teal text-right" role="progressbar"></div></div>' +
                '</div>').prependTo(context);
        },
        _removeProgress: function (context) {
            context.find(".fileupload-progress").remove();
        },
        _renderPhoto: function (context, file, urlContext) {
            return $('<div class="gallery-item ' + (this.options.imageItemClasses || '') + '">' +
                '<img src="' + urlContext + file.thumbnailUrl + '" title="' + file.name + '" class="gallery-img">' +
                '<a href="javascript:" class="action remove" style="' + (this.options.removable ? '' : 'display:none') + '"><i class="fa fa-fw fa-times-circle"></i></a>' +
                '</div>').data("file", file).appendTo(context);
        },
        _renderAddButton: function (context) {
            var $addButton = context.find(".gallery-item.gallery-new"),
                pos = (this.options.addButtonPosition || "head").toLowerCase();
            if (pos == "head") {
                context.prepend($addButton)
            } else if (pos == "tail") {
                context.append($addButton)
            } else {
                $addButton.hide()
            }
            if (this.options.single && context.find(".gallery-item:not(.gallery-new)").length > 0) {
                $addButton.hide()
            }
        },
        files: function () {
            var files = [];
            this.element.closest(".gallery").find(".gallery-item").each(function (i, item) {
                var file = $(item).data("file");
                if (file) {
                    files.push(file);
                }
            });
            return files;
        }
    });
}));