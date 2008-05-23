package virtualgusteau;

import java.util.*;
import grammar.*;

public class Response {
    private KnowledgeBase kb;
    private String ingredients;
    private Model model;
    private LinkedList<String> wanted;
    private LinkedList<String> notWanted;
    private LinkedList recipes;
    private String response;
    
    private boolean recommend = true;
    
    private enum state {RECOMMEND,SUGGEST,NORMAL};
    private state currentState = state.RECOMMEND;
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
        //LinkedList<String> wanted = kb.getIngredientsWanted(); //TODO
        //LinkedList<String> notWanted = kb.getIngredientsNotWanted(); //TODO
        LinkedList<String> wantedCategories = kb.getCategoriesWanted();
        LinkedList<String> notWantedCategories = kb.getCategoriesNotWanted();
        
        if(recommend){
            // Find a random recipe to recommend.
            DB_connect db = new DB_connect();
            int randomRecipeID = (int)(Math.random()*11+1);
            kb.setRecRec(randomRecipeID);
            response = "I can recommend ze tasty " + db.findRecipeName(randomRecipeID) + ", do you want to see ze recipe?";
            //System.out.println(response);
            db.closeConnection();
            recommend = false;
            currentState = state.RECOMMEND;
            return response;
        } else {
            currentState = state.NORMAL;
        }
                         
        if(wanted.isEmpty() && wantedCategories.isEmpty()) {
            if(!kb.getUnknowns().isEmpty()) {
                // what the user said was wrong
            	response = "I'm sorry, but I've never heard of ";
            	while (!kb.getUnknowns().isEmpty()) {
            		response += kb.getUnknowns().removeFirst();
            		if (kb.getUnknowns().size() >= 1)
            			response += " or ";
            		else
            			response += ".";
            	}
            } else {
                response = "You haven't told me what you want, I'm good but not that good...";
            }
            currentState = state.NORMAL;
        } else if (!kb.getUnknowns().isEmpty()) {
        	response = "I'm sorry, but I don't know what ";
        	while (!kb.getUnknowns().isEmpty()) {
        		response += kb.getUnknowns().removeFirst();
        		if (kb.getUnknowns().size() > 1)
        			response += " or ";
        		else
        			response += " is.";
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
                response += "I have found " +recipes.size() + " recipies, may i suggest something with " + suggestedRecipe + "?\n";
                currentState = state.SUGGEST;
            } else if(recipes.size() == 1) { 
                response += "Eureka! I have found this recipie matching your ingredients: \n";
                response += db.printRecipe((Integer)recipes.getFirst(), kb.getNrOfPersons());
                response += "Do you want to restart or quit?";
                currentState = state.NORMAL;
            } else if(recipes.isEmpty() && wantedCategories.isEmpty()) {
                response += "Im sorry, but there is no recipies matching. You can ask me for another recipe if you'd like.";                    
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
        if(word.toLowerCase().matches("quit|goodbye|bye")) {
            System.exit(0);
        } else if(word.toLowerCase().equals("restart")) {
            model.setClearText();
            kb.reset();
            recommend = true;
            return generateResponse();
        } else if (word.toLowerCase().matches("hello|greetings|hi")){
            int i = (int)(Math.random()*2);
            switch (i){
                case 0:     response = "Yes, well... greetings to you. Now what sort of recipe do you want?";
                            break;
                case 1:     response = "Well, hello! What sort of recipe do you want?";
                            break;
                case 2:     response = "Salutations! What sort of recipe do you want?";
                            break;
                default:    break;
            }
            return response;
        } else if(word.toLowerCase().matches("yes|ok")) {
            if (currentState == state.SUGGEST){
                kb.addIngredientWanted(suggestedRecipe);
                currentState = state.NORMAL;
                return generateResponse();
            } else if (currentState == state.RECOMMEND){
                //add to kb
                kb.addIngredientsFromRecipe(kb.getRecRec());
                
                DB_connect db = new DB_connect();
                String recipe = db.printRecipe(kb.getRecRec(), kb.getNrOfPersons());
                db.closeConnection();
                currentState = state.NORMAL;
                
                for (String in : kb.getIngredientsWanted()) {
                    ingredients += in+"\n";
                }
                return "Merveilleux! Here is the recipe.\n"+recipe+"Ok! Do you want to restart or quit?";            
            } else {
                return "So what is it zat you want?";
            }
        } else if(word.toLowerCase().equals("no")) {
            if(currentState == state.SUGGEST){
                kb.addIngredientNotWanted(suggestedRecipe);
                currentState = state.NORMAL;
                return generateResponse();
            } else if (currentState == state.RECOMMEND){
                currentState = state.NORMAL;
                return "No? Well then, what sort of recipe do you want? Please tell me.";            
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