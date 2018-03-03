(function() {
    'use strict';
    angular
        .module('pathfinderApp')
        .factory('Assessments', Assessments);

    Assessments.$inject = ['$resource'];

    function Assessments ($resource) {
        var resourceUrl =  'api/assessments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
