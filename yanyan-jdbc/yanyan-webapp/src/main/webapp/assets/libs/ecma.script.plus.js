window._parseFloat = window.parseFloat;
window._parseInt = window.parseInt;
window.formatInt = function (s) {
    return formatFloat(s, 0);
}

window.formatFloat = function (s, n) {
    if (s === null || s === undefined || s === "" || isNaN(s)) {
        return "";
    }

    n = n >= 0 && n <= 20 ? n : 2;
    s = window._parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(),
        r = s.split(".")[1], i, t = "";
    for (i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + (n == 0 ? "" : "." + r);
}

window.parseFloat = function (s) {
    if (s === null || s === undefined || s === "") {
        return NaN;
    }

    if (s == Infinity || s == -Infinity) {
        return s;
    }

    var rs = window._parseFloat((s + "").replace(/[^\d\.-]/g, ""));
    return isNaN(rs) ? NaN : rs;
}

window.parseInt = function (s, radix) {
    if (s === null || s === undefined || s === "") {
        return NaN;
    }

    if (s == Infinity || s == -Infinity) {
        return s;
    }

    var rs = window._parseInt((s + "").replace(/[^\d\.-]/g, ""), radix);
    return isNaN(rs) ? NaN : rs;
}


window.formatFileSize = function (bytes) {
    if (typeof bytes !== 'number') {
        return '';
    }
    if (bytes >= 1000000000) {
        return (bytes / 1000000000).toFixed(2) + ' GB';
    }
    if (bytes >= 1000000) {
        return (bytes / 1000000).toFixed(2) + ' MB';
    }
    return (bytes / 1000).toFixed(2) + ' KB';
}

window.formatBitrate = function (bits) {
    if (typeof bits !== 'number') {
        return '';
    }
    if (bits >= 1000000000) {
        return (bits / 1000000000).toFixed(2) + ' Gbit/s';
    }
    if (bits >= 1000000) {
        return (bits / 1000000).toFixed(2) + ' Mbit/s';
    }
    if (bits >= 1000) {
        return (bits / 1000).toFixed(2) + ' kbit/s';
    }
    return bits.toFixed(2) + ' bit/s';
}

window.formatTime = function (seconds) {
    var date = new Date(seconds * 1000),
        days = Math.floor(seconds / 86400);
    days = days ? days + 'd ' : '';
    return days +
        ('0' + date.getUTCHours()).slice(-2) + ':' +
        ('0' + date.getUTCMinutes()).slice(-2) + ':' +
        ('0' + date.getUTCSeconds()).slice(-2);
}

window.formatPercentage = function (floatValue) {
    return (floatValue * 100).toFixed(2) + ' %';
}

Array.prototype.indexOf = function (v) {
    for (var i = this.length; i-- && this[i] !== v;);
    return i;
};

Array.prototype.remove = function (b) {
    var a = this.indexOf(b);
    if (a >= 0) {
        this.splice(a, 1);
        return true;
    }
    return false;
};

//获取字符串长度的方法，汉字2字节，字符1字节
String.prototype.getBytes = function () {
    var cArr = this.match(/[^\x00-\xff]/ig);
    return this.length + (cArr == null ? 0 : cArr.length);
};