(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .controller('AssessmentsController', AssessmentsController);

    AssessmentsController.$inject = ['Assessments'];

    function AssessmentsController(Assessments) {

        var vm = this;

        vm.assessments = [];

        loadAll();

        function loadAll() {
            Assessments.query(function(result) {
                vm.assessments = result;
                vm.searchQuery = null;
            });
        }
    }
})();
