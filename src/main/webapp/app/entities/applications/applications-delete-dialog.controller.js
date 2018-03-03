(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .controller('ApplicationsDeleteController',ApplicationsDeleteController);

    ApplicationsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Applications'];

    function ApplicationsDeleteController($uibModalInstance, entity, Applications) {
        var vm = this;

        vm.applications = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Applications.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
