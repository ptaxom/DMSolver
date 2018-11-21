package Minimizator;

import Model.EquivalentType;
import Model.Machine.Machine;
import Model.Machine.MachineEngine;
import javafx.util.Pair;

import javax.crypto.Mac;
import java.util.HashMap;

/**
 * Created by ptaxom on 20.11.2018.
 */
public class Minimizator {

    private Machine m;

    public Minimizator(Machine m) {
        this.m = m;
    }

    private EquivalentType[][] matrix = null;

    public Machine Minimize()
    {
        matrix = m.getEquivalentMatrix();

        for(int i = 0; i < m.getPossibleStates(); i++)
            for(int j = i + 1; j < m.getPossibleStates(); j++)
                if (matrix[i][j] == EquivalentType.UNDEFINED)
                {
                    MachineEngine m1 = new MachineEngine(m,i);
                    MachineEngine m2 = new MachineEngine(m,j);
                    StateDefiner stateDefiner = new StateDefiner(m1,m2);
                    DefineStates(i,j,
                            stateDefiner.DefineDoubleState(stateDefiner.DefineState(i,j)));
                }

        MachineStateEncoder encoder = new MachineStateEncoder(matrix);
        return m.getCodedMachine(encoder.Encode());
    }

    private void printMatrix(){
        for(int i = 0; i < m.getPossibleStates(); i++) {
            for (int j = 0; j < m.getPossibleStates(); j++)
                System.out.printf("%.1f ", matrix[i][j].getType());
            System.out.println();
        }
    }





    private void DefineStates(int x, int y, EquivalentType type){
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
                {
                    m1.changeState(i); m2.changeState(i);
                    curType = DefineState(m1.getState(), m2.getState());
                }
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


    class MachineStateEncoder {

        private boolean[] visited;
        private EquivalentType[][] matrix;
        HashMap<Integer, Integer> directMap = new HashMap<>();

        public MachineStateEncoder(EquivalentType[][] matrix) {
            this.matrix = matrix;
            visited = new boolean[matrix.length];
        }

        void vertexDFS(int vertex, int type)
        {
            visited[vertex] = true;
            directMap.put(vertex,type);
            for(int i = 0; i < matrix.length; i++)
                if (!visited[i] && (matrix[i][vertex] == EquivalentType.CLEARLY || matrix[vertex][i] == EquivalentType.CLEARLY))
                    vertexDFS(i, type);
        }


        public HashMap<Integer, Integer> Encode(){
            int type = 0;
            for(int i = 0; i < matrix.length; i++)
                if (!visited[i])
                    vertexDFS(i,type++);
            return directMap;
        }
    }


}
