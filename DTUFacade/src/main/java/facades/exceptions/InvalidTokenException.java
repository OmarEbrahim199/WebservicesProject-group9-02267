package facades.exceptions;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String cause) {
        super(cause);
    }
    
}
