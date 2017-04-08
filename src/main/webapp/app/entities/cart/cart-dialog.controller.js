(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('CartDialogController', CartDialogController);

    CartDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Cart', 'User'];

    function CartDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Cart, User) {
        var vm = this;

        vm.cart = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cart.id !== null) {
                Cart.update(vm.cart, onSaveSuccess, onSaveError);
            } else {
                Cart.save(vm.cart, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bookstoreApp:cartUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
