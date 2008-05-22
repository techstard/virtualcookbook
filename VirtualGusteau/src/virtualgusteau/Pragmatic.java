/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import grammar.*;
import java.util.*;

/**
 *
 * @author magnus
 */
public class Pragmatic {
    
    private LinkedList<Object> semantics;

    //private KnowledgeBase kb = new KnowledgeBase();
    private KnowledgeBase kb;
    private KBValidator kbv;
    private LinkedList<String> wantPhrases = new LinkedList<String>();
    private LinkedList<String> amPhrases = new LinkedList<String>();
    private LinkedList<String> negationPhrases = new LinkedList<String>();
    
    private Object memory;
    
    
    public Pragmatic(KnowledgeBase kb, LinkedList<Object> sem) {
        semantics = sem;
        this.kb = kb;
        memory = sem;
        kbv = new KBValidator(kb);
        
        wantPhrases.add("want");
        wantPhrases.add("wants");
        wantPhrases.add("love");
        wantPhrases.add("like");
        wantPhrases.add("add");
        wantPhrases.add("contain");
        
        amPhrases.add("am");
        amPhrases.add("is");
        
        negationPhrases.add("remove");
    }

    public KnowledgeBase getKb() {
        return kb;
    }

    
    public Object getMemory() {
        return memory;
    }
    
    public void checkAnafora(Object obj) {
        
    }
    public String rationalResponse() {
        LinkedList rID;
        LinkedList<String[]> ingredients = new LinkedList<String[]>();
        DB_connect db = new DB_connect();
        Iterator iw = kb.iWIterator();
        
        if(!iw.hasNext())
            return "OMG my brain is empty!";
        
        rID = db.searchRecipe(iw);
        //db.closeConnection();
        System.out.println("size: "+rID.size());
        int id;
        String[] tmp;
        TreeMap<String,Value> basket = new TreeMap<String, Value>();
        
        for (int i = 0; i < rID.size(); i++) {
            id = (Integer)rID.get(i);
            //ingredients.add(db.getIngredients(id));
            tmp = db.getIngredients(id);
            for (int j = 0; j < tmp.length; j++) {
               // if(!kbv.checkConsistency(kb.getIngredientsNotWanted(), tmp[j])) { //true → ingredients is ok, add to basket
                Value v = basket.get(tmp[j]);
                if(v == null) {
                    // no value found → no ingredient of that name
                    basket.put(tmp[j], new Value(1,id));
                } else {
                    v.addRID(id);
                    v.incrementCounter();
                    basket.put(tmp[j], v);
                }
               // }
                //desition trees.  (ID tree)
            }
        }
        System.out.println(basket.toString());
        System.out.println("size: "+basket.size());
        db.closeConnection();
                
        return "lol"; //TODO
    }

