(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('applications', {
            parent: 'entity',
            url: '/applications',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Applications'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/applications/applications.html',
                    controller: 'ApplicationsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('applications-detail', {
            parent: 'applications',
            url: '/applications/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Applications'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/applications/applications-detail.html',
                    controller: 'ApplicationsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Applications', function($stateParams, Applications) {
                    return Applications.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'applications',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('applications-detail.edit', {
            parent: 'applications-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/applications/applications-dialog.html',
                    controller: 'ApplicationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Applications', function(Applications) {
                            return Applications.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('applications.new', {
            parent: 'applications',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/applications/applications-dialog.html',
                    controller: 'ApplicationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('applications', null, { reload: 'applications' });
                }, function() {
                    $state.go('applications');
                });
            }]
        })
        .state('applications.edit', {
            parent: 'applications',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/applications/applications-dialog.html',
                    controller: 'ApplicationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Applications', function(Applications) {
                            return Applications.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('applications', null, { reload: 'applications' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('applications.delete', {
            parent: 'applications',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/applications/applications-delete-dialog.html',
                    controller: 'ApplicationsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Applications', function(Applications) {
                            return Applications.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('applications', null, { reload: 'applications' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
