package Model.Machine;

import Model.EquivalentType;
import Model.Exceptions.MachineLoadException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class Machine {

    private HashMap<Integer, State> conversionTable = null;
    private HashMap<Integer, Integer> reflectionMap = null;

    public Machine(HashMap<Integer, State> conversionTable) {
        this.conversionTable = conversionTable;
    }

    private int inputWords = -1;
    private int possibleStates = -1;


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

    private HashMap<Integer, Integer> directMap = null;
    private HashMap<Integer, Integer> reverseMap = null;


    private void BuildReflections(){
        directMap = new HashMap<>();
        reverseMap = new HashMap<>();
        int key = 0;
        for(Integer i : conversionTable.keySet())
        {
            directMap.put(i, key);
            reverseMap.put(key, i);
            key++;
        }
    }

    public Machine getCodedMachine(HashMap<Integer, Integer> map){
        HashMap<Integer, State> newConversionTable = new HashMap<>();

        for(Map.Entry<Integer, State> entry : conversionTable.entrySet())
            newConversionTable.put(map.get(entry.getKey()), entry.getValue().getCodedState(map));

        Machine m = new Machine(newConversionTable);
        int minKey = newConversionTable.keySet().iterator().next();
        m.inputWords = newConversionTable.get(minKey).getTransitionList().size();
        m.possibleStates = newConversionTable.size();

        return m;
    }

    public Machine getCodedMachine(){
        if (directMap == null)
            BuildReflections();
        return getCodedMachine(directMap);
    }

}