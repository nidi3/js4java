angular.module('Arith').service('AddService', function () {
    this.add = function (a, b) {
        return a + b;
    };
});