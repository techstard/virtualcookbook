
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
    private String nouns;
    private FastTag fastTag;
    
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
    public String getNouns() {
        return nouns;
    }
    /**
     * 
     * @param arg The user input
     */
    public void parse(String arg) {
        
        input = arg;        
        
        String[] words = Tokenizer.wordsToArray(arg);
        //String[] tags = (new FastTag()).tag(words);
        String[] tags = fastTag.tag(words);
        
        output = "\n";
        nouns = "";
        for (int i = 0; i < words.length; i++) {
            if(tags[i].contains("NN")) {
                nouns += words[i] + "\n";
            }
            output += words[i] + "/" + tags[i] + " - ";
        }
        
        setChanged();
        notifyObservers();
    }
}
