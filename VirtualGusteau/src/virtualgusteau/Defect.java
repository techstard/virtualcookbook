/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import java.util.LinkedList;
import grammar.*;
/**
 *
 * @author magnus
 */
public class Defect {
    private String name;
    private LinkedList<Noun> ingredients;
    
    public Defect() {
        //empty
    }
    public Defect(String name, Noun ing) {
        this.name = name;
        this.ingredients.add(ing);
    }

    public LinkedList<Noun> getIngredients() {
        return ingredients;
    }

    private boolean addIngredient(Noun ingredient) {
        if(ingredients.add(ingredient))
            return true;
        else
            return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
            
            
}
