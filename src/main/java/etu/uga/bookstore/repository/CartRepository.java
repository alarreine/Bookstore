package etu.uga.bookstore.repository;

import etu.uga.bookstore.domain.Cart;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cart entity.
 */
@SuppressWarnings("unused")
public interface CartRepository extends JpaRepository<Cart,Long> {

}
