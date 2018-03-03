(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('assessments', {
            parent: 'entity',
            url: '/assessments',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Assessments'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/assessments/assessments.html',
                    controller: 'AssessmentsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('assessments-detail', {
            parent: 'assessments',
            url: '/assessments/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Assessments'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/assessments/assessments-detail.html',
                    controller: 'AssessmentsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Assessments', function($stateParams, Assessments) {
                    return Assessments.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'assessments',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('assessments-detail.edit', {
            parent: 'assessments-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assessments/assessments-dialog.html',
                    controller: 'AssessmentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Assessments', function(Assessments) {
                            return Assessments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('assessments.new', {
            parent: 'assessments',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assessments/assessments-dialog.html',
                    controller: 'AssessmentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                results: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('assessments', null, { reload: 'assessments' });
                }, function() {
                    $state.go('assessments');
                });
            }]
        })
        .state('assessments.edit', {
            parent: 'assessments',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assessments/assessments-dialog.html',
                    controller: 'AssessmentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Assessments', function(Assessments) {
                            return Assessments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('assessments', null, { reload: 'assessments' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('assessments.delete', {
            parent: 'assessments',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assessments/assessments-delete-dialog.html',
                    controller: 'AssessmentsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Assessments', function(Assessments) {
                            return Assessments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('assessments', null, { reload: 'assessments' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
