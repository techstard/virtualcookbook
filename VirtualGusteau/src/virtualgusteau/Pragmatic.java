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
    
    private LinkedList<String[]> semantics;
    private KnowledgeBase kb = new KnowledgeBase();
    private KBValidator kbv = new KBValidator(kb);
    private LinkedList<String> wantPhrases = new LinkedList<String>();
    
    private String[] memory = new String[3];
    
    
    public Pragmatic(KnowledgeBase kb, LinkedList<String[]> sem) {
        semantics = sem;
        this.kb = kb;
        
        wantPhrases.add("want");
        wantPhrases.add("wants");
        wantPhrases.add("love");
        wantPhrases.add("like");
    }
    
    /**
     * This function will analyze the input from the semantics and put the relevant information into the KnowledgeBase.
     * @param sentance is the sentance that we want to analyze before putting it in to the KnowledgeBase.
     */
    public void kbAnalyzeInput(String[] sentance) {
        String action = sentance[0];
        String subject = sentance[1];
        String object = sentance[2];
        
        if(wantPhrases.contains(object)) {
            if(object.matches("^not(")) {
                kb.addIngredientNotWanted(new Noun(object));
                if(!kbv.ruleOne())
                    kb.removeIngredientWanted(new Noun(object));
            }
            else {
                kb.addIngredientWanted(new Noun(object));
                if(!kbv.ruleOne())
                    kb.removeIngreidentNotWanted(new Noun(object));
            }
        }
        
        return "lolatmagnus";
    }
    
    /**
     * if(he,she,it,they,you) {
     *      look at last sentance and get the last person or last noun
     * }
     */

}
