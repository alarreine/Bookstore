package etu.uga.bookstore.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CartToBooks.
 */
@Entity
@Table(name = "cart_to_books")
public class CartToBooks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "cart_id")
    private Integer cartId;

    @Column(name = "book_id")
    private Integer bookId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCartId() {
        return cartId;
    }

    public CartToBooks cartId(Integer cartId) {
        this.cartId = cartId;
        return this;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public CartToBooks bookId(Integer bookId) {
        this.bookId = bookId;
        return this;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartToBooks cartToBooks = (CartToBooks) o;
        if (cartToBooks.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cartToBooks.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CartToBooks{" +
            "id=" + id +
            ", cartId='" + cartId + "'" +
            ", bookId='" + bookId + "'" +
            '}';
    }
}
