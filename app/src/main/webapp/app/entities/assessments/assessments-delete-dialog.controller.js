(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .controller('AssessmentsDeleteController',AssessmentsDeleteController);

    AssessmentsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Assessments'];

    function AssessmentsDeleteController($uibModalInstance, entity, Assessments) {
        var vm = this;

        vm.assessments = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Assessments.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
