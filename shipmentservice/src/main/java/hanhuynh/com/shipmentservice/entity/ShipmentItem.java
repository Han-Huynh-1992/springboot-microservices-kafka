package hanhuynh.com.shipmentservice.entity;

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
@Table(name = "shipment_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentItem {

	@Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;
 
    @Column(name = "product_id", nullable = false)
    private String productId;
 
    @Column(name = "quantity", nullable = false)
    private int quantity;
}
