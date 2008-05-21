/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author Tobias
 */
public class IngredientException extends Exception {
    private String ing;
    private String cat;

    public IngredientException(String ing) {
        this.ing = ing;
    }
    public IngredientException(String cat, String ing) {
        this.cat = cat;
        this.ing = ing;
    }
    public String getIngredient() {
        return ing;
    }
    public String getCategory() {
        return cat;
    }
}
