package hanhuynh.com.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

	private int errorCode;
    private String message;
}
