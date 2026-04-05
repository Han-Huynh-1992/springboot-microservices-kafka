package hanhuynh.com.orderservice.exception;

import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import hanhuynh.com.orderservice.util.MessageUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageUtil messageUtil;

	/**
     * @Valid fail in fields
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse(ex.getLocalizedMessage());
 
        logger.warn(messageUtil.get("order.error.invalidFields", request.getDescription(false), message));
        return new ErrorMessage(10001, message);
    }
 
    /**
     * Duplicate key, constraint violation in DB
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleDataIntegrityException(DataIntegrityViolationException ex, WebRequest request) {
    	logger.error(messageUtil.get("order.error.invalidKeyAndConstraint", request.getDescription(false)), ex);
        return new ErrorMessage(10002, ex.getLocalizedMessage());
    }
 
    /**
     * DB connection failed, timeout, query error
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorMessage handleDataAccessException(DataAccessException ex, WebRequest request) {
    	logger.error(messageUtil.get("order.error.dbConnection", request.getDescription(false)), ex);
        return new ErrorMessage(10003, ex.getLocalizedMessage());
    }
 
    /**
     * Kafka broker unavailable, send event failed
     */
    @ExceptionHandler(KafkaException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorMessage handleKafkaException(KafkaException ex, WebRequest request) {
    	logger.error(messageUtil.get("order.error.kafkaBroker", request.getDescription(false)), ex);
        return new ErrorMessage(10004, ex.getLocalizedMessage());
    }
 
    /**
     * Other exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleAllException(Exception ex, WebRequest request) {
    	logger.error(messageUtil.get("order.error.otherException", request.getDescription(false)), ex);
        return new ErrorMessage(10000, ex.getLocalizedMessage());
    }
}
