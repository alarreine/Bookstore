package etu.uga.bookstore.web.rest;

import etu.uga.bookstore.BookstoreApp;

import etu.uga.bookstore.domain.Cart;
import etu.uga.bookstore.repository.CartRepository;
import etu.uga.bookstore.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CartResource REST controller.
 *
 * @see CartResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApp.class)
public class CartResourceIntTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCartMockMvc;

    private Cart cart;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CartResource cartResource = new CartResource(cartRepository);
        this.restCartMockMvc = MockMvcBuilders.standaloneSetup(cartResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createEntity(EntityManager em) {
        Cart cart = new Cart();
        return cart;
    }

    @Before
    public void initTest() {
        cart = createEntity(em);
    }

    @Test
    @Transactional
    public void createCart() throws Exception {
        int databaseSizeBeforeCreate = cartRepository.findAll().size();

        // Create the Cart
        restCartMockMvc.perform(post("/api/carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isCreated());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate + 1);
        Cart testCart = cartList.get(cartList.size() - 1);
    }

    @Test
    @Transactional
    public void createCartWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartRepository.findAll().size();

        // Create the Cart with an existing ID
        cart.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartMockMvc.perform(post("/api/carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCarts() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList
        restCartMockMvc.perform(get("/api/carts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get the cart
        restCartMockMvc.perform(get("/api/carts/{id}", cart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cart.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCart() throws Exception {
        // Get the cart
        restCartMockMvc.perform(get("/api/carts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart
        Cart updatedCart = cartRepository.findOne(cart.getId());

        restCartMockMvc.perform(put("/api/carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCart)))
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Create the Cart

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCartMockMvc.perform(put("/api/carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isCreated());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);
        int databaseSizeBeforeDelete = cartRepository.findAll().size();

        // Get the cart
        restCartMockMvc.perform(delete("/api/carts/{id}", cart.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cart.class);
    }
}
