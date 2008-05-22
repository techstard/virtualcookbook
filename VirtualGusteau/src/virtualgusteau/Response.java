package virtualgusteau;

import java.util.*;
import grammar.*;

public class Response {
    private KnowledgeBase kb;
    private String ingredients;
    private Model model;
    LinkedList<String> wanted;
    LinkedList<String> notWanted;
    LinkedList recipes;
    String response;
    
    private enum state {SUGGEST,NORMAL};
    private state currentState = state.NORMAL;
    private String suggestedRecipe = "";
    
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
        LinkedList<String> wantedCategories = kb.getCategoriesWanted();
        LinkedList<String> notWantedCategories = kb.getCategoriesNotWanted();
        
        if(wanted.isEmpty() && wantedCategories.isEmpty()) {
            if(!kb.getUnknowns().isEmpty()) {
                // what the user said was wrong
                response = "I don't know what "+kb.getUnknowns().getFirst()+" is.";
                kb.getUnknowns().clear();
            } else {
                response = "You haven't told me what you want, I'm good but not that good...";
                currentState = state.NORMAL;
            }
        } else {
            DB_connect db = new DB_connect();
            recipes = db.possibleRecipes(kb);
            for (String in : wanted) {
                ingredients += in+"\n";
            }
            response = "";
            if(recipes.size() >= 2) {
                LinkedList<String> unique = db.findUniqueIngredients(kb);
                suggestedRecipe = unique.get((int)(Math.random()*unique.size()));
                response += "May i suggest something with " + suggestedRecipe + "?\n";
                currentState = state.SUGGEST;
            } else if(recipes.size() == 1) { 
                response += "I have found this recipie matching your ingredients: \n";
                response += db.printRecipe((Integer)recipes.getFirst(), kb.getNrOfPersons());
                response += "Do you want to restart or quit?";
                currentState = state.NORMAL;
            } else if(recipes.isEmpty() && wantedCategories.isEmpty()) {
                response += "There is no recipies matching, please try again";                    
                currentState = state.NORMAL;
            } else {
                response += "I have found "+recipes.size()+" recipies. Is there anything " +
                        "else you want?";
                currentState = state.NORMAL;
            }
            db.closeConnection();
        }
//        response += ". I have found "+recipies.size()+(recipies.size()==1?" recipie":" recipies")+
//                " matching your ingredients.";
        return response;
    }
    public String handleKeyWord(String word) {
        if(word.toLowerCase().equals("quit")) {
            System.exit(0);
        } else if(word.toLowerCase().equals("restart")) {
            model.setClearText();
            kb.reset();
            return "What do you want this time?";
        } else if(word.toLowerCase().equals("yes")) {
            if(currentState == state.SUGGEST){
                kb.addIngredientWanted(suggestedRecipe);
                currentState = state.NORMAL;
                return generateResponse();
            } else{
                return "So what is it that you want?";
            }
        } else if(word.toLowerCase().equals("no")) {
            if(currentState == state.SUGGEST){
                kb.addIngredientNotWanted(suggestedRecipe);
                currentState = state.NORMAL;
                return generateResponse();
            } else{
                // Assume this is only said when asked 
                // "Is there anything else you want?"
                // → list all recipes
                
                if(recipes != null && !recipes.isEmpty()){
                    DB_connect db = new DB_connect();
                    for(int i = 0; i < recipes.size(); i++) {
                        response += db.printRecipe((Integer)recipes.get(i), kb.getNrOfPersons());
                    }
                    response += "Do you want to restart or quit?";
                    db.closeConnection();
                } else {
                    response = "What?";
                }
             
                return response;
            }
            
        }
        return "";
    }
}