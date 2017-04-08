(function() {
    'use strict';
    angular
        .module('bookstoreApp')
        .factory('CartToBooks', CartToBooks);

    CartToBooks.$inject = ['$resource'];

    function CartToBooks ($resource) {
        var resourceUrl =  'api/cart-to-books/:id';

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
