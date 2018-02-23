(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .controller('AssessmentsDetailController', AssessmentsDetailController);

    AssessmentsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Assessments'];

    function AssessmentsDetailController($scope, $rootScope, $stateParams, previousState, entity, Assessments) {
        var vm = this;

        vm.assessments = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pathfinderApp:assessmentsUpdate', function(event, result) {
            vm.assessments = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
