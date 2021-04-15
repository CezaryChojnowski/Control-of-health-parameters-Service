package pb.wi.cohp.config.error;

import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pb.wi.cohp.config.error.exception.*;
import pb.wi.cohp.config.error.response.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
@ControllerAdvice
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:en.exception.messages.properties")
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${validationFailed}")
    private String validationFailed;

    @Value("${loginFailed}")
    private String loginFailed;

    @Value("${accessDenied}")
    private String accessDenied;

    @Value("${authenticationException}")
    private String authenticationException;

    @Value("${userNotFound}")
    private String userNotFound;

    @Value("${accountActivationFailed}")
    private String accountActivationFailed;


    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        AccessDeniedResponse error = new AccessDeniedResponse(accessDenied, details, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception) {
        List<String> details = new ArrayList<>();
        details.add(authenticationException);
        AuthenticationResponse error = new AuthenticationResponse(loginFailed, details, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDoesNotHaveActiveAccountException.class)
    public ResponseEntity<Object> handleUserDoesNotHaveActiveAccountException(UserDoesNotHaveActiveAccountException exception){
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        UserDoesNotHaveActiveAccountResponse error = new UserDoesNotHaveActiveAccountResponse(loginFailed, details, HttpStatus.CONFLICT.value());
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectTokenException.class)
    public ResponseEntity<Object> handleIncorrectTokenException(IncorrectTokenException exception){
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        IncorrectTokenResponse error = new IncorrectTokenResponse(accountActivationFailed, details, HttpStatus.CONFLICT.value());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(ObjectNotFoundException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        ObjectNotFoundResponse error = new ObjectNotFoundResponse(userNotFound, details, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordValidationFailedException.class)
    public ResponseEntity<Object> handlePasswordValidationFailedException(PasswordValidationFailedException exception){
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        PasswordValidationFailedResponse error = new PasswordValidationFailedResponse(validationFailed, details, HttpStatus.CONFLICT.value());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<Object> handleNonUniqueResultException(PasswordValidationFailedException exception){
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        PasswordValidationFailedResponse error = new PasswordValidationFailedResponse("1", details, HttpStatus.CONFLICT.value());
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

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Object> handleInvalidDataException(InvalidDataException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        InvalidDataResponse error = new InvalidDataResponse(validationFailed, details, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> details = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            details.add(error.getDefaultMessage());
        }
        MethodArgumentNotValidResponse methodArgumentNotValidResponse = new MethodArgumentNotValidResponse(validationFailed, details, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<Object>(methodArgumentNotValidResponse, HttpStatus.BAD_REQUEST);
    }
}
