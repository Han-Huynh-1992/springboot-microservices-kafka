package hanhuynh.com.orderservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {

	private static final String TOPIC_EMAIL    = "email-topic";
    private static final String TOPIC_SHIPMENT = "shipment-topic";
    
	@Bean
	public NewTopic getEmail() {
		// Create topic for emailService
		return new NewTopic(TOPIC_EMAIL, 1, (short) 1);
	}
	
	@Bean
	public NewTopic getShipment() {
		// Create topic for shipmentService
		return new NewTopic(TOPIC_SHIPMENT, 1, (short) 1);
	}
	
	@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
