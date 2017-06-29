(function() {
    'use strict';

    angular
        .module('repoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gk', {
            parent: 'entity',
            url: '/gk',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'repoApp.gk.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gk/gks.html',
                    controller: 'GkController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('gk');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('gk-detail', {
            parent: 'gk',
            url: '/gk/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'repoApp.gk.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gk/gk-detail.html',
                    controller: 'GkDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('gk');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Gk', function($stateParams, Gk) {
                    return Gk.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'gk',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('gk-detail.edit', {
            parent: 'gk-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gk/gk-dialog.html',
                    controller: 'GkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Gk', function(Gk) {
                            return Gk.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gk.new', {
            parent: 'gk',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gk/gk-dialog.html',
                    controller: 'GkDialogController',
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
                    $state.go('gk', null, { reload: 'gk' });
                }, function() {
                    $state.go('gk');
                });
            }]
        })
        .state('gk.edit', {
            parent: 'gk',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gk/gk-dialog.html',
                    controller: 'GkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Gk', function(Gk) {
                            return Gk.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gk', null, { reload: 'gk' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gk.delete', {
            parent: 'gk',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gk/gk-delete-dialog.html',
                    controller: 'GkDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Gk', function(Gk) {
                            return Gk.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gk', null, { reload: 'gk' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
