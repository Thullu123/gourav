(function() {
    'use strict';

    angular
        .module('repoApp')
        .controller('GkDetailController', GkDetailController);

    GkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Gk'];

    function GkDetailController($scope, $rootScope, $stateParams, previousState, entity, Gk) {
        var vm = this;

        vm.gk = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('repoApp:gkUpdate', function(event, result) {
            vm.gk = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
