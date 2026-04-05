package hanhuynh.com.orderservice.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hanhuynh.com.orderservice.entity.OutboxEvent;
import hanhuynh.com.orderservice.enums.OutboxStatus;
import hanhuynh.com.orderservice.event.OrderCreatedEvent;
import hanhuynh.com.orderservice.repository.OutboxRepository;
import hanhuynh.com.orderservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

	@Autowired
    private KafkaTemplate<String, Object> producer;
	
	@Autowired
	private OutboxRepository outboxRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
    
    @Autowired
	private MessageUtil messageUtil;
 
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxRepository.findByStatus(OutboxStatus.PENDING);
 
        if (pendingEvents.isEmpty()) return;
 
        log.info(messageUtil.get("outbox.log.processing"));
 
        for (OutboxEvent outboxEvent : pendingEvents) {
            try {
            	// Deserialize JSON String to OrderCreatedEvent object
                OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(
                		outboxEvent.getPayload(),
                        OrderCreatedEvent.class
                );
 
                // Publish OrderCreatedEvent object on Kafka
                producer.send(outboxEvent.getTopic(), outboxEvent.getMessageKey(), orderCreatedEvent);
 
                outboxEvent.setStatus(OutboxStatus.PUBLISHED);
                outboxRepository.save(outboxEvent);
 
                log.info(messageUtil.get("outbox.log.published", outboxEvent.getTopic(), outboxEvent.getMessageKey()));
            } catch (Exception ex) {
                log.error(messageUtil.get("outbox.log.failed", outboxEvent.getTopic(), outboxEvent.getMessageKey(), ex.getMessage()));
            }
        }
    }
}
