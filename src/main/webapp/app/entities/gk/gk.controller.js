(function() {
    'use strict';

    angular
        .module('repoApp')
        .controller('GkController', GkController);

    GkController.$inject = ['Gk'];

    function GkController(Gk) {

        var vm = this;

        vm.gks = [];

        loadAll();

        function loadAll() {
            Gk.query(function(result) {
                vm.gks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
