package Lab4.Exceptions;

public class TooShortNameException extends Exception{
    public TooShortNameException(String message){
        super(message);
    }
}
