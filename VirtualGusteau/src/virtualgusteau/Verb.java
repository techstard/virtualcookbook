/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author magnus
 */
public class Verb {

    private String verb;
    private Noun object;
    private Noun subject;
    
    public Verb(String word) {
        verb = word;
    }

    public Noun getObject() {
        return object;
    }

    public void setObject(Noun object) {
        this.object = object;
    }

    public Noun getSubject() {
        return subject;
    }

    public void setSubject(Noun subject) {
        this.subject = subject;
    }
    
    
    
    /**
     * Will return the verb.
     * @return
     */
    public String getVerb() {
        return verb;
    }
    /**
     * Sets the verb.
     * @param verb
     */
    public void setVerb(String verb) {
        this.verb = verb;
    }
    
}
