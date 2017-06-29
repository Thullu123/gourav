(function() {
    'use strict';

    angular
        .module('repoApp')
        .controller('GkDialogController', GkDialogController);

    GkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Gk'];

    function GkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Gk) {
        var vm = this;

        vm.gk = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.gk.id !== null) {
                Gk.update(vm.gk, onSaveSuccess, onSaveError);
            } else {
                Gk.save(vm.gk, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('repoApp:gkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
