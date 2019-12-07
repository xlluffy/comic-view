String.prototype.format = function (args) {
    let result = this;
    if (arguments.length > 0) {
        if (arguments.length === 1 && typeof (args) == "object") {
            for (let key in args) {
                if (args[key] !== undefined) {
                    let reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (let i = 0; i < arguments.length; i++) {
                if (arguments[i] !== undefined) {
                    let reg = new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
};

Number.prototype.format = function (args) {
    let temp = this.toString();
    let count = temp.length;
    let prefix = "";
    for (let i = 0; i < args - count; i++) {
        prefix += "0";
    }
    return prefix + temp;
};


function getRequest(href) {
    let url = href == null ? location.search.substr(1) : href.substr(href.indexOf('?') + 1);
    let requestPairs = url.split('&');
    let request = [];
    for (let i = 0; i < requestPairs.length; i++) {
        let requestPair = requestPairs[i].split('=');
        request[requestPair[0]] = requestPair[1];
    }
    return request;
}

function splitCookies() {
    let cookies = document.cookie.split(";");
    let cookieMap = {};
    for (let x in cookies) {
        let i = cookies[x].indexOf("=");
        cookieMap[cookies[x].substring(0, i)] = cookies[x].substring(i + 1, cookies[x].length)
    }
    return cookieMap
}
