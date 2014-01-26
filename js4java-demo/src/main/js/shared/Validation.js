/*global angular*/
angular.module('Shared')
    .service('Validation', function () {
        /**
         *
         * @param {string} email
         * @returns {boolean} if it is valid email.
         */
        this.isValidEmail = function (email) {
            return !!email && /^[A-Za-z0-9_\-]+@[A-Za-z0-9_\-]+\.[a-z]{2,}$/.test(email);
        };
    });