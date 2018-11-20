import Model.Machine;

import java.io.File;


/**
 * Created by ptaxom on 20.11.2018.
 */

public class Main {


    public static void main(String[] args) {
        Machine m = new Machine();
        m.loadFromFile(new File("res\\machine.txt"));
        System.out.println(m);
        m.setCurrentState(1);

      
    }

}
