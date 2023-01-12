package itacad.aliaksandrkryvapust.usermicroservice.controller.exceptions;

public class ExpiredEmailTokenException extends RuntimeException{
    public ExpiredEmailTokenException (String errorMessage){
        super(errorMessage);
    }
}
