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
    private LinkedList<Noun> ingredientsWanted = new LinkedList<Noun>(); //all ingredientsWanted mentioned to Gusteau
    private LinkedList<Noun> ingredientsNotWanted = new LinkedList<Noun>(); //ingredientsWanted not wanted
    private LinkedList<Noun> defects = new LinkedList<Noun>(); //defects such as vegetarian
    private int nrOfPersons; //how many people

    public KnowledgeBase() {
        //empty constructor
    }
    public LinkedList<Noun> getDefects() {
        return defects;
    }

    public LinkedList<Noun> getIngredientsNotWanted() {
        return ingredientsNotWanted;
    }

    public LinkedList<Noun> getIngredientsWanted() {
        return ingredientsWanted;
    }
    
    /**
     * This function will try to add the ingredient to the kb and return a bool of its success.
     * @param ingredient is the ingredient which you want to add to the knowledge base.
     * @return true or false depending on if the ingredient was succesfully added to the kb or not.
     */
    public boolean addIngredientWanted(Noun ingredient) {
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
    public boolean removeIngredidentWanted(Noun ingredient) {
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
    public boolean addIngredientNotWanted(Noun ingredient) {
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
    public boolean removeIngreidentNotWanted(Noun ingredient) {
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
    public boolean addDefects(Noun defect) {
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
    public boolean removeDefects(Noun defect) {
        if(defects.remove(defect))
            return true;
        else
            return false;
    }
    
    public boolean setNrOfPersons(int amount) {
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
    public int getNrOfPersons() {
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
        public Noun next() {
            return ingredientsWanted.get(cnt++);
        }
        public void remove(){}
    }
    private class ingredientsNotWantedIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < ingredientsNotWanted.size();
        }
        public Noun next() {
            return ingredientsNotWanted.get(cnt++);
        }
        public void remove(){}
    }
    private class defectsIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < defects.size();
        }
        public Noun next() {
            return defects.get(cnt++);
        }
        public void remove(){}
    }
}