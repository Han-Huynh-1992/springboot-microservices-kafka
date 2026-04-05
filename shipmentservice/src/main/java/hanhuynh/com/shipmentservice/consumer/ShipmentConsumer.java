package hanhuynh.com.shipmentservice.consumer;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;

import hanhuynh.com.shipmentservice.entity.ShipmentFailedLog;
import hanhuynh.com.shipmentservice.enums.ShipmentResolvedStatus;
import hanhuynh.com.shipmentservice.event.OrderCreatedEvent;
import hanhuynh.com.shipmentservice.repository.ShipmentFailedLogRepository;
import hanhuynh.com.shipmentservice.service.ShipmentService;
import hanhuynh.com.shipmentservice.util.MessageUtil;


@Service
public class ShipmentConsumer {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String TOPIC_ORDER_SHIPMENT = "order-shipment-topic";
	
	@Autowired
	private ShipmentService shipmentService;
	
	@Autowired
	private ShipmentFailedLogRepository shipmentFailedLogRepository;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@RetryableTopic(attempts = "5", dltTopicSuffix = "-dlt", backOff = @BackOff(delay = 2000, multiplier = 2), kafkaTemplate = "byteArrayKafkaTemplate")
	@KafkaListener(id = "shipment-id", groupId = "shipment-group-id", topics = TOPIC_ORDER_SHIPMENT)
	public void listenEvent(OrderCreatedEvent event) {
		logger.info("Received: {}", event.getCustomerEmail());
		shipmentService.createShipment(event);
	}
	
	@KafkaListener(id = "dltShipmentId", groupId = "dltShipmentGroupId", topics = TOPIC_ORDER_SHIPMENT + "-dlt")
	public void dltOrderListen(OrderCreatedEvent event) {
		logger.error(messageUtil.get("shipment.dlt.received", TOPIC_ORDER_SHIPMENT, event.getOrderId(), event.getCustomerEmail()));
		
		ShipmentFailedLog failedLog = shipmentFailedLogRepository.findByOrderId(event.getOrderId());
		
		if (failedLog == null) {
			// Save failed shipment
			failedLog = ShipmentFailedLog.builder()
					.id(UUID.randomUUID().toString())
					.orderId(event.getOrderId())
					.orderNumber(event.getOrderNumber())
					.customerName(event.getCustomerName())
					.customerEmail(event.getCustomerEmail())
					.errorMessage(messageUtil.get("shipment.dlt.error.message", event.getOrderId()))
					.resolvedStatus(ShipmentResolvedStatus.PENDING)
					.build();
			
			shipmentFailedLogRepository.save(failedLog);
			
			logger.error(messageUtil.get("shipment.dlt.saved", event.getOrderId()));
		}
	}
}
