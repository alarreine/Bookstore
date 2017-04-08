(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('CartDetailController', CartDetailController);

    CartDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cart', 'User'];

    function CartDetailController($scope, $rootScope, $stateParams, previousState, entity, Cart, User) {
        var vm = this;

        vm.cart = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookstoreApp:cartUpdate', function(event, result) {
            vm.cart = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
