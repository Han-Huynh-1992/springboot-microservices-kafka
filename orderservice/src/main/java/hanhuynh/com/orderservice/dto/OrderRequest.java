package hanhuynh.com.orderservice.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrderRequest {
	
	@NotBlank(message = "{order.validation.required}")
    private String customerId;
 
    @NotBlank(message = "{order.validation.required}")
    private String customerName;
 
    @Email(message = "{order.customerEmail.invalid}")
    @NotBlank(message = "{order.validation.required}")
    private String customerEmail;
    
    @NotBlank(message = "{order.validation.required}")
    private String customerAddress;
 
    @NotEmpty(message = "{order.items.required}")
    @Valid
    private List<OrderItemRequest> items;
 
    /**
     * Nested DTO for each item
     */
    @Data
    public static class OrderItemRequest {
 
        @NotBlank(message = "{order.validation.required}")
        private String productId;
 
        @NotBlank(message = "{order.validation.required}")
        private String productName;
 
        @Min(value = 1, message = "{order.item.quantity.min}")
        private int quantity;
 
        @DecimalMin(value = "0.0", inclusive = false, message = "{order.item.price.min}")
        private Double price;
    }
}
