(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('CartToBooksController', CartToBooksController);

    CartToBooksController.$inject = ['CartToBooks'];

    function CartToBooksController(CartToBooks) {

        var vm = this;

        vm.cartToBooks = [];

        loadAll();

        function loadAll() {
            CartToBooks.query(function(result) {
                vm.cartToBooks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
