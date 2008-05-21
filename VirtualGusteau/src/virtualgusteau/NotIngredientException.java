/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author Tobias
 */
public class NotIngredientException extends Exception {
    private String keyWord;

    public NotIngredientException(String keyWord) {
        this.keyWord = keyWord;
    }
    public String toString() {
        return keyWord;
    }
}
