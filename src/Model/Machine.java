package Model;

import Model.Exceptions.MachineLoadException;
import Model.Exceptions.MachineRuntimeException;

import java.io.*;
import java.util.HashMap;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class Machine {

    private HashMap<Integer, State> conversionTable = null;

    public Machine(HashMap<Integer, State> conversionTable) {
        this.conversionTable = conversionTable;
    }

    public Machine() {
    }




    public void loadFromFile(File file) {
        conversionTable = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String readedLine = "";


            while ((readedLine = reader.readLine()) != null) {
                String[] args = readedLine.split(":\\s*");
                if (args.length != 2)
                    throw new MachineLoadException("Illegal machine storage format");
                int id = Integer.parseInt(args[0]);
                State state = new State(args[1]);
                conversionTable.put(id,state);
            }

        } catch (FileNotFoundException e) {
            throw new MachineLoadException("File not founded!!");
        } catch (IOException e) {
            throw new MachineLoadException("File corrupted");
        } catch (MachineLoadException ex) {
            throw ex;
        }

    }

    @Override
    public String toString() {
        String out = "";
        for(int key : conversionTable.keySet())
            out += key + ": " + conversionTable.get(key) + "\n";
        return out;
    }

    private int currentState = 0;

    public int getCurrentState() {
        return currentState;
    }

    private void assertStateValue(int state){
        if (!conversionTable.keySet().contains(state))
            throw  new MachineRuntimeException("Unknown machine state");
    }

    public void setCurrentState(int currentState) {
        assertStateValue(currentState);
        this.currentState = currentState;
    }

    public int changeState(int inputValue) {
        assertStateValue(currentState);

        int output = -1;

        State state = conversionTable.get(currentState);
        try {
            Transition t = state.getTransitionList().get(inputValue);
            int nextState = t.getNextTransition();
            assertStateValue(nextState);
            output = t.getOutput();
            currentState = nextState;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new MachineRuntimeException("Illigal keyword");
        }
        catch (MachineRuntimeException ex) {
            throw ex;
        }

        if (output == -1)
            throw new MachineRuntimeException("Illigal state");
        return output;
    }

}