package hanhuynh.com.orderservice.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

	@Autowired
	private MessageSource messageSource;

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}
