/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author magnus
 */
public class verb {

    private String verb;
    private String object;
    private String subject;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
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
