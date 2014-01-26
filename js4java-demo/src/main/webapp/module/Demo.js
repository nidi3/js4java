/*global angular*/
angular.module('Demo', ['Shared'])
    .controller('Main', function ($scope, $http, Validation, Calc) {
        $scope.model = {};

        $scope.emailValid = function () {
            return Validation.isValidEmail($scope.model.email);
        };
        $scope.ageValid = function () {
            return Validation.isValidAge($scope.model.age);
        };
        $scope.heightValid = function () {
            return Validation.isValidHeight($scope.model.height);
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