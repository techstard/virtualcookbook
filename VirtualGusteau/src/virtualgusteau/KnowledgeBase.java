/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import java.util.LinkedList;
import java.util.*;


/**
 *
 * @author magnus
 */
public class KnowledgeBase {
    private LinkedList<Word> ingredientsWanted = new LinkedList<Word>(); //all ingredientsWanted mentioned to Gusteau
    private LinkedList<Word> ingredientsNotWanted = new LinkedList<Word>(); //ingredientsWanted not wanted
    private LinkedList<Word> defects = new LinkedList<Word>(); //defects such as vegetarian
    private int nrOfPersons; //how many people

    public LinkedList<Word> getDefects() {
        return defects;
    }

    public LinkedList<Word> getIngredientsNotWanted() {
        return ingredientsNotWanted;
    }

    public LinkedList<Word> getIngredientsWanted() {
        return ingredientsWanted;
    }
    
    /**
     * This function will try to add the ingredient to the kb and return a bool of its success.
     * @param ingredient is the ingredient which you want to add to the knowledge base.
     * @return true or false depending on if the ingredient was succesfully added to the kb or not.
     */
    private boolean addIngredientWanted(Word ingredient) {
        if(ingredientsWanted.add(ingredient))
            return true;
        else
            return false;
    }
    
    /**
     * This function will try to remove the ingredient from the kb and return a bool of its success.
     * @param ingredient is the ingredient which you want to remove from the knowledge base.
     * @return true or false depending on if the ingredient was succesfullt removed from the kb or not.
     */
    private boolean removeIngredidentWanted(Word ingredient) {
        if(ingredientsWanted.remove(ingredient))
            return true;
        else
            return false;
    }
    
        /**
     * This function will try to add the ingredient not wanted to the kb and return a bool of its success.
     * @param ingredient is the ingredient not wanted which you want to add to the knowledge base.
     * @return true or false depending on if the ingredient was succesfully added to the kb or not.
     */
    private boolean addIngredientNotWanted(Word ingredient) {
        if(ingredientsNotWanted.add(ingredient))
            return true;
        else
            return false;
    }
    
    /**
     * This function will try to remove the ingredient not wanted from the kb and return a bool of its success.
     * @param ingredient is the ingredient not wanted which you want to remove from the knowledge base.
     * @return true or false depending on if the ingredient was succesfullt removed from the kb or not.
     */
    private boolean removeIngreidentNotWanted(Word ingredient) {
        if(ingredientsNotWanted.remove(ingredient))
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
    
    private boolean setNrOfPersons(int amount) {
        int tmp = nrOfPersons;
        if(nrOfPersons != amount)
        {
            nrOfPersons = amount;
            if(tmp == nrOfPersons)
                return false;
            else
                return true;
        }
        else
            return true;
    }
    private int getNrOfPersons() {
        return nrOfPersons;
    }
    
    
    public Iterator iWIterator() {
                return new ingredientsWantedIterator();
    }
    public Iterator iNWIterator() {
                return new ingredientsNotWantedIterator();
    }
    public Iterator dIterator() {
                return new defectsIterator();
    }
    
    private class ingredientsWantedIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < ingredientsWanted.size();
        }
        public Word next() {
            return ingredientsWanted.get(cnt++);
        }
        public void remove(){}
    }
    private class ingredientsNotWantedIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < ingredientsNotWanted.size();
        }
        public Word next() {
            return ingredientsNotWanted.get(cnt++);
        }
        public void remove(){}
    }
    private class defectsIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < defects.size();
        }
        public Word next() {
            return defects.get(cnt++);
        }
        public void remove(){}
    }
}