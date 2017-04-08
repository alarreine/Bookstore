(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('CartToBooksDialogController', CartToBooksDialogController);

    CartToBooksDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CartToBooks'];

    function CartToBooksDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CartToBooks) {
        var vm = this;

        vm.cartToBooks = entity;
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
            if (vm.cartToBooks.id !== null) {
                CartToBooks.update(vm.cartToBooks, onSaveSuccess, onSaveError);
            } else {
                CartToBooks.save(vm.cartToBooks, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bookstoreApp:cartToBooksUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
