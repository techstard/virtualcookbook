/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author magnus
 */
public class Noun {

    private String noun;
    
    public Noun(String word) {
        noun = word;
    }
            
    /**
     * Will return the noun.
     * @return the noun.
     */
    public String getNoun() {
        return word;
    }
    /**
     * Will set the noun.
     * @param word
     */
    public void setNoun(String word) {
        this.word = word;
    }
    
    
}
