package Model.Exceptions;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class MachineRuntimeException extends RuntimeException {

    public MachineRuntimeException() {
    }

    public MachineRuntimeException(String message) {
        super(message);
    }
}

