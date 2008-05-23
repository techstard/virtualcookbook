
package virtualgusteau;

import java.util.*;
import com.knowledgebooks.nlp.fasttag.FastTag;
import com.knowledgebooks.nlp.util.Tokenizer;
import grammar.*;
/**
 *
 * @author rkrantz
 */
public class Model extends Observable {
    
    private String input;
    private String output;
    private String ingredients;
    private FastTag fastTag;
    private String[] words;
    private String[] tags;
    
    private KnowledgeBase kb;
    
    private Pragmatic pragmatics;
    
    private LinkedList<Object> grammarResult;
    private LinkedList<Object> semanticsResult;
    private Response response;
    
    private boolean clearText = false;
        
    public Model() {
        //sentence = new LinkedList<Word>();
        //phraseList = new LinkedLimodelst<Phrase>();
        kb = new KnowledgeBase();
        semanticsResult = new LinkedList<Object>();
        pragmatics = new Pragmatic(kb, semanticsResult);
    }
    public void setClearText() {
        ingredients = "";
        clearText = !clearText;
    }
    public boolean getClearText() {
        return clearText;
    }
    /**
     * 
     * @param fastTag Sets the FastTag object of this class
     */
    public void setFastTag(FastTag fastTag) {
        this.fastTag = fastTag;
    }
    /**
     * 
     * @return The system response to a user input
     */
    public String getOutput() {
        return output;
    }
    /**
     * 
     * @return What the user inputed into the system
     */
    public String getInput() {
        return input;
    }
    /**
     * 
     * @return A String of all nouns in inputed sentence
     *         Separated by new line
     */
    public String getIngredients() {
        return ingredients;
    }
    /**
     * The initial parsing of the users input
     * 
     * @param arg The user input
     */
    public void parse(String arg) {
        
        if(arg.toLowerCase().matches("i pity the fool[a-z|' ']*")) {
            View v = new View(this);
            v.setAvatar("mrT.jpg");
            v.repaint();
            System.out.println("shut up fool!");
        }
            
        if( arg.compareTo("/db") == 0) {
            DB_connect db = new DB_connect();
            KnowledgeBase kb = new KnowledgeBase();
            String ingredient1 = "minced meat";
            String ingredient2 = "coffee";
            String ingredient3 = "chocolate";
            String category1 = "meat";
            //kb.addIngredientWanted(ingredient1);
            //kb.addIngredientWanted("oil");
            kb.addDishesWanted("main");
            //kb.addCategoriesWanted(category1);
            //kb.addCategoriesNotWanted(category1);
            //kb.addIngredientWanted(ingredient3);            
            //Iterator iW = kb.iWIterator();
            //db.searchRecipe(iW);
            //db.removeNotWantedRecipes("potato");
            //db.removeCategoryRecipes("meat");
            //db.findUniqueIngredients(kb);
            db.possibleRecipes(kb);
            //output = db.printRecipe(1) +db.printRecipe(2) +db.printRecipe(3) +db.printRecipe(4) +db.printRecipe(5) +db.printRecipe(6) +db.printRecipe(7);
            /*if (db.isCategory("meathej")){
                output = "true";
            }else{
                output = "false";
            }*/
            db.closeConnection();
            setChanged();
            notifyObservers();
            return;
        } else if(arg.equals("/isai")) { // test the isAnIngredient function
            DB_connect db = new DB_connect();
            // two test strings
            String test1 = "shrimpos";
            String test2 = "red wine";
            System.out.println(test1 + " = " + db.isAnIngredient(test1) + ", " + test2 + " = " + db.isAnIngredient(test2));
            //output = db.isAnIngredient("banana");
            db.closeConnection();
            setChanged();
            notifyObservers();
            return;
        } else if(arg.equals("/logic")) {
            KBValidator lg = new KBValidator(kb);
            String ing = "APpLeS";
            kb.addIngredientWanted(ing);
            if(lg.ruleThree())
                System.out.println("ruleThree == true");
            else
                System.out.println("ruleThree == false");
        } else if(arg.equals("/prag")) {
            Iterator semIT = semanticsResult.iterator();
            while(semIT.hasNext()) //has no next
            {
                kb.addCategoriesWanted(((Action)semIT.next()).getTarget().getName());
                //pragmatics.checkObject(semIT.next()); //since wantIT has no next nothing is added.
            }
            Iterator wantIT = kb.iWIterator();
            output = "This is in kb: ";
            while(wantIT.hasNext()) //has no next
            {
                String tmp = ((Action)wantIT.next()).toString();
                System.out.println("KB: " + tmp);
                output += tmp;
            }
            setChanged();
            notifyObservers();
        }
        
        words = Tokenizer.wordsToArray(arg);
        tags = fastTag.tag(words);
        input = arg;
        output = "";
        try {
            NG ng = new NG();
            grammarResult = ng.parser(tags, words);
            NS ns = new NS();
            semanticsResult = ns.parser(grammarResult);
            
            for(Object o : semanticsResult) {
                if(o instanceof Action) {
                    System.out.println(((Action)o).toString());
                } else {
                    System.out.println(((Target)o).toString());
                }
            }
            
            Iterator semIT = semanticsResult.iterator();
            while(semIT.hasNext())
            {
                pragmatics.logicToKB(semIT.next());
                //output = pragmatics.rationalResponse();
            }
            
            Iterator wantIT = kb.iWIterator();
            System.out.print("\nWant (kb): ");
            while(wantIT.hasNext())
            {
                Object tmp = wantIT.next();
                System.out.print((String)tmp + " ");
            }
            
            Iterator wantNIT = kb.iNWIterator();
            System.out.print("\nNot want (nkb): ");
            while(wantNIT.hasNext()) {
                Object tmp = wantNIT.next();
                System.out.print((String)tmp + " ");
            }
            
            Iterator wantCategory = kb.iWCIterator();
            System.out.print("\nWanted category : ");
            while(wantCategory.hasNext()) {
                Object tmp = wantCategory.next();
                System.out.print((String)tmp + " ");
            }
            
            Iterator wantDish = kb.iWDIterator();
            System.out.print("\nWanted dish : ");
            while(wantDish.hasNext()) {
                Object tmp = wantDish.next();
                System.out.print((String)tmp + " ");
            }
            System.out.println("\n");
            
            System.out.println("# People: "+kb.getNrOfPersons());
            
            response.setKB(kb);
            output = response.generateResponse();
            ingredients = response.getIngredients();

        } catch(KeyWordException key) {
            // Do something
            String keyWord = key.getKeyWord();
            output = response.handleKeyWord(keyWord);
            ingredients = response.getIngredients();
        } catch (Exception exception) {
            //System.out.println(exception.getMessage());
            output = "Uhm, I'm sorry? I didn't understand that.";
        }
        setChanged();
        notifyObservers();
    }
    public void initiate(){
    	 response = new Response(this);
         response.setKB(kb);
         output = response.generateResponse();
         setChanged();
         notifyObservers();
    }
}