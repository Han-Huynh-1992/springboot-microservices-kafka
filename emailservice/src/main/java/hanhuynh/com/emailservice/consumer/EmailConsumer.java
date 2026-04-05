package hanhuynh.com.emailservice.consumer;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

import hanhuynh.com.emailservice.entity.EmailLog;
import hanhuynh.com.emailservice.enums.EmailStatus;
import hanhuynh.com.emailservice.event.OrderCreatedEvent;
import hanhuynh.com.emailservice.event.ShipmentCreatedEvent;
import hanhuynh.com.emailservice.repository.EmailLogRepository;
import hanhuynh.com.emailservice.service.EmailLogService;
import hanhuynh.com.emailservice.service.EmailService;
import hanhuynh.com.emailservice.service.ShipmentEmailService;
import hanhuynh.com.emailservice.util.MessageUtil;

@Component
public class EmailConsumer {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String TOPIC_ORDER_EMAIL = "order-email-topic";
	private static final String TOPIC_SHIPMENT_EMAIL = "shipment-email-topic";
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
    private EmailLogService emailLogService;
	
	@Autowired
	private ShipmentEmailService shipmentEmailService;
	
	@Autowired
	private EmailLogRepository emailLogRepository;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@RetryableTopic(attempts = "5", dltTopicSuffix = "-dlt", backOff = @BackOff(delay = 2000, multiplier = 2))
	@KafkaListener(id = "order-email-id", groupId = "order-email-group-id", topics = TOPIC_ORDER_EMAIL)
	public void listenOrderEvent(OrderCreatedEvent event) {
		logger.info("Received: {}", event.getCustomerEmail());
		emailService.sendOrderConfirmation(event);
	}
	
	@RetryableTopic(attempts = "5", dltTopicSuffix = "-dlt", backOff = @BackOff(delay = 2000, multiplier = 2))
	@KafkaListener(id = "shipment-email-id", groupId = "shipment-email-group-id", topics = TOPIC_SHIPMENT_EMAIL)
	public void listenShipmentEvent(ShipmentCreatedEvent event) {
		logger.info("Received: {}", event.getCustomerEmail());
		shipmentEmailService.sendShipmentNotification(event);
	}
	
	@KafkaListener(id = "dltOrderId", groupId="dltOrderGroupId", topics = TOPIC_ORDER_EMAIL + "-dlt")
	public void dltOrderListen(OrderCreatedEvent event) {
		logger.error(messageUtil.get("email.dlt.received", TOPIC_ORDER_EMAIL, event.getOrderId(), event.getCustomerEmail()));
		
		if (!emailLogService.existsByOrderId(event.getOrderId())) {
			saveEmailLog(event.getOrderId(), event.getCustomerEmail(), messageUtil.get("email.dlt.subject"), 
					messageUtil.get("email.dlt.body", TOPIC_ORDER_EMAIL), 
					EmailStatus.FAILED, messageUtil.get("email.dlt.error.message", event.getOrderId()));
			
			logger.error(messageUtil.get("email.dlt.saved", event.getOrderId()));
		}
	}
	
	@KafkaListener(id = "dltShipmentId", groupId = "dltShipmentGroupId", topics = TOPIC_SHIPMENT_EMAIL + "-dlt")
	public void dltShipmentListen(ShipmentCreatedEvent event) {
		logger.error(messageUtil.get("email.dlt.received", TOPIC_SHIPMENT_EMAIL, event.getOrderId(), event.getCustomerEmail()));
		
		if (!emailLogService.existsByOrderId(event.getOrderId())) {
			saveEmailLog(event.getOrderId(), event.getCustomerEmail(), messageUtil.get("email.dlt.subject"), 
					messageUtil.get("email.dlt.body", TOPIC_SHIPMENT_EMAIL), 
					EmailStatus.FAILED, messageUtil.get("email.dlt.error.message", event.getOrderId()));
			
			logger.error(messageUtil.get("email.dlt.saved", event.getOrderId()));
		}
	}
	
	private void saveEmailLog(String orderId, String customerEmail, String subject, String body, EmailStatus status, String errorMessage) {
        EmailLog emailLog = EmailLog.builder()
                .emailId(UUID.randomUUID().toString())
                .orderId(orderId)
                .recipientEmail(customerEmail)
                .subject(subject)
                .body(body)
                .status(status)
                .errorMessage(errorMessage)
                .build();
 
        emailLogRepository.save(emailLog);
    }
}
