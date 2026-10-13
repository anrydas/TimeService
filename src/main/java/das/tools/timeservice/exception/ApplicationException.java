package das.tools.timeservice.exception;

public abstract class ApplicationException extends Exception {
    public ApplicationException(String message) {
        super(message);
    }
}
