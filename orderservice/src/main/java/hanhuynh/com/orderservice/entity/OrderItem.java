package hanhuynh.com.orderservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

	@Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
 
    @Column(name = "product_id", nullable = false)
    private String productId;
 
    @Column(name = "product_name", nullable = false)
    private String productName;
 
    @Column(name = "quantity", nullable = false)
    private int quantity;
 
    @Column(name = "price", nullable = false)
    private Double price;
}
