package day27workshop.day27workshop.exception;

public class Exceptions extends RuntimeException {

    public Exceptions(){
        super();
    }
    
    
    public Exceptions(String message) {
        super(message);
    }

    
    public Exceptions(String message, Throwable cause) {
        super(message, cause);
    }

    
}
