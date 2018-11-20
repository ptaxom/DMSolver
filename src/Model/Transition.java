package Model;

import Model.Exceptions.MachineLoadException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class Transition {

    private int nextTransition;
    private int output;

    public Transition(int nextTransition, int output) {
        this.nextTransition = nextTransition;
        this.output = output;
    }

    public Transition(String s){
        parseString(s);
    }

    public int getNextTransition() {
        return nextTransition;
    }

    public int getOutput() {
        return output;
    }

    public EquivalentType compare(Transition other)
    {
        if (other != null)
        {
            if (this.output != other.output)
                return EquivalentType.CLEARLY_NOT;
            if (this.nextTransition == other.nextTransition)
                return EquivalentType.CLEARLY;
        }
        return EquivalentType.UNDEFINED;
    }


    private  boolean isCorrectString(String s){
        Pattern p = Pattern.compile("\\d{1,}\\;\\d{1,}");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    private void parseString(String s){
        if (!isCorrectString(s))
            throw new MachineLoadException("Illigal transition: " + s);
        String[] args = s.split(";");
        this.nextTransition = Integer.parseInt(args[0]);
        this.output = Integer.parseInt(args[1]);
    }

    @Override
    public String toString() {
        return nextTransition + ";" + output;
    }
}
