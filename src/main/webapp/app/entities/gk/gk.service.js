(function() {
    'use strict';
    angular
        .module('repoApp')
        .factory('Gk', Gk);

    Gk.$inject = ['$resource'];

    function Gk ($resource) {
        var resourceUrl =  'api/gks/:id';

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
