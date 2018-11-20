package Minimizator;

import Model.EquivalentType;
import Model.Machine.Machine;
import Model.Machine.MachineEngine;
import javafx.util.Pair;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class Minimizator {

    private Machine m;

    public Minimizator(Machine m) {
        this.m = m;
    }

    private EquivalentType[][] matrix = null;

    public void Minimize()
    {
        matrix = m.getEquivalentMatrix();

        printMatrix();

//        for(int i = 0; i < m.getPossibleStates(); i++)
//            for(int j = i + 1; j < m.getPossibleStates(); j++)
//                if (matrix[i][j] == EquivalentType.UNDEFINED)
//                {
//                    MachineEngine m1 = new MachineEngine(m,i);
//                    MachineEngine m2 = new MachineEngine(m,j);
//                    StateDefiner stateDefiner = new StateDefiner(m1,m2);
//                    DefineStates(i,j,
//                            stateDefiner.DefineDoubleState(stateDefiner.DefineState(i,j)));
//                }

    }

    public void printMatrix(){
        for(int i = 0; i < m.getPossibleStates(); i++) {
            for (int j = 0; j < m.getPossibleStates(); j++)
                System.out.printf("%.2f ", matrix[i][j].getType());
            System.out.println();
        }
    }


    public void DefineStates(int x, int y, EquivalentType type){
        matrix[x][y] = type;
        matrix[y][x] = type;
    }

    class StateDefiner{

        private MachineEngine m1;
        private MachineEngine m2;

        private boolean[][] visited;

        public StateDefiner(MachineEngine m1, MachineEngine m2) {
            this.m1 = m1;
            this.m2 = m2;
            visited = new boolean[m.getPossibleStates()][m.getPossibleStates()];
        }

        public double DefineState(int x, int y){
            if (matrix[x][y] == EquivalentType.CLEARLY_NOT) {
                DefineStates(x,y, EquivalentType.CLEARLY_NOT);
                return EquivalentType.CLEARLY_NOT.getType();
            }
            if (visited[x][y] || visited[y][x])
                return EquivalentType.UNDEFINED.getType();

            visited[x][y] = true; visited[y][x] = true;
            for(int i = 0; i < m.getInputWords(); i++)
            {
                Pair<Integer, Integer> prevStates = new Pair<>(m1.getState(), m2.getState());
                double curType = 1.0;
                if (!visited[m1.getNextState(i)][m2.getNextState(i)])
                    curType = DefineState(m1.changeState(i),m2.changeState(i));
                if (curType == 0)
                    return 0.0;
                m1.setState(prevStates.getKey());
                m2.setState(prevStates.getValue());

            }
            return 0.5;
        }

        public EquivalentType DefineDoubleState(double val)
        {
            if (val == 0.0)
                return EquivalentType.CLEARLY_NOT;
            else return EquivalentType.CLEARLY;
        }
    }

}
