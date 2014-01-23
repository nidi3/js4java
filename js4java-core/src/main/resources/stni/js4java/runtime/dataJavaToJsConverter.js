/*global java*/
function convert(obj) {
    var map = {};
    return doConvert(obj);

    function doConvert(obj) {
        if (obj === null) {
            return null;
        }
        if (obj instanceof java.lang.Class) {
            return '' + obj.getName();
        }
        if (obj instanceof java.lang.String) {
            return '' + obj;
        }
        if (obj instanceof java.lang.Number) {
            return obj.doubleValue();
        }
        if (obj instanceof java.lang.Boolean) {
            return obj.booleanValue();
        }
        if (obj instanceof java.util.Date) {
            return new Date(obj.getTime());
        }
        if (typeof obj === 'object') {
            var hash = java.lang.System.identityHashCode(obj),
                res = {};

            if (map[hash]) {
                return map[hash];
            }
            map[hash] = res;
            for (var prop in obj) {
                if (typeof obj[prop] === 'function') {
                    if (prop.substring(0, 3) === 'get') {
                        res[prop.charAt(3).toLowerCase() + prop.substring(4)] = doConvert(obj[prop]());
                    } else if (prop.substring(0, 2) == 'is') {
                        res[prop.charAt(2).toLowerCase() + prop.substring(3)] = doConvert(obj[prop]());
                    }
                } else {
                    res[prop] = doConvert(obj[prop]);
                }
            }
            return res;
        }
        return obj;
    }
}