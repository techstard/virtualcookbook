/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author Robert Krantz & Patrik Björkman
 */
public class VerbPhrase extends Phrase {
    
    private Phrase object = null;
    private Phrase subject = null;

    public Phrase getObject() {
        return object;
    }

    public void setObject(Phrase object) {
        this.object = object;
    }

    public Phrase getSubject() {
        return subject;
    }

    public void setSubject(Phrase subject) {
        this.subject = subject;
    }
    /**
     * 
     * @param arg The function name of the Verb Phrase
     * @param w1  The subject of the Verb Phrase
     */
    public VerbPhrase(Verb arg, Phrase p1) {
        super(arg);
        subject = p1;
    }
    /**
     * 
     * @param arg The function name of the Verb Phrase
     * @param w1  The subject of the Verb Phrase
     * @param w2  The object of the Verb Phrase
     */
    public VerbPhrase(Verb arg, Phrase p1, Phrase p2) {
        super(arg);
        subject = p1;
        object = p2;
    }
    public String toString() {
        String s = "";
        s += this.getPhrase().getWord()+"(" + subject.getPhrase().getWord();
        if(object == null) {
            s += ")";
        } else {
            s += ","+object.getPhrase().getWord() + ")";
        }
        
        return s;
    }
}
