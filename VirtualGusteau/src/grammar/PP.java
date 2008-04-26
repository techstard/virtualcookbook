/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package grammar;
import java.util.*;
/**
 *
 * @author rkrantz
 */
public class PP {
    LinkedList<Object> PP = new LinkedList<Object>();
    
    public PP(P p, NP np) {
        PP.add(p);
        PP.add(np);
    }

    @Override
    public String toString() {
        return "PP:["+PP.getFirst().toString()+" "+PP.getLast().toString()+"]";        
    }
    
}
