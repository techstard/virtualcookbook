
package virtualgusteau;

import java.util.*;

/**
 *
 * @author rkrantz
 */
public class Model extends Observable {
    
    private String ta = "";
    private String tf = "";
    
    
    public String getArea() {
        return ta;
    }
    public String getField() {
        return tf;
    }
    
    public void parse(String arg) {
        
        ta = arg;
        tf = "";
        
        setChanged();
        notifyObservers();
    }
}
