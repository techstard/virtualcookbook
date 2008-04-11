
package virtualgusteau;

import java.util.*;

/**
 *
 * @author rkrantz
 */
public class Model extends Observable {
    
    private String input;
    private String output;
    
    /**
     * 
     * @return The system response to a user input
     */
    public String getOutput() {
        return output;
    }
    /**
     * 
     * @return What the user inputed into the system
     */
    public String getInput() {
        return input;
    }    
    /**
     * 
     * @param arg The user input
     */
    public void parse(String arg) {
        
        input = arg;
        
        if(arg.contains("hello")) {
            output = "Bonjour";
        } else {
            output = "qui êtes-vous ?";
        }
        
        setChanged();
        notifyObservers();
    }
}
