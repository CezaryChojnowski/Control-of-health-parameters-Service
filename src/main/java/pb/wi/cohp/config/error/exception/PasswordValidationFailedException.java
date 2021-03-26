package pb.wi.cohp.config.error.exception;

public class PasswordValidationFailedException extends RuntimeException{
    public PasswordValidationFailedException(String message) {
        super(message);
    }
}
