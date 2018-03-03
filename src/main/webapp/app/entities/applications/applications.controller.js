(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .controller('ApplicationsController', ApplicationsController);

    ApplicationsController.$inject = ['Applications'];

    function ApplicationsController(Applications) {

        var vm = this;

        vm.applications = [];

        loadAll();

        function loadAll() {
            Applications.query(function(result) {
                vm.applications = result;
                vm.searchQuery = null;
            });
        }
    }
})();
