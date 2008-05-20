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
                
        return "lol";
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
                    kb.addCategoriesNotWanted(new Noun(((Action)obj).getName()));
                    if(!kbv.ruleFour())
                        kb.removeCategoriesWanted(new Noun(((Action)obj).getName()));
                } else { //we want
                    System.out.println("T1.1.2");
                    kb.addCategoriesWanted(new Noun(((Action)obj).getName()));
                    if(!kbv.ruleFour())
                        kb.removeCategoriesNotWanted(new Noun(((Action)obj).getName()));
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
                        kb.addIngredientNotWanted(new Noun(((Action)obj).getTarget().getName()));
                    else {
                        kb.removeIngredientWanted(new Noun(((Action)obj).getTarget().getName()));
                        kb.addIngredientNotWanted(new Noun(((Action)obj).getTarget().getName()));
                    }
                    
                    
                } else if(wantPhrases.contains(((Action)obj).getName())) { //we just want
                    kb.addIngredientWanted(new Noun(((Action)obj).getTarget().getName())); //Add to want
                    if(!kbv.ruleOne()) { //we have conflict
                        kb.removeIngredientNotWanted(new Noun(((Action)obj).getTarget().getName()));
                    }
                }
            }
        } else if(obj instanceof Target) {
            System.out.println("T2");
            if(isCategory((Target)obj)) {
                System.out.println("T2.1");
                kb.addCategoriesWanted(new Noun(((Target)obj).getName()));
            } else {
                System.out.println("T2.2");
                if(wantPhrases.contains(((Target)obj).getName())) {
                    System.out.println("T2.2.1");
                    //this means that we want something.
                    kb.addIngredientWanted(new Noun(((Target)obj).getName())); //Add to want.
                    if(!kbv.ruleOne()) //if we have conflict.
                        kb.removeIngredientNotWanted(new Noun(((Target)obj).getName())); //remove so no conflict.
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
    
    public String checkKB() {
        if(kb.getIngredientsWanted().isEmpty() && kb.getIngredientsNotWanted().isEmpty() && kb.getNrOfPersons() == 0 && kb.getDefects().isEmpty())
            return "The knowledgebase seems to be empty.";
        else
            return "OMG this is the shit LOLzErS!!"; //todo
            
    }

    public void setSemantics(LinkedList<Object> semantics) {
        this.semantics = semantics;
    }
    
    /**
     * if(he,she,it,they,you) {
     *      look at last sentance and get the last person or last noun
     * }
     */

}
