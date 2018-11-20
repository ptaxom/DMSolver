package Model.Machine;

import Model.EquivalentType;
import Model.Exceptions.DifferentSizesException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class State {

    List<Transition> transitionList = null;

    public State(List<Transition> transitionList) {
        this.transitionList = transitionList;
    }

    public State(String s){
        parseString(s);
    }

    public List<Transition> getTransitionList() {
        return transitionList;
    }

    public EquivalentType compare(State other){
        if (other != null)
        {
            if (this.transitionList.size() != other.transitionList.size())
                throw new DifferentSizesException();

            double type = 1.0;
            for(int i = 0; i < transitionList.size(); i++)
            {
                Transition ownTransition = transitionList.get(i);
                Transition otherTransition = other.transitionList.get(i);
                type *= ownTransition.compare(otherTransition).getType();
            }

            return EquivalentType.typeFactory(type);
        }

        return EquivalentType.UNDEFINED;
    }

    private void parseString(String s){
        String[] args = s.split("\\s{1,9999}");
        transitionList = new ArrayList<>();
        for(String arg : args)
            transitionList.add(new Transition(arg));
    }

    @Override
    public String toString() {
        String out = "";
        for(Transition t : transitionList)
            out += t + " ";
        return out.trim();
    }
}
