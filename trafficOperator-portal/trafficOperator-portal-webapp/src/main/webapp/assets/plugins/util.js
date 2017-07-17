function escapeHTML(text) {
    text = text.replace(/</g, "&lt;");
    text = text.replace(/>/g, "&gt;");
    text = text.replace(/ /g, "&nbsp;");
    return text.replace(/\"/g, "&quot;");
}

function defaultIfEmpty(str) {
    if (str === null || str === undefined || s === NaN) {
        return "";
    }

    return str;
}

//截取单字节和双字节混和字符串的方法
function trunc(text, len) {
    text = nvl(text);

    if (text.getBytes() <= len) {
        return text;
    } else {
        return text.replace(/([\u0391-\uffe5])/ig, '$1a').substring(0, len).replace(/([\u0391-\uffe5])a/ig, '$1') + "...";
    }
}





