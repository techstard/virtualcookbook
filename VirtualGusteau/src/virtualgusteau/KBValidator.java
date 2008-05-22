/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import java.util.LinkedList;
import java.util.*;
import java.util.regex.Pattern;
import grammar.*;

/**
 *
 * @author magnus
 */
public class KBValidator {
    private KnowledgeBase kb;

    public KBValidator(KnowledgeBase kb) {
        this.kb = kb;
    }
    
    
    /**
     * Rules:
     * 1. Objects in ingredientsWanted may not be in ingredientsNotWanted.
     * 2. If any defects listed ingreientsWanted may not contain ingredients dangerous for those defects.
     * 3. No ingredient may be in plural, because in the db it's singular
     * 4. 
     * 
     */
    
    /**
     * Check so there is no conflict between ingredientsWanted and ingredientsNotWanted.
     * @return true if no conflict is detected false otherwise.
     */
    public boolean ruleOne() {

        LinkedList<String> inw = kb.getIngredientsNotWanted();
        LinkedList<String> iw = kb.getIngredientsWanted();
        
        for(int i = 0; i < inw.size(); i++) {
            for(int j = 0; j < iw.size(); j++) {
                //check if objects are equal
                System.out.println("ruleOne::" + inw.get(i) + "?==?" + iw.get(j));
                if(inw.get(i).toLowerCase().matches("[a-z|' ']*" + iw.get(j) + "[a-z|' ']*"))
                    return false;
            }
        }
        return true;
    }
    
    /**
     * will check if the given list contains anything simuler to the given item
     * @param list that you want to travers and search for item
     * @param item the item to search for
     * @return false if list is consistent otherwise true
     */
    public boolean checkConsistency(LinkedList<String> list, String item) {
        for (int i = 0; i < list.size(); i++)
            if(item.toLowerCase().matches((list.get(i))))
                return true;
        return false;
    }
    /**
     * Check so there is no conflict between ingredientsWanted and the listed defects (such as vegetarian).
     * @return true or false depending on if rule #2 is valid or not.
     */
    public boolean ruleTwo() {
        LinkedList<Defect> dfs = kb.getDefects();
        LinkedList<String> iw = kb.getIngredientsWanted();
        LinkedList<String> ings;
        
        for(int i = 0; i < dfs.size(); i++) {
            ings = dfs.get(i).getIngredients();
            for(int j = 0; j < ings.size(); j++) {
                if(iw.get(i).equals(ings.get(j)))
                    return false;
            }
        }
        return true;
    }
    public boolean toSingular(String word) {
        if(word.matches("[a-z]*s$")) { //check if word ends with s
                if(word.matches("[a-z]*ies$")) { //check if word ends with ies
                    //replace ies with y
                    word = word.replaceAll("ies$","y"); //maybe use other function than replaceAll, spanggar
                }
                //remove s
                word = word.replaceAll("s$", "");
            }
        return true; //word may be in plural but greather chanse that no word was found in plural.
    }
    /**
     * Check so that no ingredient is in plural. If it finds an ingredients in plural it will change it to singular.
     * @return true if no word was found in plural.
     */
    public boolean ruleThree() {
        LinkedList<String> iw = kb.getIngredientsWanted();
        String tmp;
        for(int i = 0; i < iw.size(); i++) {
            tmp = iw.get(i).toString().toLowerCase();
            if(tmp.matches("[a-z]*s$")) { //check if word ends with s
                if(tmp.matches("[a-z]*ies$")) { //check if word ends with ies
                    //replace ies with y
                    tmp = tmp.replaceAll("ies$","y"); //maybe use other function than replaceAll, spanggar
                }
                //remove s
                tmp = tmp.replaceAll("s$", "");
            }
        }
        return true; //word may be in plural but greather chanse that no word was found in plural.
    }
    /**
     * Check so there is no conflict between categoriesWanted and categoriesNotWanted.
     * @return true if no conflict is detected false otherwise.
     */
    public boolean ruleFour() {

        LinkedList<String> inw = kb.getCategoriesNotWanted();
        LinkedList<String> iw = kb.getCategoriesWanted();
        
        for(int i = 0; i < inw.size(); i++) {
            for(int j = 0; j < iw.size(); j++) {
                //check if objects are equal
                if(inw.get(i).equals(iw.get(j)))
                    return false;
            }
        }
        return true;
    }
}
