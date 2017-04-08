(function() {
    'use strict';

    angular
        .module('bookstoreApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cart-to-books', {
            parent: 'entity',
            url: '/cart-to-books',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookstoreApp.cartToBooks.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cart-to-books/cart-to-books.html',
                    controller: 'CartToBooksController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cartToBooks');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cart-to-books-detail', {
            parent: 'cart-to-books',
            url: '/cart-to-books/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookstoreApp.cartToBooks.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cart-to-books/cart-to-books-detail.html',
                    controller: 'CartToBooksDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cartToBooks');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CartToBooks', function($stateParams, CartToBooks) {
                    return CartToBooks.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cart-to-books',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cart-to-books-detail.edit', {
            parent: 'cart-to-books-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cart-to-books/cart-to-books-dialog.html',
                    controller: 'CartToBooksDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CartToBooks', function(CartToBooks) {
                            return CartToBooks.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cart-to-books.new', {
            parent: 'cart-to-books',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cart-to-books/cart-to-books-dialog.html',
                    controller: 'CartToBooksDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cartId: null,
                                bookId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cart-to-books', null, { reload: 'cart-to-books' });
                }, function() {
                    $state.go('cart-to-books');
                });
            }]
        })
        .state('cart-to-books.edit', {
            parent: 'cart-to-books',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cart-to-books/cart-to-books-dialog.html',
                    controller: 'CartToBooksDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CartToBooks', function(CartToBooks) {
                            return CartToBooks.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cart-to-books', null, { reload: 'cart-to-books' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cart-to-books.delete', {
            parent: 'cart-to-books',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cart-to-books/cart-to-books-delete-dialog.html',
                    controller: 'CartToBooksDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CartToBooks', function(CartToBooks) {
                            return CartToBooks.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cart-to-books', null, { reload: 'cart-to-books' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
