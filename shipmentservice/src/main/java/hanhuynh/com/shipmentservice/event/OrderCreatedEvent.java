package hanhuynh.com.shipmentservice.event;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent {

	private String orderId;
    private String customerId;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private Double totalAmount;
    private List<OrderItemDTO> items;
    private Date createdDate;
 
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDTO {
        private String productId;
        private String productName;
        private int quantity;
        private Double price;
    }
}
