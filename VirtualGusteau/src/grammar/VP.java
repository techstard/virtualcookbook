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
public class VP {
    
    LinkedList<Object> VP = new LinkedList<Object>();
    /**
     * 
     * @param v VP is a verb
     */
    public VP(V v) {
        VP.add(v);
    }
    /**
     * 
     * @param vp Verb Phrase
     * @param np Noun Phrase
     */
    public VP(VP vp, NP np) {
        VP.add(vp);
        VP.add(np);
    }
    /**
     * 
     * @param vp Verb Phrase
     * @param jj Adjective
     */
    public VP(VP vp, JJ jj) {
        VP.add(vp);
        VP.add(jj);
    }
    /**
     * 
     * @param vp Verb Phrase
     * @param pp Preposition Phrase
     */
    public VP(VP vp, PP pp) {
        VP.add(vp);
        VP.add(pp);
    }
    /**
     * 
     * @param vp Verb Phrase
     * @param rb Adverb
     */
    public VP(VP vp, RB rb) {
        VP.add(vp);
        VP.add(rb);
    }
    @Override
    public String toString() {
        String tmp = "VP:[";
        for(int i = 0; i < VP.size(); i++) {
            if(i != VP.size()-1) {
                tmp += VP.get(i).toString() + " ";
            } else {
                tmp += VP.get(i).toString();
            }
        }
        tmp += "]";
        return tmp;
    }
}
