(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('CartToBooksDeleteController',CartToBooksDeleteController);

    CartToBooksDeleteController.$inject = ['$uibModalInstance', 'entity', 'CartToBooks'];

    function CartToBooksDeleteController($uibModalInstance, entity, CartToBooks) {
        var vm = this;

        vm.cartToBooks = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CartToBooks.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
