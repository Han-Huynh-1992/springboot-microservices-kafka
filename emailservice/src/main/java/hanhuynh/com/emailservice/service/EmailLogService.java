package hanhuynh.com.emailservice.service;

import hanhuynh.com.emailservice.entity.EmailLog;
import hanhuynh.com.emailservice.event.OrderCreatedEvent;

public interface EmailLogService {

	public EmailLog createOrGetEmailLog(OrderCreatedEvent event, String subject);
	
	public boolean existsByOrderId(String orderId);
	
	public void markAsSent(EmailLog emailLog, String body);
	
	public void markAsFailed(EmailLog emailLog, String error);
}
