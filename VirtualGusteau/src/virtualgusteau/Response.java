package virtualgusteau;

import java.util.*;
import grammar.*;

public class Response {
    private KnowledgeBase kb;
    private String ingredients;
    private Model model;
    LinkedList<String> wanted;
    LinkedList<String> notWanted;
    public Response(Model model) {
        this.model = model;
    }
    public void setKB(KnowledgeBase kb) {
        this.kb = kb;
    }
    public String getIngredients() {
        return ingredients;
    }
    
    public String generateResponse() {
        wanted = kb.getIngredientsWanted();
        notWanted = kb.getIngredientsNotWanted();
        ingredients = "";
        LinkedList<String> wanted = kb.getIngredientsWanted();
        LinkedList<String> notWanted = kb.getIngredientsNotWanted();
        LinkedList recipies;
        String response;
        
        if(wanted.isEmpty()) {
            response = "You haven't told me what you want, I'm good but not that good...";
        } else {
            DB_connect db = new DB_connect();
            recipies = db.possibleRecipes(kb);
            for (String in : wanted) {
                ingredients += in+"\n";
            }
            response = "";
            if(recipies.size() > 5) {
                response += "There are many recipies matching your ingredients. Can you" +
                        " be more specific.";
            } else if(recipies.size() == 1) {
                response += "I have found this recipie matching your ingredients: \n";
                response += db.printRecipe((Integer)recipies.getFirst());
                response += "Do you want to restart or quit?";
            } else if(recipies.isEmpty()) {
                response += "There is no recipies matching, please try again";                    
            } else {
                response += "I have found "+recipies.size()+" recipies. Is there anything " +
                        "else you want?";
            }
            db.closeConnection();
        }
//        response += ". I have found "+recipies.size()+(recipies.size()==1?" recipie":" recipies")+
//                " matching your ingredients.";
        return response;
    }
    public void handleKeyWord(String word) {
        if(word.toLowerCase().equals("quit")) {
            System.exit(0);
        } else if(word.toLowerCase().equals("restart")) {
            model.setClearText();
            kb.reset();
        } else if(word.toLowerCase().equals("yes")) {
            
        } else if(word.toLowerCase().equals("no")) {
            
        }
    }
    
    public String handleIngredientException(String[] words) {
        if(words.length == 1) {
            return "I dont think that \"" + words[0] + "\" is an ingredient.";
        } else if(words.length == 2) {
            return "I found category \"" + words[0] + "\" but not an ingredient called \"" + words[1] + "\".";
        } else {
            return "Lawl";
        }
        
    }
    public String handleCategoryException(String[] words) {
        if(words.length == 1) {
            return "\"" + words[0] + "\" is not a category.";
        } else if(words.length == 2) {
            return "Category \"" + words[0] + "\" does not appear in my database but an ingredient called \"" + words[1] + "\" does.";
        } else {
            return "Lawl";
        }
    }
}