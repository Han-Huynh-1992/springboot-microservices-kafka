package hanhuynh.com.emailservice.service;

import hanhuynh.com.emailservice.enums.EmailStatus;
import hanhuynh.com.emailservice.event.OrderCreatedEvent;

public interface EmailService {

	/**
	 * send email and store log
	 * @param event
	 */
	void sendOrderConfirmation(OrderCreatedEvent event);
}
