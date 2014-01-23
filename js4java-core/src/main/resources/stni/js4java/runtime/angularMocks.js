window = (function () {
    return this;
}());
document = {
    addEventListener: function () {
    },
    createElement: function () {
        return {
            setAttribute: function () {
            },
            pathname: 'xxx',
            removeChild: function () {
            }
        };
    },
    getElementsByTagName: function () {
    }
};
addEventListener = function () {
};
location = {href: 'http://my.com/path', protocol: 'http'};
setTimeout = function () {
};
navigator = {};