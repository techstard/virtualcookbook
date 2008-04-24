/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import java.util.LinkedList;
import java.util.*;

/**
 *
 * @author magnus
 */
public class Logic {
    private KnowledgeBase kb = new KnowledgeBase();
    /**
     * Rules:
     * 1. Objects in ingredientsWanted may not be in ingredientsNotWanted.
     * 2. If any defects listed ingreientsWanted may not contain ingredients dangerous for those defacts.
     * 3. b
     * 
     */
    
    /**
     * Check so there is no conflict between ingredientsWanted and ingredientsNotWanted.
     * @return true or false depending on if rule #1 is valid or not.
     */
    private boolean ruleOne() {
        LinkedList<Noun> inw = kb.getIngredientsNotWanted();
        LinkedList<Noun> iw = kb.getIngredientsWanted();
        
        for(int i = 0; i < inw.size(); i++) {
            for(int j = 0; j < iw.size(); j++) {
                //jämnför object om de är lika.
                if(inw.get(i).getWord() == iw.get(j).getWord())
                    return false;
            }
        }
        return true;
    }
}
