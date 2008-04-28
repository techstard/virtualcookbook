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
    //LinkedList<Object> PP = new LinkedList<Object>();
    private Object left;
    private Object right;
    
    public PP(P p, NP np) {
        //PP.add(p);
        //PP.add(np);
        left = p;
        right = np;
    }

    @Override
    public String toString() {
        return "PP:["+left.toString()+" "+right.toString()+"]";        
    }
    
}
