package rapifuzz.com.ims.model.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Represents a standardized response structure for ImsCore.
 *
 * @param <T> The type of the data field that is being included in the response.
 */
public record ImsResponse<T>(
        Integer code,
        boolean success,
        String message,
        T data
) {

    public ImsResponse(Object code, String message, int httpStatusValue) {
        this((Integer) code, false, message, null);
    }

    /**
     * Creates a successful ResponseEntity with the given message and data.
     *
     * @param message The success message.
     * @param data    The data to be included in the response.
     * @param <T>     The type of the data.
     * @return A ResponseEntity representing a successful operation.
     */
    public static <T> ResponseEntity<ImsResponse> success(String message, T data) {
        ImsResponse<T> response = new ImsResponse<>(HttpStatus.OK.value(), true, message, data);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a successful ResponseEntity with the given message and data.
     *
     * @param message The success message.
     * @param data    The data to be included in the response.
     * @param <T>     The type of the data.
     * @return A ResponseEntity representing a successful operation.
     */
    public static <T> ResponseEntity<ImsResponse> success(String message, T data,Boolean success) {
        ImsResponse<T> response = new ImsResponse<>(HttpStatus.OK.value(), success, message, data);
        return ResponseEntity.ok(response);
    }



    /**
     * Creates a successful ResponseEntity with the given message and data.
     *
     * @param message The success message.
     * @param data    The data to be included in the response.
     * @param <T>     The type of the data.
     * @return A ResponseEntity representing a successful operation.
     */
    public static <T> ResponseEntity<ImsResponse> handleError(String message, T data) {
        ImsResponse<T> response = new ImsResponse<>(HttpStatus.ACCEPTED.value(), true, message, data);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    /**
     * Creates a successful ResponseEntity with only a message.
     *
     * @param message The success message.
     * @return A ResponseEntity representing a successful operation with no data.
     */
    public static ResponseEntity<ImsResponse> success(String message) {
        return success(message, null);
    }

    /**
     * Creates a failed ResponseEntity with the given status, message, and data.
     *
     * @param status  The HTTP status representing the error.
     * @param message The error message.
     * @param data    The data to be included in the response.
     * @param <T>     The type of the data.
     * @return A ResponseEntity representing a failed operation.
     */
    public static <T> ResponseEntity<ImsResponse> failed(HttpStatus status, String message, T data) {
        ImsResponse<T> response = new ImsResponse<>(status.value(), false, message, data);
        return new ResponseEntity<>(response, status);
    }

    /**
     * Creates a failed ResponseEntity with the given status and message, without data.
     *
     * @param status  The HTTP status representing the error.
     * @param message The error message.
     * @return A ResponseEntity representing a failed operation with no data.
     */
    public static ResponseEntity<ImsResponse> failed(HttpStatus status, String message) {
        return failed(status, message, null);
    }
}
