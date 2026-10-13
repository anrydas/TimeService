package das.tools.timeservice.exception;

import das.tools.timeservice.entity.AppResponse;
import das.tools.timeservice.entity.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<AppResponse> handleInvalidRequest(InvalidRequestException e) {
        return badRequest(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<AppResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return badRequest("Invalid value '" + e.getValue() + "' of parameter '" + e.getName() + "'");
    }

    private ResponseEntity<AppResponse> badRequest(String message) {
        log.warn("Bad request: {}", message);
        AppResponse res = AppResponse.builder()
                .status(ResponseStatus.ERROR)
                .message("Error: " + message)
                .build();
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
