package hanhuynh.com.orderservice.dto;

import java.util.Date;
import java.util.List;

import hanhuynh.com.orderservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

	private String orderId;
	private String orderNumber;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private Double totalAmount;
    private OrderStatus status;
    private Date createdDate;
    private Date updatedDate;
    private List<OrderItemDTO> items;
}
