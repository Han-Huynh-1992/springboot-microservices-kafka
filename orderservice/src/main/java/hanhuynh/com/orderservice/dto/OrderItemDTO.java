package hanhuynh.com.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
	private String productId;
    private String productName;
    private int quantity;
    private Double price;
}
