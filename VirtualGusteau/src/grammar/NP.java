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
    LinkedList<Object> NP = new LinkedList<Object>();
    /**
     * 
     * @param pn Noun Phrase consists of a Pronoun
     */
    public NP(PN pn) {
        NP.add(pn);
    }
    /**
     * 
     * @param n Noun Phrase consists of a single noun
     */
    public NP(N n) {
        NP.add(n);
    }
    /**
     * 
     * @param a The Article of the Noun Phrase
     * @param n The Noun
     */
    public NP(A a, N n) {
        NP.add(a);
        NP.add(n);
    }
    /**
     * 
     * @param np A Noun Phrase
     * @param pp A Prepositional Phrase
     */
    public NP(NP np, PP pp) {
        NP.add(np);
        NP.add(pp);
    }
    @Override
    public String toString() {
        String tmp = "NP:[";
        for(int i = 0; i < NP.size(); i++) {
            
            if(i != NP.size()-1) {
                tmp += NP.get(i).toString() + " ";
            } else {
                tmp += NP.get(i).toString();
            }
        }
        tmp += "]";
        return tmp;
    }
    
}
