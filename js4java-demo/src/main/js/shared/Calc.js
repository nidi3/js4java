/*global angular*/
angular.module('Shared')
    .service('Calc', function () {
        /**
         *
         * @param {?number} age
         * @param {?number} height
         * @returns {?number}
         */
        this.growthPerYear = function (age, height) {
            return (height - 50) / age;
        };
    });