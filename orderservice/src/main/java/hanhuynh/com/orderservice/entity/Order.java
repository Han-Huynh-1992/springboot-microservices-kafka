package hanhuynh.com.orderservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hanhuynh.com.orderservice.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

	@Id
    @Column(name = "order_id", nullable = false, updatable = false)
    private String orderId;
	
	@Column(name = "order_number", nullable = false, updatable = false, unique = true)
    private String orderNumber;
 
    @Column(name = "customer_id", nullable = false)
    private String customerId;
 
    @Column(name = "customer_name", nullable = false)
    private String customerName;
 
    @Column(name = "customer_email", nullable = false)
    private String customerEmail;
    
    @Column(name = "customer_address", nullable = false)
    private String customerAddress;
    
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.CREATED;
 
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;
 
    @Column(name = "updated_date")
    private Date updatedDate;
 
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
 
    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
 
    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date();
    }
 
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }
}
