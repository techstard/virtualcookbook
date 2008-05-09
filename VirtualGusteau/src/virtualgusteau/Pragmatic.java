/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import grammar.*;
import java.util.LinkedList;

/**
 *
 * @author magnus
 */
public class Pragmatic {
    
    private LinkedList<Object> semantics;
    private KnowledgeBase kb = new KnowledgeBase();
    private KBValidator kbv = new KBValidator(kb);
    private LinkedList<String> wantPhrases = new LinkedList<String>();
    
    
    public Pragmatic(KnowledgeBase kb, LinkedList<Object> sem) {
        semantics = sem;
        this.kb = kb;
        
        wantPhrases.add("want");
        wantPhrases.add("wants");
        wantPhrases.add("love");
        wantPhrases.add("like");
        
    }
    
    public void checkObject(Object obj) {
        if(obj instanceof Action) {
            if(wantPhrases.contains(((Action)obj).getName()) && ((Action)obj).isNegation()) {
                //This means that we don't want something.
                kb.addIngredientNotWanted(new Noun(((Action)obj).getTarget().getName())); //Add to not want.
                if(!kbv.ruleOne()) //if we have conflict.
                    kb.removeIngredientWanted(new Noun(((Action)obj).getTarget().getName())); //remove so no conflict.
            } else if(wantPhrases.contains(((Action)obj).getName())) {
                //this means that we want something.
                kb.addIngredientWanted(new Noun(((Action)obj).getTarget().getName())); //Add to want.
                if(!kbv.ruleOne()) //if we have conflict.
                     kb.removeIngreidentNotWanted(new Noun(((Action)obj).getTarget().getName())); //remove so no conflict.
            } else {
                //This means that the action isn't an want or an do not want.
            }
            //WHAT DO WE WANT TO DO WITH THE TARGETS!!!
            //check target
            //check target has subtarget.
            //if taget has subtarget make target class of recipe.
        } else if(obj instanceof Target) {
            //one word seantance. porbably answer to a question.
        }
            
    }
    
    public int checkRecipies() {
        //connect to db and get amount of recipies and return that amount.
        return 0;
    }
    public String checkKB() {
        if(kb.getIngredientsWanted().size() <= 0 && kb.getIngredientsNotWanted().size() <= 0 && kb.getNrOfPersons() <= 0 && kb.getDefects().size() <= 0)
            return "Hi I'm sorry I may be stupid or you haven't specified anything of importance.";
        else
            return "OMG this is the shit LOLzErS!!"; //todo
            
    }
    /**
     * if(he,she,it,they,you) {
     *      look at last sentance and get the last person or last noun
     * }
     */

}
