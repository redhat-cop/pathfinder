(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .controller('AssessmentsDialogController', AssessmentsDialogController);

    AssessmentsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Assessments'];

    function AssessmentsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Assessments) {
        var vm = this;

        vm.assessments = entity;
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
            if (vm.assessments.id !== null) {
                Assessments.update(vm.assessments, onSaveSuccess, onSaveError);
            } else {
                Assessments.save(vm.assessments, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pathfinderApp:assessmentsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
