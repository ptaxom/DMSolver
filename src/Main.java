import Minimizator.Minimizator;
import Model.Machine.Machine;

import java.io.File;
import java.util.HashMap;


/**
 * Created by ptaxom on 20.11.2018.
 */

public class Main {


    public static void main(String[] args) {
        Machine m = new Machine();
        m.loadFromFile(new File("res\\machine.txt"));
//        System.out.println(m);

        Machine codedMachine = m.getCodedMachine();

        Minimizator minimizator = new Minimizator(codedMachine);

        HashMap<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < 5; i++)
            map.put(i,i+1);

        Machine minimized = minimizator.Minimize();


        System.out.println(minimized.getCodedMachine(map));



    }


    public int hashCode(){
        return 0;
    }
}
