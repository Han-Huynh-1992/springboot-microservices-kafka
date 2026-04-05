package hanhuynh.com.emailservice.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanhuynh.com.emailservice.entity.EmailLog;
import hanhuynh.com.emailservice.enums.EmailStatus;
import hanhuynh.com.emailservice.event.OrderCreatedEvent;
import hanhuynh.com.emailservice.repository.EmailLogRepository;
import hanhuynh.com.emailservice.util.MessageUtil;
import lombok.Data;

@Service
@Data
public class EmailLogServiceImpl implements EmailLogService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    private EmailLogRepository emailLogRepository;
	
	@Autowired
	private MessageUtil messageUtil;

    @Transactional
    @Override
    public EmailLog createOrGetEmailLog(OrderCreatedEvent event, String subject) {
    	 EmailLog emailLog = emailLogRepository.findByOrderId(event.getOrderId());
    	 
         if (emailLog == null) {
         	emailLog = emailLogRepository.save(EmailLog.builder()
                     .emailId(UUID.randomUUID().toString())
                     .orderId(event.getOrderId())
                     .recipientEmail(event.getCustomerEmail())
                     .subject(subject)
                     .body("")
                     .status(EmailStatus.FAILED)
                     .build());
         }
        
        return emailLog;
    }
    
    @Override
    public boolean existsByOrderId(String orderId) {
    	boolean isExisted = false;
    	
    	EmailLog emailLog = emailLogRepository.findByOrderId(orderId);
    	if (emailLog != null) {
    		isExisted = true;
    	}
    	
    	return isExisted;
    }

    @Transactional
    @Override
    public void markAsSent(EmailLog emailLog, String body) {
        emailLog.setStatus(EmailStatus.SENT);
        emailLog.setBody(body);
        emailLog.setErrorMessage(null);
        emailLogRepository.save(emailLog);
    }

    @Transactional
    @Override
    public void markAsFailed(EmailLog emailLog, String error) {
        emailLog.setStatus(EmailStatus.FAILED);
        emailLog.setErrorMessage(error);
        emailLogRepository.save(emailLog);
    }
}
