package das.tools.timeservice.exception;

import das.tools.timeservice.entity.AppResponse;
import das.tools.timeservice.entity.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
    private ResponseEntity<AppResponse> handle(ApplicationException e) {
        AppResponse res = AppResponse.builder()
                .status(ResponseStatus.ERROR)
                .message("Error: " + e.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
