/*global obj,convert*/
var proxy = function (orig) {
    return function () {
        var param = [];
        for (var i = 0; i < arguments.length; i++) {
            param.push(convert(arguments[i]));
        }
        return orig.apply(this, param);  //TODO 'this' correct?
    };
};
for (var prop in obj) {
    if (obj.hasOwnProperty(prop) && typeof obj[prop] === 'function') {
        obj[prop] = proxy(obj[prop]);
    }
}