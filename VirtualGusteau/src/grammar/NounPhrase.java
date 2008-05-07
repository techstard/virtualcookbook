package grammar;
import java.util.*;
/**
 *
 * @author rkrantz
 */
public class NounPhrase {    
    //LinkedList<Object> NounPhrase = new LinkedList<Object>();
    private Object left;
    private Object right;
    /**
     * 
     * @param pn Noun Phrase consists of a Pronoun
     */
    public NounPhrase(Pronoun pn) {
        //NounPhrase.add(pn);
        left = pn;
    }
    /**
     * 
     * @param n Noun Phrase consists of a single noun
     */
    public NounPhrase(Noun n) {
        //NounPhrase.add(n);
        left = n;
    }
    /**
     * 
     * @param cd Noun Phrase consists of a Digit
     */
    public NounPhrase(Digit cd) {
        //NounPhrase.add(cd);
        left = cd;
    }
    /**
     * 
     * @param a The Article of the Noun Phrase
     * @param n The Noun
     */
    public NounPhrase(Article a, Noun n) {
        //NounPhrase.add(a);
        //NounPhrase.add(n);
        left = a;
        right = n;
    }
    /**
     * 
     * @param np Article Noun Phrase
     * @param pp Article Prepositional Phrase
     */
    public NounPhrase(NounPhrase np, PrepositionalPhrase pp) {
        //NounPhrase.add(np);
        //NounPhrase.add(pp);
        left = np;
        right = pp;
    }
    /**
     * 
     * @param jj Adjective
     * @param n Noun
     */
    public NounPhrase(Adjective jj, Noun n) {
        left = jj;
        right = n;
    }
    public NounPhrase(Article a, NounPhrase np) {
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
        /*for(int i = 0; i < NounPhrase.size(); i++) {
            
            if(i != NounPhrase.size()-1) {
                tmp += NounPhrase.get(i).toString() + " ";
            } else {
                tmp += NounPhrase.get(i).toString();
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
