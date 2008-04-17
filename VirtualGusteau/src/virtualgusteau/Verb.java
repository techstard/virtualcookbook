/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author magnus
 */
public class Verb extends Word{

    private Noun object;
    private Noun subject;
    
    public Verb(String word) {
        super(word);
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
}
