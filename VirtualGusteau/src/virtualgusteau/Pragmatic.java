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
    
    private String[] memory = new String[3];
    
    
    public Pragmatic(LinkedList<String[]> sem) {
        semantics = sem;
    }
    public Pragmatic(KnowledgeBase kb) {
        this.kb = kb;
    }
    
    public String kbAnalyzeInput(String[] sentance) {
        String action = sentance[0];
        String object = sentance[1];
        String subject = sentance[2];
        
        if(action == "want")
            kb.addIngredientWanted(new Noun(object));
    }
    
    /**
     * if(he,she,it,they,you) {
     *      look at last sentance and get the last person or last noun
     * }
     */

}
