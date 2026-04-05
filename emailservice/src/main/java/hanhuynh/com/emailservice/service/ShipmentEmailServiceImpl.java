package hanhuynh.com.emailservice.service;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import hanhuynh.com.emailservice.event.ShipmentCreatedEvent;
import hanhuynh.com.emailservice.util.MessageUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;

@Service
@Data
public class ShipmentEmailServiceImpl implements ShipmentEmailService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private SpringTemplateEngine template;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Value("${spring.mail.username}")
	private String from;
	
	@Override
    public void sendShipmentNotification(ShipmentCreatedEvent event) {
		logger.info(messageUtil.get("shipment.email.log.processing", event.getOrderId()));
 
        String subject = messageUtil.get("shipment.email.subject", event.getOrderNumber());
 
        // Build email body by Thymeleaf template
        Context context = new Context();
        context.setVariable("customerName", event.getCustomerName());
        context.setVariable("orderNumber", event.getOrderNumber());
        context.setVariable("shipmentId", event.getShipmentId());
        context.setVariable("shippingAddress", event.getShippingAddress());
        context.setVariable("estimatedDeliveryDate", event.getEstimatedDeliveryDate());
        String body = template.process("shipment-notification", context);
 
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
 
            helper.setFrom(from);
            helper.setTo(event.getCustomerEmail());
            helper.setSubject(subject);
            helper.setText(body, true);
 
            javaMailSender.send(message);
            logger.info(messageUtil.get("shipment.email.log.sent", event.getCustomerEmail(), event.getOrderId()));
        } catch (Exception ex) {
        	logger.error(messageUtil.get("shipment.email.log.failed", event.getCustomerEmail(), event.getOrderId(), ex.getMessage()));
        	
        	// Throw exception that retry mechanism can catch error
            throw new RuntimeException(ex);
        }
    }
 
}
