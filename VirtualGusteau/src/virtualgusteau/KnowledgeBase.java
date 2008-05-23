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
    private LinkedList<String> ingredientsWanted = new LinkedList<String>(); //all ingredientsWanted mentioned to Gusteau
    private LinkedList<String> ingredientsNotWanted = new LinkedList<String>(); //ingredientsWanted not wanted
    private LinkedList<Defect> defects = new LinkedList<Defect>(); //defects such as vegetarian
    private LinkedList<String> categoriesWanted = new LinkedList<String>();
    private LinkedList<String> categoriesNotWanted = new LinkedList<String>();
    private LinkedList<Integer> recipeIDs = new LinkedList<Integer>();
    private LinkedList<String> unknowns = new LinkedList<String>();

    private LinkedList<String> dishesWanted = new LinkedList<String>();
    private LinkedList<String> dishesNotWanted = new LinkedList<String>();

    private int recommendedRecipe;
    
    private int nrOfPersons = 1; //how many people

    public KnowledgeBase() {
        //empty constructor
    }
    public void reset() {
        ingredientsWanted.clear();
        ingredientsNotWanted.clear();
        defects.clear();
        categoriesWanted.clear();
        categoriesNotWanted.clear();
        recipeIDs.clear();
        nrOfPersons = 1;
        dishesNotWanted.clear();
        dishesWanted.clear();
    }
    
    public int getRecRec(){
        return recommendedRecipe;
    }
    
    public void setRecRec(int r){
        recommendedRecipe = r;
    }
    
    public LinkedList<Defect> getDefects() {
        return defects;
    }

    public LinkedList<String> getCategoriesNotWanted() {
        return categoriesNotWanted;
    }

    public LinkedList<String> getCategoriesWanted() {
        return categoriesWanted;
    }

    public LinkedList<String> getDishesWanted() {
        return dishesWanted;
    }
    public LinkedList<String> getDishesNotWanted() {
        return dishesNotWanted;
    }
    
    public void addDishesWanted(String n) {
        dishesWanted.add(n.toLowerCase());
    }
    public void addDishesNotWanted(String n) {
        dishesNotWanted.add(n.toLowerCase());
    }
    
    public void addCategoriesWanted(String n) {
        categoriesWanted.add(n.toLowerCase());
    }

    public void addCategoriesNotWanted(String n) {
        categoriesNotWanted.add(n.toLowerCase());
    }
    
    public void removeCategoriesWanted(String n) {
        categoriesWanted.remove(n.toLowerCase());
    }
    
    public void removeCategoriesNotWanted(String n) {
        categoriesNotWanted.remove(n.toLowerCase());
    }
    
    public LinkedList<String> getIngredientsNotWanted() {
        return ingredientsNotWanted;
    }

    public LinkedList<String> getIngredientsWanted() {
        return ingredientsWanted;
    }

    public void setRecipeIDs(LinkedList<Integer> recipeIDs) {
        this.recipeIDs = recipeIDs;
    }
    
    public LinkedList<Integer> getRecipeIDs() {
        return recipeIDs;
    }

    public LinkedList<String> getUnknowns() {
        return unknowns;
    }
    
    
    
    /**
     * This function will try to add the ingredient to the kb and return a bool of its success.
     * @param ingredient is the ingredient which you want to add to the knowledge base.
     * @return true or false depending on if the ingredient was succesfully added to the kb or not.
     */
    public boolean addIngredientWanted(String ingredient) {
        if(ingredientsWanted.contains(ingredient.toLowerCase()))
            return true;
        if(ingredientsWanted.add(ingredient.toLowerCase()))
            return true;
        else
            return false;
    }
    
    /**
     * This function will try to remove the ingredient from the kb and return a bool of its success.
     * @param ingredient is the ingredient which you want to remove from the knowledge base.
     * @return true or false depending on if the ingredient was succesfullt removed from the kb or not.
     */
    public boolean removeIngredientWanted(String ingredient) {
        /*for(int i = 0; i < ingredientsWanted.size(); i++) {
            if(ingredientsWanted.get(i).getNoun().equals(ingredient.getNoun())) {
                if(ingredientsWanted.remove(i).getNoun().equals(ingredient.getNoun()))
                    return true;
            }
        }
        return false;
        */
        ingredient = ingredient.toLowerCase();
        for(int i = 0; i < ingredientsWanted.size(); i++) {
            if(ingredientsWanted.get(i).matches("[a-z|' ']*" + ingredient + "[a-z |' ']*")) {
                if(ingredientsWanted.remove(i).matches("[a-z|' ']*" + ingredient + "[a-z|' ']*"))
                    return true;
            }
        }
        return false;
    }
    
    public boolean addUnknowns(String ingredient) {
        if(unknowns.contains(ingredient.toLowerCase()))
            return true;
        if(unknowns.add(ingredient.toLowerCase()))
            return true;
        else
            return false;
    }
    
        /**
     * This function will try to add the ingredient not wanted to the kb and return a bool of its success.
     * @param ingredient is the ingredient not wanted which you want to add to the knowledge base.
     * @return true or false depending on if the ingredient was succesfully added to the kb or not.
     */
    public boolean addIngredientNotWanted(String ingredient) {
        if(ingredientsNotWanted.contains(ingredient.toLowerCase()))
            return true;
        if(ingredientsNotWanted.add(ingredient.toLowerCase()))
            return true;
        else
            return false;
    }
    
    /**
     * This function will try to remove the ingredient not wanted from the kb and return a bool of its success.
     * @param ingredient is the ingredient not wanted which you want to remove from the knowledge base.
     * @return true or false depending on if the ingredient was succesfullt removed from the kb or not.
     */
    public boolean removeIngredientNotWanted(String ingredient) {
        ingredient = ingredient.toLowerCase();
        for(int i = 0; i < ingredientsNotWanted.size(); i++) {
            if(ingredientsNotWanted.get(i).equals(ingredient)) {
                if(ingredientsNotWanted.remove(i).equals(ingredient))
                    return true;
            }
        }
        return false;
    }
    public boolean removeDishesNotWanted(String dish) {
        dish = dish.toLowerCase();
        for (int i = 0; i < dishesNotWanted.size(); i++) {
            if(dishesNotWanted.get(i).equals(dish)) {
                if(dishesNotWanted.remove(i).equals(dish))
                    return true;
            }
        }
        return false;
    }
    public boolean removeDishesWanted(String dish) {
        dish = dish.toLowerCase();
        for (int i = 0; i < dishesWanted.size(); i++) {
            if(dishesWanted.get(i).equals(dish)) {
                if(dishesWanted.remove(i).equals(dish))
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
     * @param defect is the defect you want to remove.KnowledgeBase
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
    public void addIngredientsFromRecipe(int recipeID) {
        DB_connect db = new DB_connect();
        String[] ings = db.getIngredients(recipeID);
        for (int i = 0; i < ings.length; i++) {
            ingredientsWanted.add(ings[i]);
        }
        db.closeConnection();

    }
    
    public Iterator iWIterator() {
        return new IngredientsWantedIterator();
    }
    public Iterator iNWIterator() {
        return new IngredientsNotWantedIterator();
    }

    public Iterator iWCIterator() {
        return new CategoryWantedIterator();

    }
    public Iterator iNWCIterator() {
        return new NotWantedCategoryIterator();
    }
    public Iterator dIterator() {
        return new DefectsIterator();
    }
    
    private class IngredientsWantedIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < ingredientsWanted.size();
        }
        public String next() {
            return ingredientsWanted.get(cnt++);
        }
        public void remove() {
            defects.remove(cnt);
        }
        public void reset() {
            cnt = 0;
        }
    }
    private class IngredientsNotWantedIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < ingredientsNotWanted.size();
        }
        public String next() {
            return ingredientsNotWanted.get(cnt++);
        }
        public void remove(){
            defects.remove(cnt);
        }
        public void reset(){
            cnt = 0;
        }
    }
    private class DefectsIterator implements Iterator {
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
    private class CategoryWantedIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < categoriesWanted.size();
        }
        public String next() {
            return categoriesWanted.get(cnt++);
        }
        public void remove(){
            categoriesWanted.remove(cnt);
        }
        public void reset(){
            cnt = 0;
        }
    }
    private class NotWantedCategoryIterator implements Iterator {
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < categoriesNotWanted.size();
        }
        public String next() {
            return categoriesNotWanted.get(cnt++);
        }
        public void remove(){
            categoriesNotWanted.remove(cnt);
        }
        public void reset(){
            cnt = 0;
        }
    }
}