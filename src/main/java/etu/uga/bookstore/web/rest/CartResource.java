package etu.uga.bookstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import etu.uga.bookstore.domain.Cart;

import etu.uga.bookstore.repository.CartRepository;
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
 * REST controller for managing Cart.
 */
@RestController
@RequestMapping("/api")
public class CartResource {

    private final Logger log = LoggerFactory.getLogger(CartResource.class);

    private static final String ENTITY_NAME = "cart";
        
    private final CartRepository cartRepository;

    public CartResource(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /**
     * POST  /carts : Create a new cart.
     *
     * @param cart the cart to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cart, or with status 400 (Bad Request) if the cart has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/carts")
    @Timed
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) throws URISyntaxException {
        log.debug("REST request to save Cart : {}", cart);
        if (cart.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cart cannot already have an ID")).body(null);
        }
        Cart result = cartRepository.save(cart);
        return ResponseEntity.created(new URI("/api/carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /carts : Updates an existing cart.
     *
     * @param cart the cart to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cart,
     * or with status 400 (Bad Request) if the cart is not valid,
     * or with status 500 (Internal Server Error) if the cart couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/carts")
    @Timed
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart) throws URISyntaxException {
        log.debug("REST request to update Cart : {}", cart);
        if (cart.getId() == null) {
            return createCart(cart);
        }
        Cart result = cartRepository.save(cart);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cart.getId().toString()))
            .body(result);
    }

    /**
     * GET  /carts : get all the carts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of carts in body
     */
    @GetMapping("/carts")
    @Timed
    public List<Cart> getAllCarts() {
        log.debug("REST request to get all Carts");
        List<Cart> carts = cartRepository.findAll();
        return carts;
    }

    /**
     * GET  /carts/:id : get the "id" cart.
     *
     * @param id the id of the cart to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cart, or with status 404 (Not Found)
     */
    @GetMapping("/carts/{id}")
    @Timed
    public ResponseEntity<Cart> getCart(@PathVariable Long id) {
        log.debug("REST request to get Cart : {}", id);
        Cart cart = cartRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cart));
    }

    /**
     * DELETE  /carts/:id : delete the "id" cart.
     *
     * @param id the id of the cart to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/carts/{id}")
    @Timed
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        log.debug("REST request to delete Cart : {}", id);
        cartRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
