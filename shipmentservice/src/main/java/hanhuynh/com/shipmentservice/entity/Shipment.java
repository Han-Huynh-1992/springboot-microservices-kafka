package hanhuynh.com.shipmentservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hanhuynh.com.shipmentservice.enums.ShipmentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

	@Id
    @Column(name = "shipment_id", nullable = false, updatable = false)
    private String shipmentId;
 
    @Column(name = "order_id", nullable = false)
    private String orderId;
 
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
 
    @Column(name = "estimated_delivery_date", nullable = false)
    private Date estimatedDeliveryDate;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ShipmentStatus status = ShipmentStatus.PENDING;
 
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;
 
    @Column(name = "shipped_date")
    private Date shippedDate;
 
    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ShipmentItem> items = new ArrayList<>();
 
    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
    }
 
    public void addItem(ShipmentItem item) {
        item.setShipment(this);
        this.items.add(item);
    }
}
