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

    public Object getLeft() {
        return left;
    }

    public void setLeft(Object left) {
        this.left = left;
    }

    public Object getRight() {
        return right;
    }

    public void setRight(Object right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "PP:["+left.toString()+" "+right.toString()+"]";        
    }
    
}
