package studio.bluekitten.backtestingcat.exceptions;

public class DifferDimensionException extends RuntimeException{
    public DifferDimensionException(){
        super();
    }
    public DifferDimensionException(String message){
        super(message);
    }
    public DifferDimensionException(String message, Throwable cause){
        super(message, cause);
    }
    public DifferDimensionException(Throwable cause){
        super(cause);
    }
}
