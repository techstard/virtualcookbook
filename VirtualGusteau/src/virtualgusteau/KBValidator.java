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

        LinkedList<Noun> inw = kb.getIngredientsNotWanted();
        LinkedList<Noun> iw = kb.getIngredientsWanted();
        
        for(int i = 0; i < inw.size(); i++) {
            for(int j = 0; j < iw.size(); j++) {
                //check if objects are equal
                System.out.println("ruleOne::" + inw.get(i).getNoun() + "?==?" + iw.get(j).getNoun());
                if(inw.get(i).getNoun().toLowerCase().matches("[a-z|' ']*" + iw.get(j).getNoun() + "[a-z|' ']*"))
                    return false;
            }
        }
        return true;
    }
    
    /**
     * will check if the given list contains anything simuler to the given item
     * @param list that you want to travers and search for item
     * @param item the item to search for
     * @return true if list is consistent otherwise false
     */
    public boolean checkConsistency(LinkedList<Noun> list, String item) {
        for (int i = 0; i < list.size(); i++)
            if(item.toLowerCase().matches(((Noun)list.get(i)).getNoun()))
                return false;
        return true;
    }
    /**
     * Check so there is no conflict between ingredientsWanted and the listed defects (such as vegetarian).
     * @return true or false depending on if rule #2 is valid or not.
     */
    public boolean ruleTwo() {
        LinkedList<Defect> dfs = kb.getDefects();
        LinkedList<Noun> iw = kb.getIngredientsWanted();
        LinkedList<Noun> ings;
        
        for(int i = 0; i < dfs.size(); i++) {
            ings = dfs.get(i).getIngredients();
            for(int j = 0; j < ings.size(); j++) {
                if(iw.get(i).equals(ings.get(j)))
                    return false;
            }
        }
        return true;
    }
    
    /**
     * Check so that no ingredient is in plural. If it finds an ingredients in plural it will change it to singular.
     * @return true if no word was found in plural.
     */
    public boolean ruleThree() {
        LinkedList<Noun> iw = kb.getIngredientsWanted();
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

        LinkedList<Noun> inw = kb.getCategoriesNotWanted();
        LinkedList<Noun> iw = kb.getCategoriesWanted();
        
        for(int i = 0; i < inw.size(); i++) {
            for(int j = 0; j < iw.size(); j++) {
                //check if objects are equal
                if(inw.get(i).getNoun().equals(iw.get(j).getNoun()))
                    return false;
            }
        }
        return true;
    }
}
