package studio.bluekitten.backtestingcat.exceptions;

public class NotBooleanPresentationException extends RuntimeException{
    public NotBooleanPresentationException(){
        super();
    }
    public NotBooleanPresentationException(String message){
        super(message);
    }
    public NotBooleanPresentationException(String message, Throwable cause){
        super(message, cause);
    }
    public NotBooleanPresentationException(Throwable cause){
        super(cause);
    }
}
