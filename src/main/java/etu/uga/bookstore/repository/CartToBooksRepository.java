package etu.uga.bookstore.repository;

import etu.uga.bookstore.domain.CartToBooks;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CartToBooks entity.
 */
@SuppressWarnings("unused")
public interface CartToBooksRepository extends JpaRepository<CartToBooks,Long> {

}
