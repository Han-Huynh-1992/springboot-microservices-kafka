package hanhuynh.com.emailservice.service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

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

import hanhuynh.com.emailservice.entity.EmailLog;
import hanhuynh.com.emailservice.enums.EmailStatus;
import hanhuynh.com.emailservice.event.OrderCreatedEvent;
import hanhuynh.com.emailservice.repository.EmailLogRepository;
import hanhuynh.com.emailservice.util.MessageUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;

@Service
@Data
public class EmailServiceImpl implements EmailService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private SpringTemplateEngine template;
	
	@Autowired
    private EmailLogService emailLogService;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Value("${spring.mail.username}")
	private String from;
	
	@Override
    @Transactional
    public void sendOrderConfirmation(OrderCreatedEvent event) {
		logger.info(messageUtil.get("email.log.processing", event.getOrderId()));
		
		String subject = String.format(messageUtil.get("email.subject", event.getOrderNumber()));
		String body = buildEmailBody(event);
		
		EmailLog emailLog;
		
		try {
			emailLog = emailLogService.createOrGetEmailLog(event, subject);
			logger.info(messageUtil.get("email.log.createdEmailLog", event.getOrderId()));
		} catch (Exception ex) {
		    logger.error(messageUtil.get("email.log.createdEmailLog.failed", event.getOrderId()), ex);
		    throw new RuntimeException(ex);
		}
 
        // Email is sent already, then return
        if (emailLog != null && emailLog.getStatus() == EmailStatus.SENT) {
        	logger.info(messageUtil.get("email.log.already.sent", event.getOrderId()));
            return;
        }
        
		// Send email and update status
		try {
			sendEmail(event.getCustomerEmail(), subject, body);
			
			// Update status
			emailLogService.markAsSent(emailLog, body);
			
			logger.info(messageUtil.get("email.log.sent", event.getCustomerEmail(), event.getOrderId()));
		} catch (Exception ex) {
			logger.error(messageUtil.get("email.log.failed",  event.getCustomerEmail(), event.getOrderId(), ex.getMessage()));
            
			// Update error message
			emailLogService.markAsFailed(emailLog, ex.getMessage());
            
            // Throw exception that retry mechanism can catch error
            throw new RuntimeException(ex);
		}
    }
	
	private String buildEmailBody(OrderCreatedEvent event) {
        Context context = new Context();
        context.setVariable("customerName", event.getCustomerName());
        context.setVariable("orderId", event.getOrderId());
        context.setVariable("orderNumber", event.getOrderNumber());
        context.setVariable("totalAmount", event.getTotalAmount());
        context.setVariable("items", event.getItems());
        return template.process("order-confirmation", context);
    }
	
	private void sendEmail(String to, String subject, String body) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );
        
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body, true);
		javaMailSender.send(message);
	}
 
}
