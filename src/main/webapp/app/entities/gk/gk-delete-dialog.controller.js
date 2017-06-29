(function() {
    'use strict';

    angular
        .module('repoApp')
        .controller('GkDeleteController',GkDeleteController);

    GkDeleteController.$inject = ['$uibModalInstance', 'entity', 'Gk'];

    function GkDeleteController($uibModalInstance, entity, Gk) {
        var vm = this;

        vm.gk = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Gk.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
