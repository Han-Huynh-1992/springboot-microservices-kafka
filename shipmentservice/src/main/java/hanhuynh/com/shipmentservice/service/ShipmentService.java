package hanhuynh.com.shipmentservice.service;

import hanhuynh.com.shipmentservice.event.OrderCreatedEvent;

public interface ShipmentService {

	/**
     * Create a shipment record from OrderCreatedEvent, and calculate the estimatedDeliveryDate
     */
    void createShipment(OrderCreatedEvent event);
}
