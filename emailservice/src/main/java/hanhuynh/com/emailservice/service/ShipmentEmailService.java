package hanhuynh.com.emailservice.service;

import hanhuynh.com.emailservice.event.ShipmentCreatedEvent;

public interface ShipmentEmailService {

	/**
     * Send a shipment notification email to the customer
     */
    void sendShipmentNotification(ShipmentCreatedEvent event);
}
