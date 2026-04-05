package hanhuynh.com.orderservice.service;

import hanhuynh.com.orderservice.dto.OrderRequest;
import hanhuynh.com.orderservice.dto.OrderResponse;

public interface OrderService {
	
	/**
	 * create new order
	 * @param request
	 * @return OrderResponse obj
	 */
	OrderResponse createOrder(OrderRequest request);
}
