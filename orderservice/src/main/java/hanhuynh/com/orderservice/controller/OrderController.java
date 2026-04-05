package hanhuynh.com.orderservice.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hanhuynh.com.orderservice.dto.OrderMapper;
import hanhuynh.com.orderservice.dto.OrderRequest;
import hanhuynh.com.orderservice.dto.OrderResponse;
import hanhuynh.com.orderservice.entity.OutboxEvent;
import hanhuynh.com.orderservice.enums.OutboxStatus;
import hanhuynh.com.orderservice.event.OrderCreatedEvent;
import hanhuynh.com.orderservice.repository.OutboxRepository;
import hanhuynh.com.orderservice.service.OrderService;
import hanhuynh.com.orderservice.util.MessageUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/order")
public class OrderController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String TOPIC_ORDER_EMAIL = "order-email-topic";
    private static final String TOPIC_ORDER_SHIPMENT = "order-shipment-topic";
    
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OutboxRepository outboxRepository;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	@Transactional
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
		logger.info(messageUtil.get("order.controller.log.received", request.getCustomerId() + " - " + request.getCustomerEmail()));
		
        OrderResponse response = orderService.createOrder(request);
        logger.info(messageUtil.get("order.controller.log.created", response.getOrderId()));
 
        // Mapping OrderResponse to OrderCreatedEvent object (necessary information for consumers) to send events
        OrderCreatedEvent event = orderMapper.toOrderCreatedEvent(response);
 
        // Serialize OrderCreatedEvent to JSON String to store outbox_events
        String payload = toJson(event);
        
        outboxRepository.save(buildOutboxEvent(TOPIC_ORDER_EMAIL, response.getOrderId(), payload));
        outboxRepository.save(buildOutboxEvent(TOPIC_ORDER_SHIPMENT, response.getOrderId(), payload));
 
        logger.info(messageUtil.get("order.log.outbox.saved", response.getOrderId()));
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	private OutboxEvent buildOutboxEvent(String topic, String messageKey, String payload) {
        return OutboxEvent.builder()
                .id(UUID.randomUUID().toString())
                .topic(topic)
                .messageKey(messageKey)
                .payload(payload)
                .status(OutboxStatus.PENDING)
                .build();
    }
 
	private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Failed to serialize event to JSON", ex);
        }
    }
}
