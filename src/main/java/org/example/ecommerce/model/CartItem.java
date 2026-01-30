package org.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- HIBERNATE MAPPING EXAMPLES ---

    /**
     * Many-to-One relationship with Cart.
     * 
     * @JoinColumn(name = "cart_id"): Specifies the foreign key column.
     * 
     *                  fetch = FetchType.LAZY: The Cart will not be loaded until
     *                  explicitly accessed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /**
     * Many-to-One relationship with Product.
     * 
     * fetch = FetchType.EAGER: The associated Product will always be fetched
     * together with the CartItem.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    // Convenience getters for backward compatibility
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }

    public String getProductName() {
        return product != null ? product.getName() : null;
    }
}
