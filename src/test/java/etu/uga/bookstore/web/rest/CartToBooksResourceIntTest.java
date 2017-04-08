package etu.uga.bookstore.web.rest;

import etu.uga.bookstore.BookstoreApp;

import etu.uga.bookstore.domain.CartToBooks;
import etu.uga.bookstore.repository.CartToBooksRepository;
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
 * Test class for the CartToBooksResource REST controller.
 *
 * @see CartToBooksResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApp.class)
public class CartToBooksResourceIntTest {

    private static final Integer DEFAULT_CART_ID = 1;
    private static final Integer UPDATED_CART_ID = 2;

    private static final Integer DEFAULT_BOOK_ID = 1;
    private static final Integer UPDATED_BOOK_ID = 2;

    @Autowired
    private CartToBooksRepository cartToBooksRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCartToBooksMockMvc;

    private CartToBooks cartToBooks;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CartToBooksResource cartToBooksResource = new CartToBooksResource(cartToBooksRepository);
        this.restCartToBooksMockMvc = MockMvcBuilders.standaloneSetup(cartToBooksResource)
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
    public static CartToBooks createEntity(EntityManager em) {
        CartToBooks cartToBooks = new CartToBooks()
            .cartId(DEFAULT_CART_ID)
            .bookId(DEFAULT_BOOK_ID);
        return cartToBooks;
    }

    @Before
    public void initTest() {
        cartToBooks = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartToBooks() throws Exception {
        int databaseSizeBeforeCreate = cartToBooksRepository.findAll().size();

        // Create the CartToBooks
        restCartToBooksMockMvc.perform(post("/api/cart-to-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartToBooks)))
            .andExpect(status().isCreated());

        // Validate the CartToBooks in the database
        List<CartToBooks> cartToBooksList = cartToBooksRepository.findAll();
        assertThat(cartToBooksList).hasSize(databaseSizeBeforeCreate + 1);
        CartToBooks testCartToBooks = cartToBooksList.get(cartToBooksList.size() - 1);
        assertThat(testCartToBooks.getCartId()).isEqualTo(DEFAULT_CART_ID);
        assertThat(testCartToBooks.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
    }

    @Test
    @Transactional
    public void createCartToBooksWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartToBooksRepository.findAll().size();

        // Create the CartToBooks with an existing ID
        cartToBooks.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartToBooksMockMvc.perform(post("/api/cart-to-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartToBooks)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CartToBooks> cartToBooksList = cartToBooksRepository.findAll();
        assertThat(cartToBooksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCartToBooks() throws Exception {
        // Initialize the database
        cartToBooksRepository.saveAndFlush(cartToBooks);

        // Get all the cartToBooksList
        restCartToBooksMockMvc.perform(get("/api/cart-to-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartToBooks.getId().intValue())))
            .andExpect(jsonPath("$.[*].cartId").value(hasItem(DEFAULT_CART_ID)))
            .andExpect(jsonPath("$.[*].bookId").value(hasItem(DEFAULT_BOOK_ID)));
    }

    @Test
    @Transactional
    public void getCartToBooks() throws Exception {
        // Initialize the database
        cartToBooksRepository.saveAndFlush(cartToBooks);

        // Get the cartToBooks
        restCartToBooksMockMvc.perform(get("/api/cart-to-books/{id}", cartToBooks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartToBooks.getId().intValue()))
            .andExpect(jsonPath("$.cartId").value(DEFAULT_CART_ID))
            .andExpect(jsonPath("$.bookId").value(DEFAULT_BOOK_ID));
    }

    @Test
    @Transactional
    public void getNonExistingCartToBooks() throws Exception {
        // Get the cartToBooks
        restCartToBooksMockMvc.perform(get("/api/cart-to-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartToBooks() throws Exception {
        // Initialize the database
        cartToBooksRepository.saveAndFlush(cartToBooks);
        int databaseSizeBeforeUpdate = cartToBooksRepository.findAll().size();

        // Update the cartToBooks
        CartToBooks updatedCartToBooks = cartToBooksRepository.findOne(cartToBooks.getId());
        updatedCartToBooks
            .cartId(UPDATED_CART_ID)
            .bookId(UPDATED_BOOK_ID);

        restCartToBooksMockMvc.perform(put("/api/cart-to-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCartToBooks)))
            .andExpect(status().isOk());

        // Validate the CartToBooks in the database
        List<CartToBooks> cartToBooksList = cartToBooksRepository.findAll();
        assertThat(cartToBooksList).hasSize(databaseSizeBeforeUpdate);
        CartToBooks testCartToBooks = cartToBooksList.get(cartToBooksList.size() - 1);
        assertThat(testCartToBooks.getCartId()).isEqualTo(UPDATED_CART_ID);
        assertThat(testCartToBooks.getBookId()).isEqualTo(UPDATED_BOOK_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingCartToBooks() throws Exception {
        int databaseSizeBeforeUpdate = cartToBooksRepository.findAll().size();

        // Create the CartToBooks

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCartToBooksMockMvc.perform(put("/api/cart-to-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartToBooks)))
            .andExpect(status().isCreated());

        // Validate the CartToBooks in the database
        List<CartToBooks> cartToBooksList = cartToBooksRepository.findAll();
        assertThat(cartToBooksList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCartToBooks() throws Exception {
        // Initialize the database
        cartToBooksRepository.saveAndFlush(cartToBooks);
        int databaseSizeBeforeDelete = cartToBooksRepository.findAll().size();

        // Get the cartToBooks
        restCartToBooksMockMvc.perform(delete("/api/cart-to-books/{id}", cartToBooks.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CartToBooks> cartToBooksList = cartToBooksRepository.findAll();
        assertThat(cartToBooksList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartToBooks.class);
    }
}
