$.extend({
    getUrlParams: function (url) {
        var url = url || window.location.href, vars = {}, parts = url.replace(/[?&]+([^=&]+)=([^&#]*)/gi,
            function (m, key, value) {
                key = decodeURI(key).replace("[]", "")
                value = decodeURI(value)
                if (vars[key]) {
                    vars[key] = $.makeArray(vars[key]).concat($.makeArray(value));
                } else {
                    vars[key] = value;
                }
            }
        );
        return vars;
    },
    getUrlParam: function (name, url) {
        return this.getUrlParams(url)[name];
    },
    /**
     * 返回，
     * 如果跳转过来的链接和需要返回的链接是一样的，则把原来的参数带上，并可以覆盖参数
     * @param referer 跳转过来的链接
     * @param target 返回的链接
     * @param params 返回的参数
     */
    goBack: function (referer, target, params) {
        params = params || {}
        if (referer && referer.indexOf(target) >= 0) {
            params = $.extend($.getUrlParams(referer), params)
        }
        var search = $.param(params), url = target + (search ? "?" : "") + search;

        location.assign(url);
    }
})