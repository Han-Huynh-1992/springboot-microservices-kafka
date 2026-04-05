package hanhuynh.com.orderservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanhuynh.com.orderservice.dto.OrderMapper;
import hanhuynh.com.orderservice.dto.OrderRequest;
import hanhuynh.com.orderservice.dto.OrderResponse;
import hanhuynh.com.orderservice.entity.Order;
import hanhuynh.com.orderservice.repository.OrderRepository;
import hanhuynh.com.orderservice.util.MessageUtil;
import lombok.Data;

@Service
@Data
public class OrderServiceImpl implements OrderService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
    private OrderMapper orderMapper;
	
	@Autowired
	private MessageUtil messageUtil;
 
    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
    	logger.info(messageUtil.get("order.service.log.creating", request.getCustomerId()));
 
        // Mapping request to entity
        Order order = orderMapper.toOrder(request);
 
        // Save Order info
        Order savedOrder = orderRepository.save(order);
        logger.info(messageUtil.get("order.service.log.saved", savedOrder.getOrderId()));
 
        return orderMapper.toOrderResponse(savedOrder);
    }
}