    /**
     * Extracts ingredient and/or category from the inputed 
     * object and puts it in the correct part of KB, depending 
     * if the user wants the item or not
     * 
     * @param obj An object of the type Action or Target
     * @throws java.lang.Exception
     */
    public void logicToKB(Object obj) throws Exception {
        // If object is an action
        if(obj instanceof Action) {
            Action action = (Action)obj;
            if(wantPhrases.contains(action.getName())) {
                int n = action.getNumberOfPeople();
                if(n != 0) {
                    kb.setNrOfPersons(n);
                }
                Target target = action.getTarget();
                if(!action.isNegation()) {
                    // This Action is NOT negated
                    if(isCategory(target)) {
                        handleCategory(target,false);
                    } else if(isIngredient(target)) {
                        handleIngredient(target, false);
                    } else if(isDish(target)) {
                        handleDish(target, false);
                    } else {
                        handleUnknown(target, false);
                    }
                } else {
                    // The Action is NEGATED
                    if(isCategory(target)) {
                        handleCategory(target,true);
                    } else if(isIngredient(target)) {
                        handleIngredient(target, true);
                    } else {
                        handleUnknown(target, true);
                    }
                }
            } else if(amPhrases.contains(action.getName())) {
                // a description of the user or someone else
                // e.g. lactose intollerant
                // TODO: make a function
                System.out.println(action.getTarget().getName());
            } else if(negationPhrases.contains(action.getName())) {
                Target target = action.getTarget();
                //TODO : add so that it checks if it is category.
                if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(target.getName()))) {
                    // Ingredient exist in IngredientsWanted
                    // remove it and add to IngredientsNotWanted
                    kb.removeIngredientWanted(toSingular(target.getName()));
                }
                if(target.getSubTarget() != null) {
                    Target subTarget = target.getSubTarget();
                    if(isIngredient(subTarget)) {
                        // subTarget exist in db
                        // add to IngredientsWanted
                        if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(subTarget.getName()))) {
                            // Ingredient exist in IngredientWanted
                            // remove it and add to IngredientsNotWanted
                            kb.removeIngredientWanted(toSingular(subTarget.getName()));
                        }
                    } else {
                        // subTarget does not exist in db
                        // notify user
                        kb.addUnknowns(subTarget.getName());
                    }
                }
            }                
        } else if(obj instanceof Target) {
            // Object is a target
            Target target = (Target)obj;
            int n = target.getNumberOfPeople();
            if(n != 0) {
                kb.setNrOfPersons(n);
            }
            if(!target.isNegation()) {
                if(isCategory(target)) {
                    handleCategory(target, false);
                } else if(isIngredient(target)) {
                    handleIngredient(target, false);
                } else {
                    handleUnknown(target, false);
                }
            } else {
                // User does not want this category
                if(isCategory(target)) {
                    handleCategory(target, true);
                } else if(isIngredient(target)) {
                    handleIngredient(target, true);
                } else {
                    handleUnknown(target, true);
                }
            }
        }
    }
    /**
     * Check if a target is a category.
     * @param tag to check if category.
     * @return true if is category else false.
     */
    public boolean isCategory(Target tag) {
        String name = tag.getName().toLowerCase();
        if(name.equals("something") || name.equals("dinner") || name.equals("meal")) {
            return true;
        } else {
            DB_connect db = new DB_connect();
            if(db.isCategory(tag.getName())) {
                db.closeConnection();
                return true;
            }
            else {
                db.closeConnection();
                return false;
            }
        }     
    }
    
    /**
     * Check if a target is a category.
     * @param tag to check if category.
     * @return true if is category else false.
     */
    public boolean isIngredient(Target tag) {
        DB_connect db = new DB_connect();
        if(db.isAnIngredient(toSingular(tag.getName()))) {
            db.closeConnection();
            return true;
        } else {
            db.closeConnection();
            return false;
        }   
    }
    public boolean isDish(Target tag) {
        DB_connect db = new DB_connect();
        if(db.isDish(toSingular(tag.getName()))) {
            db.closeConnection();
            return true;
        } else {
            db.closeConnection();
            return false;
        }
    }

    public void setSemantics(LinkedList<Object> semantics) {
        this.semantics = semantics;
    }
    
    public String toSingular(String word) {
        if(word.matches("[a-z]*s$")) { //check if word ends with s
                if(word.matches("[a-z]*ies$")) { //check if word ends with ies
                    //replace ies with y
                    return word.replaceAll("ies$","y"); //maybe use other function than replaceAll, spanggar
                } else if(word.matches("[a-z]*oes$")) {
                    return word.replaceAll("oes$","o");
                }
                //remove s
                return word.replaceAll("s$", "");
            }
        return word; //word may be in plural but greather chanse that no word was found in plural.
    }
    public void handleCategory(Target target, boolean negation) {
        if(!negation) {
            // User has specified a category
            if(target.getName().toLowerCase().equals("something") || 
                    target.getName().toLowerCase().equals("dinner") || 
                    target.getName().toLowerCase().equals("meal")) {
                /* The category is not strictly a category of recipes but
                 * rather a category of food...
                 */
            } else {
                if(kbv.checkConsistency(kb.getCategoriesNotWanted(), toSingular(target.getName()))) {                                
                     /* Category exist in CategoriesNotWanted →
                     * remove it and add to CategoriesWanted
                     */
                    kb.removeCategoriesNotWanted(toSingular(target.getName()));
                    kb.addCategoriesWanted(toSingular(target.getName()));
                } else {
                    kb.addCategoriesWanted(toSingular(target.getName()));
                }
            }
            /* TODO: What if the subTarget is a category
             * example: I want something with meat
             */
            handleSubTarget(target.getSubTarget(), negation);
//            if(target.getSubTarget() != null) {
//                // The Category contains an ingredient
//                Target subTarget = target.getSubTarget();
//                if(isIngredient(subTarget)) {
//                    // Ingredient exist in the db
//                    if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
//                        /* Ingredient exist in IngredientNotWanted →
//                         * remove it and add it to IngredientWanted
//                         */
//                        kb.removeIngredientNotWanted(toSingular(subTarget.getName()));
//                        kb.addIngredientWanted(toSingular(subTarget.getName()));
//                    } else {
//                        kb.addIngredientWanted(toSingular(subTarget.getName()));
//                    }
//                } else {
//                    /* Throws an Exception because Category is correct but ingredient is not
//                     */ 
//                    kb.addUnknowns(subTarget.getName());
//                }
//            }
        } else {
            // Category exists in DB
            // User has specified a category
            if(target.getName().toLowerCase().equals("something") || 
                    target.getName().toLowerCase().equals("dinner") || 
                    target.getName().toLowerCase().equals("meal")) {
                /* The category is not strictly a category of recipes but
                 * rather a category of food...
                 */
            } else {
                if(kbv.checkConsistency(kb.getCategoriesWanted(), toSingular(target.getName()))) {
                    /* The Category exists in CategoriesWanted →
                     * remove it and add to CategoriesNotWanted
                     */
                    kb.removeCategoriesWanted(toSingular(target.getName()));
                    kb.addCategoriesNotWanted(toSingular(target.getName()));
                } else {
                    kb.addCategoriesNotWanted(toSingular(target.getName()));
                }
            }
            handleSubTarget(target.getSubTarget(), negation);
//            if(target.getSubTarget() != null) {
//                // The Category contains a subTarget
//                // TODO: what if it's a Category
//                Target subTarget = target.getSubTarget();
//                if(isIngredient(subTarget)) {
//                    // subTarget is an Ingredient
//                    if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(subTarget.getName()))) {
//                        /* Ingredient exists in IngredientsWanted →
//                         * remove it and add to IngredientsNotWanted
//                         */
//                        kb.removeIngredientWanted(toSingular(subTarget.getName()));
//                        kb.addIngredientNotWanted(toSingular(subTarget.getName()));
//                    } else {
//                        kb.addIngredientNotWanted(toSingular(subTarget.getName()));
//                    }
//                } else {
//                    kb.addUnknowns(subTarget.getName());
//                }
//            }
        }
    }
    
    public void handleIngredient(Target target, boolean negation) {
        if(!negation) {
            // User has specified an Ingredient
            if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(target.getName()))) {
                /* Ingredient exists in IngredientsNotWanted →
                 * remove it and add to IngredientsWanted
                 */
                kb.removeIngredientNotWanted(toSingular(target.getName()));
                kb.addIngredientWanted(toSingular(target.getName()));
            } else {
                kb.addIngredientWanted(toSingular(target.getName()));
            }
            handleSubTarget(target.getSubTarget(), negation);
