package Model.Machine;

import Model.Exceptions.MachineRuntimeException;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class MachineEngine {

    private Machine machine;
    private int state;

    public MachineEngine(Machine machine, int state) {
        this.machine = machine;
        this.state = state;
    }


    public int getNextState(int inputValue){
        State state = machine.getConversionTable().get(this.state);
        Transition t = state.getTransitionList().get(inputValue);
        return t.getNextTransition();
    }

    public int changeState(int inputValue) {
        State state = machine.getConversionTable().get(this.state);
        Transition t = state.getTransitionList().get(inputValue);
        this.state = t.getNextTransition();
        return t.getOutput();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
