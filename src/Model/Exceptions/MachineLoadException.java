package Model.Exceptions;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class MachineLoadException extends RuntimeException{
    public MachineLoadException() {
    }

    public MachineLoadException(String message) {
        super(message);
    }
}