//            if(target.getSubTarget() != null) {
//                Target subTarget = target.getSubTarget();
//                if(isIngredient(subTarget)) {
//                    // The specified Ingredient has another Ingredient as subTarget
//                    if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
//                        /* Ingredient exists in IngredientsNotWanted →
//                         * remove it and add to IngredientsWanted
//                         */
//                        kb.removeIngredientNotWanted(toSingular(subTarget.getName()));
//                        kb.addIngredientWanted(toSingular(subTarget.getName()));
//                    } else {
//                        kb.addIngredientWanted(toSingular(subTarget.getName()));
//                    }
//                } else {
//                    kb.addUnknowns(subTarget.getName());
//                }
//            }
        } else {
            // Ingredient exist in db
            // add to IngredientsWanted
            if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(target.getName()))) {
                // Ingredient exist in IngredientsWanted
                // remove it and add to IngredientsNotWanted
                kb.removeIngredientWanted(toSingular(target.getName()));
                kb.addIngredientNotWanted(toSingular(target.getName()));
            } else {
                kb.addIngredientNotWanted(toSingular(target.getName()));
            }
            handleSubTarget(target.getSubTarget(), negation);
//            if(target.getSubTarget() != null) {
//                Target subTarget = target.getSubTarget();
//                if(isIngredient(subTarget)) {
//                    // subTarget exist in db
//                    // add to IngredientsWanted
//                    if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(subTarget.getName()))) {
//                        // Ingredient exist in IngredientWanted
//                        // remove it and add to IngredientsNotWanted
//                        kb.removeIngredientWanted(toSingular(subTarget.getName()));
//                        kb.addIngredientNotWanted(toSingular(subTarget.getName()));
//                    } else {
//                        kb.addIngredientNotWanted(toSingular(subTarget.getName()));
//                    }
//                } else {
//                    kb.addUnknowns(subTarget.getName());
//                }
//            }
        }
    }
    public void handleUnknown(Target target, boolean negation) {
        Target subTarget = target.getSubTarget();
        kb.addUnknowns(target.getName());
        handleSubTarget(subTarget, negation);
    }
    public void handleDish(Target tag, boolean negation) {
        //not negated
        if(!negation) {
            if(kbv.checkConsistency(kb.getDishesNotWanted(), toSingular(tag.getName()))) {
                kb.removeDishesNotWanted(toSingular(tag.getName()));
                kb.addDishesWanted(toSingular(tag.getName()));
            } else {
                kb.addDishesWanted(toSingular(tag.getName()));
            }
            handleSubTarget(tag.getSubTarget(), negation);
        } else { //we have negation!
            if(tag.getSubTarget() != null) {
                if(kbv.checkConsistency(kb.getDishesWanted(), toSingular(tag.getName()))) {
                    kb.removeDishesWanted(toSingular(tag.getName()));
                    kb.addDishesNotWanted(toSingular(tag.getName()));
                } else {
                    kb.addDishesNotWanted(toSingular(tag.getName()));
                }
            }
            handleSubTarget(tag.getSubTarget(), negation);
        }
    }
    /**
     * 
     * @param subtag should be a subtarget
     * @param negation the negation from the action
     */
    public void handleSubTarget(Target subTag, boolean negation) {
        if(subTag != null) {
                if(isCategory(subTag)) {
                    handleCategory(subTag, negation);
                } else if(isIngredient(subTag)) {
                    handleIngredient(subTag, negation);
                } else if(isDish(subTag)) {
                    handleDish(subTag, negation);
                } else {
                    handleUnknown(subTag, negation);
                }
            }
    }
}
