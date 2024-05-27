package das.tools.timeservice.exception;

abstract class ApplicationException extends Exception {
    public ApplicationException(String message) {
        super(message);
    }
}
