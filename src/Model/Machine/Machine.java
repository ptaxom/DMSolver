package Model.Machine;

import Model.EquivalentType;
import Model.Exceptions.MachineLoadException;
import Model.Exceptions.MachineRuntimeException;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class Machine {

    private HashMap<Integer, State> conversionTable = null;

    public Machine(HashMap<Integer, State> conversionTable) {
        this.conversionTable = conversionTable;
    }

    private int inputWords;
    private int possibleStates;


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

        assertMachine();
    }

    private void assertMachine()
    {
        Set<Integer> states = conversionTable.keySet();
        int size = -1;
        for(Integer i : states)
        {
            State state = conversionTable.get(i);
            if (size == -1)
                size = state.getTransitionList().size();
            if (state.getTransitionList().size() != size)
                throw new MachineLoadException("Incorrect machine!(Differences sizes in state " + i+")");
            for(Transition t : state.getTransitionList())
                if (!states.contains(t.getNextTransition()))
                    throw new MachineLoadException("Incorrect machine!(Illegal next state in state " + i+" with next state "
                            +t.getNextTransition()+")");
        }
        inputWords = size;
        possibleStates = conversionTable.size();
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

    public HashMap<Integer, State> getConversionTable() {
        return conversionTable;
    }

    public int getInputWords() {
        return inputWords;
    }

    public int getPossibleStates() {
        return possibleStates;
    }

    public EquivalentType[][] getEquivalentMatrix(){
        int size = possibleStates;
        EquivalentType[][] matrix = new EquivalentType[size][size];
        for(int i : conversionTable.keySet())
            for(int j : conversionTable.keySet())
                if (i != j)
                {
                    State s1 = conversionTable.get(i);
                    State s2 = conversionTable.get(j);
                    EquivalentType type = s1.compare(s2);
                    matrix[i][j] = type;
                    matrix[j][i] = type;
                }
                else matrix[i][j] = EquivalentType.UNDEFINED;
        return matrix;
    }
}