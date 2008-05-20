/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import java.util.LinkedList;
import java.util.*;
import grammar.*;


/**
 *
 * @author magnus
 */
public class KnowledgeBase {
    private LinkedList<Noun> ingredientsWanted = new LinkedList<Noun>(); //all ingredientsWanted mentioned to Gusteau
    private LinkedList<Noun> ingredientsNotWanted = new LinkedList<Noun>(); //ingredientsWanted not wanted
    private LinkedList<Defect> defects = new LinkedList<Defect>(); //defects such as vegetarian
    private LinkedList<Noun> categoriesWanted = new LinkedList<Noun>();
    private LinkedList<Noun> categoriesNotWanted = new LinkedList<Noun>();
    private int nrOfPersons = 1; //how many people

    public KnowledgeBase() {
        //empty constructor
    }
    public LinkedList<Defect> getDefects() {
        return defects;
    }

    public LinkedList<Noun> getCategoriesNotWanted() {
        return categoriesNotWanted;
    }

    public LinkedList<Noun> getCategoriesWanted() {
        return categoriesWanted;
    }
    
    public void addCategoriesWanted(Noun n) {
        categoriesWanted.add(n);
    }

    public void addCategoriesNotWanted(Noun n) {
        categoriesNotWanted.add(n);
    }
    
    public void removeCategoriesWanted(Noun n) {
        categoriesWanted.remove(n);
    }
    
    public void removeCategoriesNotWanted(Noun n) {
        categoriesNotWanted.remove(n);
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
    public boolean removeIngredientWanted(Noun ingredient) {
        /*for(int i = 0; i < ingredientsWanted.size(); i++) {
            if(ingredientsWanted.get(i).getNoun().equals(ingredient.getNoun())) {
                if(ingredientsWanted.remove(i).getNoun().equals(ingredient.getNoun()))
                    return true;
            }
        }
        return false;
        */
        for(int i = 0; i < ingredientsWanted.size(); i++) {
            if(ingredientsWanted.get(i).getNoun().matches("[a-z|' ']*" + ingredient.getNoun() + "[a-z |' ']*")) {
                if(ingredientsWanted.remove(i).getNoun().matches("[a-z|' ']*" + ingredient.getNoun() + "[a-z|' ']*"))
                    return true;
            }
        }
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
    public boolean removeIngredientNotWanted(Noun ingredient) {
        for(int i = 0; i < ingredientsNotWanted.size(); i++) {
            if(ingredientsNotWanted.get(i).getNoun().equals(ingredient.getNoun())) {
                if(ingredientsNotWanted.remove(i).getNoun().equals(ingredient.getNoun()))
                    return true;
            }
        }
        return false;
    }
    
    /**
     * This function will try to add a defect to the knowledge base and returns a boolean.
     * @param defect is the defect which you want to add to the kb.
     * @return a boolean if addition is succesfull or not.
     */
    private boolean addDefects(Defect defect) {
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
    private boolean removeDefects(Defect defect) {
        for(int i = 0; i < defects.size(); i++) {
            if(defects.get(i).getName().equals(defect.getName())) {
                if(defects.remove(i).getName().equals(defect.getName()))
                    return true;
            }
        }
        return false;
    }
    
    public boolean setNrOfPersons(int amount) {
        int tmp = nrOfPersons;
        if(nrOfPersons != amount && amount != 0) {
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
        public void remove() {
            defects.remove(cnt);
        }
        public void reset() {
            cnt = 0;
        }
    }
    private class ingredientsNotWantedIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < ingredientsNotWanted.size();
        }
        public Noun next() {
            return ingredientsNotWanted.get(cnt++);
        }
        public void remove(){
            defects.remove(cnt);
        }
        public void reset(){
            cnt = 0;
        }
    }
    private class defectsIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < defects.size();
        }
        public Defect next() {
            return defects.get(cnt++);
        }
        public void remove(){
            defects.remove(cnt);
        }
        public void reset(){
            cnt = 0;
        }
    }
}