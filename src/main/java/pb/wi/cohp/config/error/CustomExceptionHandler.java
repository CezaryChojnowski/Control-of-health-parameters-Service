package pb.wi.cohp.config.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pb.wi.cohp.config.error.exception.PasswordValidationFailedException;
import pb.wi.cohp.config.error.exception.UserExistsException;
import pb.wi.cohp.config.error.response.ConstraintViolationResponse;
import pb.wi.cohp.config.error.response.PasswordValidationFailedResponse;
import pb.wi.cohp.config.error.response.UserExistsResponse;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
@ControllerAdvice
@PropertySource("classpath:messages.properties")
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${registrationFailed}")
    private String registrationFailed;

    @Value("${validationFailed}")
    private String validationFailed;

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Object> handleUsernameIsAlreadyTakenException(UserExistsException exception){
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        UserExistsResponse error = new UserExistsResponse(registrationFailed, details, HttpStatus.CONFLICT.value());
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordValidationFailedException.class)
    public ResponseEntity<Object> handleUsernameIsAlreadyTakenException(PasswordValidationFailedException exception){
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        PasswordValidationFailedResponse error = new PasswordValidationFailedResponse(validationFailed, details, HttpStatus.CONFLICT.value());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity handleConstraintViolation(
            ConstraintViolationException ex) {
        List<String> details = new ArrayList<>();
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            details.add(error.getMessage());
        }
        ConstraintViolationResponse constraintViolationResponse =
                new ConstraintViolationResponse(validationFailed ,details, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<Object>(constraintViolationResponse, HttpStatus.BAD_REQUEST);
    }
}
