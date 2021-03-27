package pb.wi.cohp.config.error.exception;

public class UserDoesNotHaveActiveAccountException extends RuntimeException{
    public UserDoesNotHaveActiveAccountException(String message) {
        super(message);
    }
}