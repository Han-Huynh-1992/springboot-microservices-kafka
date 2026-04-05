package hanhuynh.com.shipmentservice.service;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanhuynh.com.shipmentservice.entity.Shipment;
import hanhuynh.com.shipmentservice.entity.ShipmentItem;
import hanhuynh.com.shipmentservice.event.OrderCreatedEvent;
import hanhuynh.com.shipmentservice.repository.ShipmentRepository;
import hanhuynh.com.shipmentservice.util.MessageUtil;
import lombok.Data;

@Service
@Data
public class ShipmentLogServiceImpl implements ShipmentLogService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Transactional
	@Override
	public Shipment createOrGetShipment(OrderCreatedEvent event, Date estimatedDeliveryDateAsDate) {
	    Shipment existingShipment = shipmentRepository.findByOrderId(event.getOrderId());
	    
	    if (existingShipment != null) {
	        return existingShipment;
	    }
	    
	    // Build Shipment entity
        Shipment shipment = Shipment.builder()
                .shipmentId(UUID.randomUUID().toString())
                .orderId(event.getOrderId())
                .shippingAddress(event.getCustomerAddress())
                .estimatedDeliveryDate(estimatedDeliveryDateAsDate)
                .build();
 
        // Map items
        event.getItems().forEach(item -> {
            ShipmentItem shipmentItem = ShipmentItem.builder()
                    .id(UUID.randomUUID().toString())
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .build();
            shipment.addItem(shipmentItem);
        });
        
        // Store shipment record
    	shipmentRepository.save(shipment);
    	logger.info(messageUtil.get("shipment.log.saved", event.getOrderId()));
	    
	    return shipment;
	}
}
