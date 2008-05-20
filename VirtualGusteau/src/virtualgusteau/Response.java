package virtualgusteau;

import java.util.*;
import grammar.*;

public class Response {
    private KnowledgeBase kb;
    public Response(KnowledgeBase kb) {
        this.kb = kb;
    }
    
    public String generateResponse() {
        LinkedList<String> wanted = kb.getIngredientsWanted();
        LinkedList<String> notWanted = kb.getIngredientsNotWanted();
        LinkedList recipies;
        String response;
        
        DB_connect db = new DB_connect();
        recipies = db.searchRecipe(wanted.iterator());
                
        response = "You want a recipe with ";
        for(int i = 0; i < wanted.size(); i++) {
            if(i == 0) {
                response += wanted.get(i);
            } else if(i == wanted.size()-1) {
                response += " and "+wanted.get(i);
            } else {
                response += ", "+wanted.get(i);
            }
        }
        response += ". I have found "+recipies.size()+(recipies.size()==1?" recipie":" recipies")+
                " matching your ingredients.";
        
        
        return response;
    }
}