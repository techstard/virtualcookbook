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
public class VerbPhrase {
    //LinkedList<Object> VerbPhrase = new LinkedList<Object>();
    private Object left;
    private Object right;
    /**
     * 
     * @param v VerbPhrase is a verb
     */
    public VerbPhrase(Verb v) {
        //VerbPhrase.add(v);
        left = v;
    }
    /**
     * 
     * @param vp Verb Phrase
     * @param np Noun Phrase
     */
    public VerbPhrase(VerbPhrase vp, NounPhrase np) {
        //VerbPhrase.add(vp);
        //VerbPhrase.add(np);
        left = vp;
        right = np;
    }
    /**
     * 
     * @param vp Verb Phrase
     * @param jj Adjective
     */
    public VerbPhrase(VerbPhrase vp, Adjective jj) {
        //VerbPhrase.add(vp);
        //VerbPhrase.add(jj);
        left = vp;
        right = jj;
    }
    /**
     * 
     * @param vp Verb Phrase
     * @param pp Preposition Phrase
     */
    public VerbPhrase(VerbPhrase vp, PrepositionalPhrase pp) {
        //VerbPhrase.add(vp);
        //VerbPhrase.add(pp);
        left = vp;
        right = pp;
    }
    /**
     * 
     * @param vp Verb Phrase
     * @param rb Adverb
     */
    public VerbPhrase(VerbPhrase vp, Adverb rb) {
        //VerbPhrase.add(vp);
        //VerbPhrase.add(rb);
        left = vp;
        right = rb;
    }
    /**
     * 
     * @param vp
     * @param g
     */
    public VerbPhrase(VerbPhrase vp, Gerund g) {
        left = vp;
        right = g;
    }
    public VerbPhrase(Modal m, VerbPhrase vp) {
        left = m;
        right = vp;
    }
    public VerbPhrase(Modal m, PrepositionalPhrase pp) {
        left = m;
        right = pp;
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
        String tmp = "VP:[";
        /*for(int i = 0; i < VerbPhrase.size(); i++) {
            if(i != VerbPhrase.size()-1) {
                tmp += VerbPhrase.get(i).toString() + " ";
            } else {
                tmp += VerbPhrase.get(i).toString();
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
