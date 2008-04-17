/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author rkrantz
 */
public class Phrase {
    private Word phrase;
    
    public Phrase(Word arg) {
        phrase = arg;
    }

    public Word getPhrase() {
        return phrase;
    }

    public void setPhrase(Word arg) {
        this.phrase = arg;
    }
}
