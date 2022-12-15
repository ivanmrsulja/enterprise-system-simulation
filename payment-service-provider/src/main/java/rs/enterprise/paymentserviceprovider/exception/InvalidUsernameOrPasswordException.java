package rs.enterprise.paymentserviceprovider.exception;

public class InvalidUsernameOrPasswordException extends RuntimeException {

    public InvalidUsernameOrPasswordException(String message) {
        super(message);
    }
}
