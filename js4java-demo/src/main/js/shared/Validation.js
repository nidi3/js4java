/*global angular*/
angular.module('Shared')
    .service('Validation', function () {
        /**
         *
         * @param {string} email
         * @returns {!boolean} if it is valid email.
         */
        this.isValidEmail = function (email) {
            return !email || /^[A-Za-z0-9_\-]+@[A-Za-z0-9_\-]+\.[a-z]{2,}$/.test(email);
        };

        /**
         *
         * @param {number} age
         * @returns {!boolean}
         */
        this.isValidAge = function (age) {
            return !age || (age >= 0 && age <= 120);
        };

        /**
         *
         * @param {*} height
         * @returns {!boolean}
         */
        this.isValidHeight = function (height) {
            return !height || (typeof height === 'number' && height >= 50 && height <= 250);
        };
    });