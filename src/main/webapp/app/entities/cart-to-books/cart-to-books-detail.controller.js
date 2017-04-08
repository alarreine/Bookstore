(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('CartToBooksDetailController', CartToBooksDetailController);

    CartToBooksDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CartToBooks'];

    function CartToBooksDetailController($scope, $rootScope, $stateParams, previousState, entity, CartToBooks) {
        var vm = this;

        vm.cartToBooks = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookstoreApp:cartToBooksUpdate', function(event, result) {
            vm.cartToBooks = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
