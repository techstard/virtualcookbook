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
        Iterator iw = kb.iWIterator();
        recipies = db.searchRecipe(iw);
                
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
        if(recipies.size() > 5) {
            response += ".\n"+
                    "There are many recipies matching your ingredients. Can you" +
                    " be more specific.";
        } else if(recipies.size() == 1) {
            response += ".\n" +
                    "I have found this recipie matching your ingredients: \n";
            response += db.printRecipe((Integer)recipies.getFirst());
        } else if(recipies.isEmpty()) {
            response += ".\n" + 
                    "There is no recipies matching, please try again";                    
        } else {
            response += ".\n" + 
                    "I have found "+recipies.size()+" recipies. Is there anything " +
                    "else you want?";     
        }
//        response += ". I have found "+recipies.size()+(recipies.size()==1?" recipie":" recipies")+
//                " matching your ingredients.";
        
        db.closeConnection();
        return response;
    }
}