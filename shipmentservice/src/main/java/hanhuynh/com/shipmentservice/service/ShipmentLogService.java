package hanhuynh.com.shipmentservice.service;

import java.util.Date;

import hanhuynh.com.shipmentservice.entity.Shipment;
import hanhuynh.com.shipmentservice.event.OrderCreatedEvent;

public interface ShipmentLogService {

	public Shipment createOrGetShipment(OrderCreatedEvent event, Date estimatedDeliveryDateAsDate);
}
