/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import java.util.LinkedList;

/**
 *
 * @author magnus
 */
public class KnowledgeBase {
    private LinkedList<Word> ingredients; //all ingredients mentioned to Gusteau
    private LinkedList<Word> ingredients_nogo; //ingredients not wanted
    private LinkedList<Word> defects; //defects such as vegetarian
    private LinkedList<Word> portions; //how many people/portions
    

    /**
     * This function will try to add the ingredient to the kb and return a bool of its success.
     * @param ingredient is the ingredient which you want to add to the knowledge base.
     * @return true or false depending on if the ingredient was succesfully added to the kb or not.
     */
    private boolean addIngredient(Word ingredient) {
        if(ingredients.add(ingredient))
            return true;
        else
            return false;
    }
    
    /**
     * This function will try to remove the ingredient from the kb and return a bool of its success.
     * @param ingredient is the ingredient which you want to remove from the knowledge base.
     * @return true or false depending on if the ingredient was succesfullt removed from the kb or not.
     */
    private boolean removeIngreident(Word ingredient) {
        if(ingredients.remove(ingredient))
            return true;
        else
            return false;
    }
    
    /**
     * This function will try to add a defect to the knowledge base and returns a boolean.
     * @param defect is the defect which you want to add to the kb.
     * @return a boolean if addition is succesfull or not.
     */
    private boolean addDefects(Word defect) {
        if(defects.add(defect))
            return true;
        else
            return false;
    }
    
    /**
     * This function will try to remove defect from kb and returns boolean as result.
     * @param defect is the defect you want to remove.
     * @return a boolean if removeal is successfull ot not.
     */
    private boolean removeDefects(Word defect) {
        if(defects.remove(defect))
            return true;
        else
            return false;
    }
}