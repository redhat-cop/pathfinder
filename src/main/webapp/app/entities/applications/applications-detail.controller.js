(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .controller('ApplicationsDetailController', ApplicationsDetailController);

    ApplicationsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Applications'];

    function ApplicationsDetailController($scope, $rootScope, $stateParams, previousState, entity, Applications) {
        var vm = this;

        vm.applications = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pathfinderApp:applicationsUpdate', function(event, result) {
            vm.applications = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
