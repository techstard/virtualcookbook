
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
    private LinkedList<Phrase> phraseList;
    
    public Model() {
        sentence = new LinkedList<Word>();
        phraseList = new LinkedList<Phrase>();
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
        phraseList.clear();
        output = "No output";
        
        String[] words = Tokenizer.wordsToArray(arg);
        String[] tags = fastTag.tag(words);
        
        //for(int i = 0; i < words.length; i++) {
        //    System.out.println(words[i] + "/" + tags[i]);
        //}
        
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
        
        VerbPhrase vp = null;
        NounPhrase np = null;
        
        for(int i = 0; i < sentence.size(); i++) {
            if(sentence.get(i) instanceof Noun) {
                //System.out.println("Found Noun: " + sentence.get(i).getWord());
                np = new NounPhrase((Noun)sentence.get(i));
                phraseList.add(np);
            } else if(sentence.get(i) instanceof Verb) {
                //System.out.println("Found Verb: " + sentence.get(i).getWord());
                vp = new VerbPhrase((Verb)sentence.get(i), new NounPhrase(new Noun("I")));
                
                if(phraseList.isEmpty()) {
                    phraseList.add(new NounPhrase(new Noun("I")));
                }
                
                for(int j = phraseList.indexOf(phraseList.getLast()); j >= 0; j--) {
                    if(phraseList.get(j) instanceof NounPhrase) {
                        vp.setSubject(phraseList.get(j));
                        break;
                    } else if(phraseList.get(j) instanceof VerbPhrase) {
                        vp.setSubject(((VerbPhrase)phraseList.get(j)).getSubject());
                        break;
                    }
                }
                
                for(int k = phraseList.indexOf(phraseList.getLast()); k >= 0; k--) {
                    if(phraseList.get(k) instanceof NounPhrase) {
                        //System.out.println("Found Noun Phrase: " + phraseList.get(k).getPhrase().getWord());
                        vp.setSubject(phraseList.get(k));
                        break;
                    }
                }
                for(int j = i; j < sentence.size(); j++) {
                    //System.out.println(sentence.get(j).toString());
                    if(sentence.get(j) instanceof Noun) {
                        NounPhrase tmp = new NounPhrase((Noun)sentence.get(j));
                        vp.setObject(tmp);
                        
                        phraseList.add(tmp);
                        sentence.remove(j);
                        break;
                    }                    
                }
            }
            if(vp != null) {
                //System.out.println(vp.getObject().getPhrase().getWord());
                phraseList.add(vp);
                vp = null;
            }
        }
        //System.out.println("End of for-loops");
        
        for(int i = 0; i < phraseList.size(); i++) {
            if(phraseList.get(i) instanceof VerbPhrase) {
                output = ((VerbPhrase)phraseList.get(i)).toString() + "\n";
                //System.out.println(((VerbPhrase)phraseList.get(i)).toString());
            }
        }
        
        
        
        /*Verb v  = null;
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
        }*/
    }
}
