package hanhuynh.com.shipmentservice.event;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentCreatedEvent {

	private String shipmentId;
    private String orderId;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String shippingAddress;
    private Date estimatedDeliveryDate;
    private Date createdDate;
}
