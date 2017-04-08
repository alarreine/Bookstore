(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('BookAddToCartController', BookAddToCartController);

 BookAddToCartController.$inject = ['Principal', 'Auth','$scope','$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Book', 'Cart', 'CartToBooks', 'User'];

    function BookAddToCartController (Principal, Auth,$scope, $stateParams, $uibModalInstance, DataUtils, entity, Book, Cart, CartToBooks, User) {
        var vm = this;
        vm.book = entity;
        vm.user = null;
        vm.cartid = 0;
        vm.cartToBook = null;
        vm.books = [];
 
        findCartId();
 
        vm.load = function (id) {
        Book.get({id: id}, function(result) {
          vm.book = result;
          });
        };
 
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
 
        vm.save = function() {
            addToCart();
            $uibModalInstance.dismiss('cancel');
        };
 
        function findCartId(){

            Principal.identity().then(function(account) {
                           User.get({login: account.login}, function (result) {
                                    vm.user = result;
                                    Cart.query({user: vm.user}, function (cart) {
                                               angular.forEach(cart, function(value, key){
                                                               if(value.user.id == vm.user.id){
                                                               vm.cartid = value.id;
                                                               checkIfAlreadyAdded();
                                                        }
                                                    });
                                               });
                            });
                    });
        }
 
        function addToCart(){
            if(vm.books.indexOf(vm.book.id) != -1) {
                $scope.message = 'Book already added!';
            }
            else{
                vm.cartToBook = new CartToBooks();
                vm.cartToBook.status = true;
                vm.cartToBook.bookId = vm.book.id;
                vm.cartToBook.cartId = vm.cartid;
                CartToBooks.save(vm.cartToBook, onSaveSuccess, onSaveError);
            }
        }
 
        function checkIfAlreadyAdded () {
            CartToBooks.query({cartId:vm.cartid}, function(result) {
                  angular.forEach(result, function(value, key){
                                  if(value.cartId == vm.cartid){
                                  Book.get({id:value.bookId}, function(book){
                                           vm.books.push(book.id);
                                    });
                                  }
                        });
                  });
        }
 
        var onSaveSuccess = function (result) {
 $scope.$emit('bookStoreApp:bookUpdate', result);
 $uibModalInstance.close(result);
        };
 
        var onSaveError = function () {
        };
 
 
    }
})();
