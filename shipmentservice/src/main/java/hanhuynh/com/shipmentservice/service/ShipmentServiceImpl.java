package hanhuynh.com.shipmentservice.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanhuynh.com.shipmentservice.entity.Shipment;
import hanhuynh.com.shipmentservice.entity.ShipmentItem;
import hanhuynh.com.shipmentservice.event.OrderCreatedEvent;
import hanhuynh.com.shipmentservice.event.ShipmentCreatedEvent;
import hanhuynh.com.shipmentservice.repository.ShipmentRepository;
import hanhuynh.com.shipmentservice.util.MessageUtil;
import lombok.Data;

@Service
@Data
public class ShipmentServiceImpl implements ShipmentService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ShipmentLogServiceImpl shipmentLogServiceImpl;
	
	@Autowired
    private DeliveryDateCalculator deliveryDateCalculator;
	
	@Autowired
	@Qualifier("jsonKafkaTemplate")
    private KafkaTemplate<String, Object> producer;
	
	@Autowired
	private MessageUtil messageUtil;
	
	private static final String TOPIC_SHIPMENT_EMAIL = "shipment-email-topic";
 
    @Override
    @Transactional
    public void createShipment(OrderCreatedEvent event) {
    	logger.info(messageUtil.get("shipment.log.processing", event.getOrderId()));
 
        // Calculate estimatedDeliveryDate
        LocalDate orderDate = event.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate estimatedDeliveryDate = deliveryDateCalculator.calculate(orderDate);
        Date estimatedDeliveryDateAsDate = Date.from(estimatedDeliveryDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        logger.info(messageUtil.get("shipment.log.delivery.date", event.getOrderId(), estimatedDeliveryDate));
 
        Shipment shipment;
        try {
            shipment = shipmentLogServiceImpl.createOrGetShipment(event, estimatedDeliveryDateAsDate);
        } catch (Exception ex) {
            logger.error(messageUtil.get("shipment.log.createdShipment.failed", event.getOrderId()), ex);
            throw new RuntimeException(ex);
        }
 
        try {
        	// Publish ShipmentCreatedEvent to EmailService
        	ShipmentCreatedEvent shipmentEvent = ShipmentCreatedEvent.builder()
        			.shipmentId(shipment.getShipmentId())
        			.orderId(event.getOrderId())
        			.orderNumber(event.getOrderNumber())
        			.customerName(event.getCustomerName())
        			.customerEmail(event.getCustomerEmail())
        			.shippingAddress(shipment.getShippingAddress())
        			.estimatedDeliveryDate(estimatedDeliveryDateAsDate)
        			.createdDate(new Date())
        			.build();
        	
        	producer.send(TOPIC_SHIPMENT_EMAIL, event.getOrderId(), shipmentEvent);
        	logger.info(messageUtil.get("shipment.log.event.sent", event.getOrderId()));
        } catch (Exception ex) {
        	logger.error(messageUtil.get("shipment.log.event.failed", event.getOrderId()), ex.getMessage());
        	
        	// Throw exception that retry mechanism can catch error
            throw new RuntimeException(ex);
        }
    }
}
