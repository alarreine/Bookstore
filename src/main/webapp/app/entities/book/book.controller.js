(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .controller('BookController', BookController);

    BookController.$inject = ['DataUtils', 'Book'];

    function BookController(DataUtils, Book) {

        var vm = this;

        vm.books = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Book.query(function(result) {
                vm.books = result;
                vm.searchQuery = null;
            });
        }
    }
})();
