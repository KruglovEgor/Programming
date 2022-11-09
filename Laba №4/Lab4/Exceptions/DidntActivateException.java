package Lab4.Exceptions;

public class DidntActivateException extends RuntimeException{
    public DidntActivateException(String message){
        super(message);
    }
}
