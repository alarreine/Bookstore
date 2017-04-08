(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('CartToBooksController', CartToBooksController);

    CartToBooksController.$inject = ['Principal', 'Auth','$scope', '$state', 'CartToBooks', 'Book', 'User', 'Cart'];

    function CartToBooksController(Principal,Auth,$scope, $state, CartToBooks, Book, User, Cart) {

 var vm = this;
 vm.cartToBooks = [];
 vm.books = [];
 vm.cartid = 0;
 vm.loadAll = function() {
 
 Principal.identity().then(function(account) {
                           User.get({login: account.login}, function (result) {
                                    vm.user = result;
                                    Cart.query({user: vm.user}, function (cart) {
                                               angular.forEach(cart, function(value, key){
                                                               if(value.user.id == vm.user.id){
                                                               vm.cartid = value.id;
                                                               loadBooks();
                                                               }
                                                               });
                                               });
                                    });
                           });
 };
 
 vm.loadAll();
 
 function loadBooks(){
 
 CartToBooks.query({cartId:vm.cartId}, function(result) {
                  vm.cartToBooks = result;
                  angular.forEach(result, function(value, key){
                                  if(value.cartId == vm.cartid){
                                  Book.get({id:value.bookId}, function(book){
                                           vm.books.push(book);
                                           console.log(book);
                                           });
                                  }
                                  });
                  });
 }
    }
})();
