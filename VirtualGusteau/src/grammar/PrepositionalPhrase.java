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
public class PrepositionalPhrase {
    //LinkedList<Object> PrepositionalPhrase = new LinkedList<Object>();
    private Object left;
    private Object right;
    
    public PrepositionalPhrase(Preposition p, NounPhrase np) {
        //PrepositionalPhrase.add(p);
        //PrepositionalPhrase.add(np);
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
