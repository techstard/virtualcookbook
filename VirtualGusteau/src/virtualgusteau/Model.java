
package virtualgusteau;

import java.util.*;
import com.knowledgebooks.nlp.fasttag.FastTag;
import com.knowledgebooks.nlp.util.Tokenizer;
/**
 *
 * @author rkrantz
 */
public class Model extends Observable {
    
    private String input;
    private String output;
    
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
     * @param arg The user input
     */
    public void parse(String arg) {
        
        input = arg;        
        
        String[] words = com.knowledgebooks.nlp.util.Tokenizer.wordsToArray(arg);
        String[] tags = (new FastTag()).tag(words);
        
        for (int i = 0; i < words.length; i++) {
            System.out.println(words[i] + "/" + tags[i]);
        }
        
        /*
        if(arg.contains("hello")) {
            output = "Bonjour";
        } else {
            output = "qui êtes-vous ?";
        }*/
        
        setChanged();
        notifyObservers();
    }
}
