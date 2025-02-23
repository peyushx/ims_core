package rapifuzz.com.ims.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{

    private  HttpStatus httpStatus;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
