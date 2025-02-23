package rapifuzz.com.ims.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import rapifuzz.com.ims.model.response.ImsResponse;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    
    private String getStackTraceAsString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ImsResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        String message = String.format("Required parameter '%s' is not present", parameterName);
        return ImsResponse.failed(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ImsResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String error = "Invalid parameter value: " + ex.getValue();
        log.error(error);
        return ImsResponse.failed(HttpStatus.BAD_REQUEST, error);
    }

    /**
     * Handles all exceptions not captured by more specific exception handlers.
     *
     * @param exception The exception being thrown.
     * @return A ResponseEntity with a standardized error format.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ImsResponse> handleAllExceptions(Exception exception) {
        if (exception instanceof CustomException) {
            CustomException customException = (CustomException) exception;
            log.info("A custom exception occurred.", customException);
            return ImsResponse.failed(customException.getHttpStatus(), customException.getMessage());
        } else if (exception instanceof InvalidDataAccessResourceUsageException) {
            InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException = (InvalidDataAccessResourceUsageException) exception;
            log.error("An invalid data access usage exception occurred.", exception);
            return ImsResponse.failed(HttpStatus.INTERNAL_SERVER_ERROR, "Query Issue : Report to tech team",invalidDataAccessResourceUsageException.getCause().getCause().getMessage());
        } else {
            log.error("An unhandled exception occurred.", exception);
            return ImsResponse.failed(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error :: Report tech team for this issue ");
        }
    }


    /**
     * Handles AccessDeniedException
     *
     * @param ex The exception being thrown.
     *
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ImsResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("An access denied exception occurred.", ex);
        return ImsResponse.failed(HttpStatus.FORBIDDEN, "Access Denied");
    }


    /**
     * Handles valid exceptions
     *
     * @param ex The exception being thrown.
     * @return A ResponseEntity with a standardized error format.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ImsResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.info("A validation exception occurred.", ex);
        return ImsResponse.failed(HttpStatus.BAD_REQUEST, ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMultipartException(MaxUploadSizeExceededException ex) {
        log.error("A multipart exception occurred.", ex);
        return ImsResponse.failed(HttpStatus.BAD_REQUEST, "File size exceeds the limit :: 2MB");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        Throwable rootCause = ex.getMostSpecificCause();

        if (rootCause instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) rootCause;
            String detailedMessage = "Invalid Payload structure :: ";
            if (!jsonMappingException.getPath().isEmpty()) {
                JsonMappingException.Reference reference = jsonMappingException.getPath().get(jsonMappingException.getPath().size() - 1);
                String fieldName = reference.getFieldName();
                Class<?> owningClass = reference.getFrom().getClass();
                String className = owningClass.getSimpleName();
                detailedMessage += String.format("Error at field '%s' in '%s': Expected structure matching the field's type.", fieldName, className);
            }
            return ImsResponse.failed(HttpStatus.BAD_REQUEST, detailedMessage);
        } else {
            String errorMessage = "Error parsing JSON: " + (rootCause != null ? rootCause.getMessage() : "Check JSON structure and data types.");
            return ImsResponse.failed(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    /**
     * Handles Data Integrity Violation Exceptions, including constraint violations. ( SQL validations )
     *
     * @param ex The exception being thrown.
     * @return A ResponseEntity with a standardized error format.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ImsResponse> handleDataIntegrityViolationExceptions(DataIntegrityViolationException ex) {
        log.error("A data integrity violation exception occurred.", ex);
        return ImsResponse.failed(HttpStatus.BAD_REQUEST, ex.getRootCause().getMessage());
    }

    /**
     * Handle JpaObjectRetrievalFailureException globally, returning a not found status and a detailed error message
     * indicating which entity and ID were not found.
     */
    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    public ResponseEntity handleJpaObjectRetrievalFailureException(JpaObjectRetrievalFailureException ex) {
        String message = ex.getCause().getMessage();
        String entityErrorMessage = message.substring(message.indexOf("entities") + 9);
        log.error("Entity might be marked as deleted: " + entityErrorMessage);
        return ImsResponse.failed(HttpStatus.BAD_REQUEST, "Entity might be marked as deleted: " + entityErrorMessage);
    }


}

