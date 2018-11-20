package Model.Exceptions;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class DifferentSizesException extends RuntimeException {

    public DifferentSizesException() {
    }

    public DifferentSizesException(String message) {
        super(message);
    }
}
