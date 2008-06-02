
package virtualgusteau;

import java.util.*;
import com.knowledgebooks.nlp.fasttag.FastTag;
import com.knowledgebooks.nlp.util.Tokenizer;
import grammar.*;
/**
 * 
 * 
 */
public class Model extends Observable {
    
    private String userInput;
    private String systemOutput;
    private String ingredients;
    private FastTag fastTag;
    private String[] words;
    private String[] tags;
    
    private KnowledgeBase kb;
    
    private LogicEvaluator logicEvaluator;
    
    private LinkedList<Object> grammarResult;
    private LinkedList<Object> logicalExpressions;
    private Response response;
    
    private boolean clearText = false;
        
    public Model() {
        kb = new KnowledgeBase();
        logicalExpressions = new LinkedList<Object>();
        logicEvaluator = new LogicEvaluator(kb, logicalExpressions);
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
     * @return The system response to a user userInput
     */
    public String getOutput() {
        return systemOutput;
    }
    /**
     * 
     * @return What the user inputed into the system
     */
    public String getInput() {
        return userInput;
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
     * The initial parsing of the users userInput
     * 
     * @param arg The user userInput
     */
    public void parse(String arg) {
        
        if(arg.toLowerCase().matches("i pity the fool[a-z|' ']*")) {
            View v = new View(this);
            v.setAvatar("mrT.jpg");
            v.repaint();
            System.out.println("shut up fool!");
        }
        
        words = Tokenizer.wordsToArray(arg);
        tags = fastTag.tag(words);
        userInput = arg;
        systemOutput = "";
        try {
            Grammar ng = new Grammar();
            grammarResult = ng.parser(tags, words);
            LogicCreator ns = new LogicCreator();
            logicalExpressions = ns.parser(grammarResult);
            
            for(Object o : logicalExpressions) {
                if(o instanceof Action) {
                    System.out.println(((Action)o).toString());
                } else {
                    System.out.println(((Target)o).toString());
                }
            }
            
            Iterator semIT = logicalExpressions.iterator();
            while(semIT.hasNext())
            {
                logicEvaluator.logicToKB(semIT.next());
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
            systemOutput = response.generateResponse();
            ingredients = response.getIngredients();

        } catch(KeyWordException key) {
            String keyWord = key.getKeyWord();
            systemOutput = response.handleKeyWord(keyWord);
            ingredients = response.getIngredients();
        } catch (Exception exception) {
            systemOutput = "Uhm, I'm sorry? I didn't understand that.";
        }
        setChanged();
        notifyObservers();
    }
    public void initiate(){
    	 response = new Response(this);
         response.setKB(kb);
         systemOutput = response.generateResponse();
         setChanged();
         notifyObservers();
    }
}