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
     * Will add an object to the kb and make sure that there won't be any ambiguety.
     * @param obj which you want to add.
     */
    public void checkObject(Object obj) {
        if(obj instanceof Action) {
            System.out.println("T1");
            if(isCategory(((Action)obj).getTarget())) {
                System.out.println("T1.1");
                if (((Action)obj).isNegation()) { //a category we don't want
                    System.out.println("T1.1.1");
                    kb.addCategoriesNotWanted(((Action)obj).getName());
                    if(!kbv.ruleFour())
                        kb.removeCategoriesWanted(((Action)obj).getName());
                } else { //we want
                    System.out.println("T1.1.2");
                    kb.addCategoriesWanted(((Action)obj).getName());
                    if(!kbv.ruleFour())
                        kb.removeCategoriesNotWanted(((Action)obj).getName());
                }
            } else {
                //This means we handle none category objects
                System.out.println("T1.2 :: Action: " + ((Action)obj).getName() + " negation: " + ((Action)obj).isNegation());
                if(wantPhrases.contains(((Action)obj).getName()) && ((Action)obj).isNegation()) {
                    System.out.println("T1.2.1");
                    //This means that we don't want something
//                    kb.addIngredientNotWanted(new Noun(((Action)obj).getTarget().getName())); //Add to not want
//                    System.out.println("T1.2.1.1");
//                    if(!kbv.ruleOne()) { //if we have conflict
//                        System.out.println("T1.2.2 Target: " + ((Action)obj).getTarget().getName());
//                        if(kb.removeIngredientWanted(new Noun(((Action)obj).getTarget().getName()))) //remove from want
//                            System.out.println("RegExp: true\n" + ((Action)obj).getTarget().getName() + " was removed from kb.");
//                        else
//                            System.out.println("RegExp: false\n" + ((Action)obj).getTarget().getName() + " wasn't removed from kb.");
//                    }
                    //This means that we don't want something
                    //true → not wanted is not in wanted → add to notwant
                    //false → not wanted is in wanted → remove from wanted add to notwanted
                    if(kbv.checkConsistency(kb.getIngredientsWanted(), ((Action)obj).getTarget().getName()))
                        kb.addIngredientNotWanted(((Action)obj).getTarget().getName());
                    else {
                        kb.removeIngredientWanted(((Action)obj).getTarget().getName());
                        kb.addIngredientNotWanted(((Action)obj).getTarget().getName());
                    }
                    
                    
                } else if(wantPhrases.contains(((Action)obj).getName())) { //we just want
                    kb.addIngredientWanted(((Action)obj).getTarget().getName()); //Add to want
                    if(!kbv.ruleOne()) { //we have conflict
                        kb.removeIngredientNotWanted(((Action)obj).getTarget().getName());
                    }
                }
            }
        } else if(obj instanceof Target) {
            System.out.println("T2");
            if(isCategory((Target)obj)) {
                System.out.println("T2.1");
                kb.addCategoriesWanted(((Target)obj).getName());
            } else {
                System.out.println("T2.2");
                if(wantPhrases.contains(((Target)obj).getName())) {
                    System.out.println("T2.2.1");
                    //this means that we want something.
                    kb.addIngredientWanted(((Target)obj).getName()); //Add to want.
                    if(!kbv.ruleOne()) //if we have conflict.
                        kb.removeIngredientNotWanted(((Target)obj).getName()); //remove so no conflict.
                }
            }
        }
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
                        if(target.getSubTarget() != null) {
                            // The Category contains an ingredient
                            Target subTarget = target.getSubTarget();
                            if(isIngredient(subTarget)) {
                                // Ingredient exist in the db
                                if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
                                    /* Ingredient exist in IngredientNotWanted →
                                     * remove it and add it to IngredientWanted
                                     */
                                    kb.removeIngredientNotWanted(toSingular(subTarget.getName()));
                                    kb.addIngredientWanted(toSingular(subTarget.getName()));
                                } else {
                                    kb.addIngredientWanted(toSingular(subTarget.getName()));
                                }
                            } else {
                                /* Throws an Exception because Category is correct but ingredient is not
                                 */ 
                                throw new IngredientException(target.getName(),subTarget.getName());
                            }
                        }
                    } else if(isIngredient(new Target(toSingular(target.getName())))) {
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
                        if(target.getSubTarget() != null) {
                            Target subTarget = target.getSubTarget();
                            if(isIngredient(subTarget)) {
                                // The specified Ingredient has another Ingredient as subTarget
                                if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
                                    /* Ingredient exists in IngredientsNotWanted →
                                     * remove it and add to IngredientsWanted
                                     */
                                    kb.removeIngredientNotWanted(toSingular(subTarget.getName()));
                                    kb.addIngredientWanted(toSingular(subTarget.getName()));
                                } else {
                                    kb.addIngredientWanted(toSingular(subTarget.getName()));
                                }
                            } else {
                                /**
                                 * Throw a new IngredientException because the subIngredient
                                 * did not exist in DB
                                 */
                                throw new IngredientException(subTarget.getName());
                            }
                        }
                    } else {
                        /* What the user has specified is not a Category, 
                         * nor is it an Ingredient
                         */
                        Target subTarget = target.getSubTarget();
                        if(subTarget != null) {
                            /* Whatever it is, it has a subTarget
                             * TODO: what if it's a category
                             */
                            if(isIngredient(subTarget)) {
                                // SubTarget is an Ingredient
                                if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
                                    /* Ingredient exists in IngredientsNotWanted →
                                     * remove it and add to IngredientsWanted
                                     */
                                    kb.removeIngredientNotWanted(toSingular(subTarget.getName()));
                                    kb.addIngredientWanted(toSingular(subTarget.getName()));
                                } else {
                                    kb.addIngredientWanted(toSingular(subTarget.getName()));
                                }
                                /**
                                 * Throws a new CategoryException because the category
                                 * specified is wrong but the Ingredient is not
                                 */
                                throw new CategoryException(target.getName(),subTarget.getName());
                            } else {
                                // TODO: throw CategoryIngredientException
                            }
                        } else {
                            // Category does not exist
                            throw new CategoryException(target.getName());
                        }
                    }
                } else {
                    // The Action is NEGATED
                    if(isCategory(target)) {
                        // Category exists in DB
                        if(kbv.checkConsistency(kb.getCategoriesWanted(), toSingular(target.getName()))) {
                            /* The Category exists in CategoriesWanted →
                             * remove it and add to CategoriesNotWanted
                             */
                            kb.removeCategoriesWanted(toSingular(target.getName()));
                            kb.addCategoriesNotWanted(toSingular(target.getName()));
                        } else {
                            kb.addCategoriesNotWanted(toSingular(target.getName()));
                        }
                        if(target.getSubTarget() != null) {
                            // The Category contains a subTarget
                            // TODO: what if it's a Category
                            Target subTarget = target.getSubTarget();
                            if(isIngredient(subTarget)) {
                                // subTarget is an Ingredient
                                if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(subTarget.getName()))) {
                                    /* Ingredient exists in IngredientsWanted →
                                     * remove it and add to IngredientsNotWanted
                                     */
                                    kb.removeIngredientWanted(toSingular(subTarget.getName()));
                                    kb.addIngredientNotWanted(toSingular(subTarget.getName()));
                                } else {
                                    kb.addIngredientNotWanted(toSingular(subTarget.getName()));
                                }
                            } else {
                                // Ingredient does not exist in the db
                                // notify user that there is a category with this name
                                // but no ingredients
                                System.out.println("No such ingredient - negatedCategory");
                            }
                        } else {
                            // The user has only specified a category of recipes
                            // No reason to do anything
                        }
                    } else if(isIngredient(target)) {
                        // Ingredient exist in db
                        // add to IngredientsWanted
                        if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(target.getName()))) {
                                // Ingredient already exist in IngredientNotWanted
                        } else if(kb.getIngredientsWanted().contains(toSingular(target.getName()))) {
                            // Ingredient exist in IngredientsWanted
                            // remove it and add to IngredientsNotWanted
                            kb.removeIngredientWanted(toSingular(target.getName()));
                            kb.addIngredientNotWanted(toSingular(target.getName()));
                        } else {
                            kb.addIngredientNotWanted(toSingular(target.getName()));
                        }
                        if(target.getSubTarget() != null) {
                            Target subTarget = target.getSubTarget();
                            if(isIngredient(subTarget)) {
                                // subTarget exist in db
                                // add to IngredientsWanted
                                if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
                                // Ingredient already exist in IngredientsNotWanted
                                } else if(kb.getIngredientsWanted().contains(toSingular(subTarget.getName()))) {
                                    // Ingredient exist in IngredientWanted
                                    // remove it and add to IngredientsNotWanted
                                    kb.removeIngredientWanted(toSingular(subTarget.getName()));
                                    kb.addIngredientNotWanted(toSingular(subTarget.getName()));
                                } else {
                                    kb.addIngredientNotWanted(toSingular(subTarget.getName()));
                                }
                            } else {
                                // subTarget does not exist in db
                                // notify user
                            }
                        } else {
                            // no subTarget, no need to do anything
                        }
                    } else {
                        // What the user doesn't want is neither a category nor an ingredient
                    }
                }
            } else if(amPhrases.contains(action.getName())) {
                // a description of the user or someone else
                // e.g. lactose intollerant
                // TODO: make a function
                System.out.println(action.getTarget().getName());
            } else if(negationPhrases.contains(action.getName())) {
                Target target = action.getTarget();
                if(kb.getIngredientsWanted().contains(toSingular(target.getName()))) {
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
                    }
                } else {
                    // no subTarget, no need to do anything
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
                    // The thing the user has specified is a category in the db
                    // Add category to wantCategory
                    if(target.getName().toLowerCase().equals("something") || 
                            target.getName().toLowerCase().equals("dinner") || 
                            target.getName().toLowerCase().equals("meal")) {

                    } else {
                        if(kbv.checkConsistency(kb.getCategoriesWanted(), toSingular(target.getName()))) {
                            // CategoriesWanted already contains this category
                            // either do nothing or notify the user
                        } else if(kbv.checkConsistency(kb.getCategoriesNotWanted(), toSingular(target.getName()))) {
                            // CategoriesNotWanted contains this category, 
                            // remove it and add it to CategoriesWanted
                            kb.removeCategoriesNotWanted(toSingular(target.getName()));
                            kb.addCategoriesWanted(toSingular(target.getName()));
                        } else {
                            kb.addCategoriesWanted(toSingular(target.getName()));
                        }
                    }
                    if(target.getSubTarget() != null) {
                        // The category should contain a specific ingredient
                        Target subTarget = target.getSubTarget();
                        if(isIngredient(subTarget)) {
                            // Ingredient exist in the db
                            // Add ingredient to to wantIngredients
                            if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(subTarget.getName()))) {
                                // Ingredient already exist in IngredientWanted
                            } else if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
                                // Ingredient exist in IngredientNotWanted
                                // remove it and add to IngredientsWanted
                                kb.removeIngredientNotWanted(toSingular(subTarget.getName()));
                                kb.addIngredientWanted(toSingular(subTarget.getName()));
                            } else {
                                kb.addIngredientWanted(toSingular(subTarget.getName()));
                            }
                        } else {
                            // Ingredient does not exist in the db
                            // notify user that there is a category with this name
                            // but no ingredients
                            System.out.println("No such ingredient - category");
                        }
                    } else {
                        // The user has only specified a category of recipes
                        // No reason to do anything
                    }
                } else if(isIngredient(target)) {
                    // Ingredient exist in db
                    // add to IngredientsWanted
                    if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(target.getName()))) {
                            // Ingredient already exist in IngredientWanted
                    } else if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(target.getName()))) {
                        // Ingredient exist in IngredientNotWanted
                        // remove it and add to IngredientsWanted
                        kb.removeIngredientNotWanted(toSingular(target.getName()));
                        kb.addIngredientWanted(toSingular(target.getName()));
                    } else {
                        kb.addIngredientWanted(toSingular(target.getName()));
                    }
                    if(target.getSubTarget() != null) {
                        Target subTarget = target.getSubTarget();
                        if(isIngredient(subTarget)) {
                            // subTarget exist in db
                            // add to IngredientsWanted
                            if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(subTarget.getName()))) {
                            // Ingredient already exist in IngredientWanted
                            } else if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
                                // Ingredient exist in IngredientNotWanted
                                // remove it and add to IngredientsWanted
                                kb.removeIngredientNotWanted(toSingular(subTarget.getName()));
                                kb.addIngredientWanted(toSingular(subTarget.getName()));
                            } else {
                                kb.addIngredientWanted(toSingular(subTarget.getName()));
                            }
                        } else {
                            // subTarget does not exist in db
                            // notify user
                        }
                    } else {
                        // no subTarget, no need to do anything
                    }
                } else {
                    // What the user wants is neither a category nor an ingredient                    
                }
            } else {
                // User does not want this category
                if(isCategory(target)) {
                    // The thing the user has specified is a category in the db
                    // Add category to notWantCategory
                    if(kbv.checkConsistency(kb.getCategoriesNotWanted(), toSingular(target.getName()))) {
                        // CategoriesNotWanted already contains this category
                        // either do nothing or notify the user
                    } else if(kbv.checkConsistency(kb.getCategoriesWanted(), toSingular(target.getName()))) {
                        // CategoriesWanted contains this category, 
                        // remove it and add it to CategoriesWanted
                        kb.removeCategoriesWanted(toSingular(target.getName()));
                        kb.addCategoriesNotWanted(toSingular(target.getName()));
                    } else {
                        kb.addCategoriesNotWanted(toSingular(target.getName()));
                    }
                    if(target.getSubTarget() != null) {
                        // The category should contain a specific ingredient
                        Target subTarget = target.getSubTarget();
                        if(isIngredient(subTarget)) {
                            // Ingredient exist in the db
                            // Add ingredient to to wantIngredients
                            if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
                                // Ingredient already exist in IngredientNotWanted
                            } else if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(subTarget.getName()))) {
                                // Ingredient exist in IngredientWanted
                                // remove it and add to IngredientsNotWanted
                                kb.removeIngredientWanted(toSingular(subTarget.getName()));
                                kb.addIngredientNotWanted(toSingular(subTarget.getName()));
                            } else {
                                kb.addIngredientNotWanted(toSingular(subTarget.getName()));
                            }
                        } else {
                            // Ingredient does not exist in the db
                            // notify user that there is a category with this name
                            // but no ingredients
                            System.out.println("No such ingredient - negatedCategory");
                        }
                    } else {
                        // The user has only specified a category of recipes
                        // No reason to do anything
                    }
                } else if(isIngredient(target)) {
                    // Ingredient exist in db
                    // add to IngredientsWanted
                    if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(target.getName()))) {
                            // Ingredient already exist in IngredientNotWanted
                    } else if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(target.getName()))) {
                        // Ingredient exist in IngredientsWanted
                        // remove it and add to IngredientsNotWanted
                        kb.removeIngredientWanted(toSingular(target.getName()));
                        kb.addIngredientNotWanted(toSingular(target.getName()));
                    } else {
                        kb.addIngredientNotWanted(toSingular(target.getName()));
                    }
                    if(target.getSubTarget() != null) {
                        Target subTarget = target.getSubTarget();
                        if(isIngredient(subTarget)) {
                            // subTarget exist in db
                            // add to IngredientsWanted
                            if(kbv.checkConsistency(kb.getIngredientsNotWanted(), toSingular(subTarget.getName()))) {
                            // Ingredient already exist in IngredientsNotWanted
                            } else if(kbv.checkConsistency(kb.getIngredientsWanted(), toSingular(subTarget.getName()))) {
                                // Ingredient exist in IngredientWanted
                                // remove it and add to IngredientsNotWanted
                                kb.removeIngredientWanted(toSingular(subTarget.getName()));
                                kb.addIngredientNotWanted(toSingular(subTarget.getName()));
                            } else {
                                kb.addIngredientNotWanted(toSingular(subTarget.getName()));
                            }
                        } else {
                            // subTarget does not exist in db
                            // notify user
                        }
                    } else {
                        // no subTarget, no need to do anything
                    }
                } else {
                    // What the user doesn't want is neither a category nor an ingredient
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
        if(db.isAnIngredient(tag.getName())) {
            db.closeConnection();
            return true;
        }
        else {
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
    /**
     * if(he,she,it,they,you) {
     *      look at last sentance and get the last person or last noun
     * }
     */

}
