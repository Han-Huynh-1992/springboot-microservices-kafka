package hanhuynh.com.orderservice.event;

import java.util.Date;
import java.util.List;

import hanhuynh.com.orderservice.dto.OrderItemDTO;
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
	private String orderNumber;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private Double totalAmount;
    private List<OrderItemDTO> items;
    private Date createdDate;
}
