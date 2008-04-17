
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
    private LinkedList<Word> sentence;
    
    public Model() {
        sentence = new LinkedList<Word>();
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
    public String getNouns() {
        return nouns;
    }
    /**
     * The initial parsing of the users input
     * 
     * @param arg The user input
     */
    public void parse(String arg) {
        
        input = arg;      
        
        sentence.clear();
        
        String[] words = Tokenizer.wordsToArray(arg);
        String[] tags = fastTag.tag(words);
        
        for(int i = 0; i < words.length; i++) {
            if(tags[i].contains("VB")) {
                sentence.add(new Verb(words[i]));
            } else if(tags[i].contains("NN") || tags[i].equals("PRP")) {
                sentence.add(new Noun(words[i]));
            }
        }
        
        phrases();
        
        setChanged();
        notifyObservers();
    }
    public void phrases() {
        Verb v  = null;
        for(int i = 0; i < sentence.size(); i++) {
            if(sentence.get(i) instanceof Verb) {
                v = (Verb)sentence.get(i);
                for(int j = i; j >= 0; j--) {
                    if(sentence.get(j) instanceof Noun) {
                        v.setSubject((Noun)sentence.get(j));
                        break;
                    }
                }
                for(int j = i; j < sentence.size(); j++) {
                    if(sentence.get(j) instanceof Noun) {
                        v.setObject((Noun)sentence.get(j));
                        break;
                    }
                }
                break;
            }
        }
        if(v != null) {
            System.out.println(v.getWord() + "(" + 
                    v.getSubject().getWord() + ", " +
                    v.getObject().getWord() + ")");
        }
    }
}
