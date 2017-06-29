(function() {
    'use strict';

    angular
        .module('repoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('pdetail', {
            parent: 'app',
            url: '/pdetail',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pdetail/pdetail.html',
                    controller: 'PdetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('pdetail');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
