/*global angular*/
angular.module('Demo', ['Shared'])
    .controller('Main', function ($scope, $http, Validation, Calc) {
        $scope.model = {};

        $scope.emailValid = function () {
            return Validation.isValidEmail($scope.model.email) ? 'valid' : 'invalid';
        };

        $scope.growth = function () {
            return Calc.growthPerYear($scope.model.age, $scope.model.height);
        };

        $scope.send = function () {
            $http.post('/app/send', $scope.model).success(function (res) {
                $scope.response = res;
            });
        };
    });