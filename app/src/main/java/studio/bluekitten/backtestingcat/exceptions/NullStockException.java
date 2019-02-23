package studio.bluekitten.backtestingcat.exceptions;

/**
 * Created by sclab on 9/20/2016.
 */
public class NullStockException  extends RuntimeException{

    public NullStockException(){
        super();
    }
    public NullStockException(String message){
        super(message);
    }
    public NullStockException(String message, Throwable cause){
        super(message, cause);
    }
    public NullStockException(Throwable cause){
        super(cause);
    }
}
