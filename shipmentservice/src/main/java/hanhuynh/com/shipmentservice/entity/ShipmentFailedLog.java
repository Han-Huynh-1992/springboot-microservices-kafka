package hanhuynh.com.shipmentservice.entity;

import java.util.Date;

import hanhuynh.com.shipmentservice.enums.ShipmentResolvedStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shipment_failed_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentFailedLog {

	@Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
 
    @Column(name = "order_id", nullable = false)
    private String orderId;
 
    @Column(name = "order_number")
    private String orderNumber;
 
    @Column(name = "customer_name")
    private String customerName;
 
    @Column(name = "customer_email")
    private String customerEmail;
 
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "resolved_status", nullable = false)
    @Builder.Default
    private ShipmentResolvedStatus resolvedStatus = ShipmentResolvedStatus.PENDING;
 
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;
 
    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
    }
}
