(function() {
    'use strict';

    angular
        .module('pathfinderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer', {
            parent: 'entity',
            url: '/customer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Customers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer/customers.html',
                    controller: 'CustomerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('customer-detail', {
            parent: 'customer',
            url: '/customer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Customer'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer/customer-detail.html',
                    controller: 'CustomerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Customer', function($stateParams, Customer) {
                    return Customer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'customer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('customer-detail.edit', {
            parent: 'customer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-dialog.html',
                    controller: 'CustomerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer.new', {
            parent: 'customer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-dialog.html',
                    controller: 'CustomerDialogController',
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
                    $state.go('customer', null, { reload: 'customer' });
                }, function() {
                    $state.go('customer');
                });
            }]
        })
        .state('customer.edit', {
            parent: 'customer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-dialog.html',
                    controller: 'CustomerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer', null, { reload: 'customer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer.delete', {
            parent: 'customer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-delete-dialog.html',
                    controller: 'CustomerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer', null, { reload: 'customer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
