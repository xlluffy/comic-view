String.prototype.format = function (args) {
    let result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "object") {
            for (let key in args) {
                if (args[key] != undefined) {
                    let reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (let i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
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
