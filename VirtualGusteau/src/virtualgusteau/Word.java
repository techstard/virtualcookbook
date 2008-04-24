/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author magnus
 */
public class Word {
    
    private String word;
    
    public Word(String word) {
        this.word = word;
    }
    
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }    
    
    public String toString() {
        return word;
    }
}
