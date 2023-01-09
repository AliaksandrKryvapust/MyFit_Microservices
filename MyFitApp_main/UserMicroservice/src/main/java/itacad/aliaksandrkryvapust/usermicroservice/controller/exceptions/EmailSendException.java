package itacad.aliaksandrkryvapust.usermicroservice.controller.exceptions;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
