(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .controller('ApplicationsDialogController', ApplicationsDialogController);

    ApplicationsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Applications'];

    function ApplicationsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Applications) {
        var vm = this;

        vm.applications = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.applications.id !== null) {
                Applications.update(vm.applications, onSaveSuccess, onSaveError);
            } else {
                Applications.save(vm.applications, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pathfinderApp:applicationsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
