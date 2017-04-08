package etu.uga.bookstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import etu.uga.bookstore.domain.CartToBooks;

import etu.uga.bookstore.repository.CartToBooksRepository;
import etu.uga.bookstore.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CartToBooks.
 */
@RestController
@RequestMapping("/api")
public class CartToBooksResource {

    private final Logger log = LoggerFactory.getLogger(CartToBooksResource.class);

    private static final String ENTITY_NAME = "cartToBooks";
        
    private final CartToBooksRepository cartToBooksRepository;

    public CartToBooksResource(CartToBooksRepository cartToBooksRepository) {
        this.cartToBooksRepository = cartToBooksRepository;
    }

    /**
     * POST  /cart-to-books : Create a new cartToBooks.
     *
     * @param cartToBooks the cartToBooks to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartToBooks, or with status 400 (Bad Request) if the cartToBooks has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cart-to-books")
    @Timed
    public ResponseEntity<CartToBooks> createCartToBooks(@RequestBody CartToBooks cartToBooks) throws URISyntaxException {
        log.debug("REST request to save CartToBooks : {}", cartToBooks);
        if (cartToBooks.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cartToBooks cannot already have an ID")).body(null);
        }
        CartToBooks result = cartToBooksRepository.save(cartToBooks);
        return ResponseEntity.created(new URI("/api/cart-to-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cart-to-books : Updates an existing cartToBooks.
     *
     * @param cartToBooks the cartToBooks to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartToBooks,
     * or with status 400 (Bad Request) if the cartToBooks is not valid,
     * or with status 500 (Internal Server Error) if the cartToBooks couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cart-to-books")
    @Timed
    public ResponseEntity<CartToBooks> updateCartToBooks(@RequestBody CartToBooks cartToBooks) throws URISyntaxException {
        log.debug("REST request to update CartToBooks : {}", cartToBooks);
        if (cartToBooks.getId() == null) {
            return createCartToBooks(cartToBooks);
        }
        CartToBooks result = cartToBooksRepository.save(cartToBooks);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cartToBooks.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cart-to-books : get all the cartToBooks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cartToBooks in body
     */
    @GetMapping("/cart-to-books")
    @Timed
    public List<CartToBooks> getAllCartToBooks() {
        log.debug("REST request to get all CartToBooks");
        List<CartToBooks> cartToBooks = cartToBooksRepository.findAll();
        return cartToBooks;
    }

    /**
     * GET  /cart-to-books/:id : get the "id" cartToBooks.
     *
     * @param id the id of the cartToBooks to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cartToBooks, or with status 404 (Not Found)
     */
    @GetMapping("/cart-to-books/{id}")
    @Timed
    public ResponseEntity<CartToBooks> getCartToBooks(@PathVariable Long id) {
        log.debug("REST request to get CartToBooks : {}", id);
        CartToBooks cartToBooks = cartToBooksRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cartToBooks));
    }

    /**
     * DELETE  /cart-to-books/:id : delete the "id" cartToBooks.
     *
     * @param id the id of the cartToBooks to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cart-to-books/{id}")
    @Timed
    public ResponseEntity<Void> deleteCartToBooks(@PathVariable Long id) {
        log.debug("REST request to delete CartToBooks : {}", id);
        cartToBooksRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
