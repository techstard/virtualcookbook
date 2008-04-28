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
public class NP {    
    //LinkedList<Object> NP = new LinkedList<Object>();
    private Object left;
    private Object right;
    /**
     * 
     * @param pn Noun Phrase consists of a Pronoun
     */
    public NP(PN pn) {
        //NP.add(pn);
        left = pn;
    }
    /**
     * 
     * @param n Noun Phrase consists of a single noun
     */
    public NP(N n) {
        //NP.add(n);
        left = n;
    }
    /**
     * 
     * @param cd Noun Phrase consists of a Digit
     */
    public NP(CD cd) {
        //NP.add(cd);
        left = cd;
    }
    /**
     * 
     * @param a The Article of the Noun Phrase
     * @param n The Noun
     */
    public NP(A a, N n) {
        //NP.add(a);
        //NP.add(n);
        left = a;
        right = n;
    }
    /**
     * 
     * @param np A Noun Phrase
     * @param pp A Prepositional Phrase
     */
    public NP(NP np, PP pp) {
        //NP.add(np);
        //NP.add(pp);
        left = np;
        right = pp;
    }
    /**
     * 
     * @param jj Adjective
     * @param n Noun
     */
    public NP(JJ jj, N n) {
        left = jj;
        right = n;
    }
    public NP(A a, NP np) {
        left = a;
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
        String tmp = "NP:[";
        /*for(int i = 0; i < NP.size(); i++) {
            
            if(i != NP.size()-1) {
                tmp += NP.get(i).toString() + " ";
            } else {
                tmp += NP.get(i).toString();
            }
        }*/
        tmp += left.toString();
        if(right != null) {
            tmp += " "+right.toString();
        }
        tmp += "]";
        return tmp;
    }
    
}
