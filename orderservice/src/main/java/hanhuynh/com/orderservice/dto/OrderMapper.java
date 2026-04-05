package hanhuynh.com.orderservice.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import hanhuynh.com.orderservice.entity.Order;
import hanhuynh.com.orderservice.entity.OrderItem;
import hanhuynh.com.orderservice.event.OrderCreatedEvent;

@Component
public class OrderMapper {

	/**
	 * Mapping OrderRequest to Order entity
	 * @param request
	 * @return Order obj
	 */
    public Order toOrder(OrderRequest request) {
    	String orderId = UUID.randomUUID().toString();
        String orderNumber = "ORD-" + orderId.replace("-", "").substring(0, 8).toUpperCase();
        
        Order order = Order.builder()
                .orderId(orderId)
                .orderNumber(orderNumber)
                .customerId(request.getCustomerId())
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .customerAddress(request.getCustomerAddress())
                .build();
 
        List<OrderItem> items = request.getItems().stream()
                .map(itemReq -> toOrderItem(itemReq, order))
                .toList();
 
        double totalAmount = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
 
        order.setTotalAmount(totalAmount);
        items.forEach(order::addItem);
 
        return order;
    }
 
    private OrderItem toOrderItem(OrderRequest.OrderItemRequest req, Order order) {
        return OrderItem.builder()
                .id(UUID.randomUUID().toString())
                .order(order)
                .productId(req.getProductId())
                .productName(req.getProductName())
                .quantity(req.getQuantity())
                .price(req.getPrice())
                .build();
    }
 
    /**
     * Mapping Order entity to OrderResponse
     * @param order
     * @return OrderResponse obj
     */
    public OrderResponse toOrderResponse(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();
 
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomerId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .customerAddress(order.getCustomerAddress())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdDate(order.getCreatedDate())
                .updatedDate(order.getUpdatedDate())
                .items(itemDTOs)
                .build();
    }

    /**
     * Mapping OrderResponse to OrderCreatedEvent (Kafka payload)
     * @param response
     * @return OrderCreatedEvent obj
     */
    public OrderCreatedEvent toOrderCreatedEvent(OrderResponse response) {
        return OrderCreatedEvent.builder()
                .orderId(response.getOrderId())
                .orderNumber(response.getOrderNumber())
                .customerId(response.getCustomerId())
                .customerName(response.getCustomerName())
                .customerEmail(response.getCustomerEmail())
                .customerAddress(response.getCustomerAddress())
                .totalAmount(response.getTotalAmount())
                .items(response.getItems())
                .createdDate(response.getCreatedDate())
                .build();
    }
}
