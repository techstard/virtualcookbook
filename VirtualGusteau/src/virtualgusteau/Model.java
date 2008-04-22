
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
    
    public String[] possessive = {
        "want","like","wish","love"
    };
    public String[] negation = {
        "not","no","hate"
    };
    
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
        
        /**
         * Reset these fields so that no words from one input is 
         * mixed with those from another
         */
        sentence.clear();
        phraseList.clear();
        output = "No output";
        
        /**
         * Call the imported functions for splitting a string into 
         * words and grammatical tags
         */
        String[] words = Tokenizer.wordsToArray(arg);
        String[] tags = fastTag.tag(words);
        
        
        
        /**
         * Debug code for the result of the parsing
         *
        for(int i = 0; i < words.length; i++) {
            System.out.println(words[i] + "/" + tags[i]);
        }
         */
        
        idNoun(words,tags);
        
        for(int i = 0; i < words.length; i++) {
            if(tags[i].contains("VB")) {                                        // Any form of Verb
                sentence.add(new Verb(words[i]));
            } else if(tags[i].contains("NN") || tags[i].equals("PRP")) {        // Any form of Noun and Personal Pronouns
                sentence.add(new Noun(words[i]));
            }
        }
        
        //phrases();
        
        setChanged();
        notifyObservers();
    }
    public void phrases() {
        
        VerbPhrase vp = null;
        NounPhrase np = null;
        
        /**
         * At this point the words in the user input has made into instances of their 
         * respective grammatical class and stored in a LinkedList for ease of traversal
         * 
         * LinkedList:
         *  Noun - DT - Verb - Noun
         * 
         * That is however not enough, we need to express the dependancy of the words, 
         * i.e. like noun- and verb phrases.
         * 
         * For this we might use lambda calculus to obtain a "function form" for our 
         * phrases. One example might be:
         *          I like apples -> like(I,apples)
         * 
         * We do this by traversing through the LinkedList called sentence in which 
         * all words have been stored as instances of grammatical classes.
         * 
         * If it is a noun we construct a noun phrase of it, at the moment the 
         * noun phrases can only be a one word phrase, i.e. the noun itself.
         * 
         *
         * We then starts traversing backwards in our other LinkedList, the one 
         * containing the previously created phrases, to look for a noun phrase that 
         * the verb refers to. Alternatively, if it finds a verb before that it 
         * can use the subject of that verb. 
         * 
         * If it is a verb we construct one that looks like "verb(I)", this is done
         * in the case the user writes "like apples". Then we assume he means himself
         * and can therefor add "I" to the equation.
         * 
         * 
         * 
         */
        
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
                /**
                 * Rule 1:
                 *  Set the subject of this verb to the previously closest noun phrase
                 *      or set the subject to the subject of the previously closest 
                 *      verb phrase
                 */
                for(int j = phraseList.indexOf(phraseList.getLast()); j >= 0; j--) {
                    if(phraseList.get(j) instanceof NounPhrase) {
                        vp.setSubject(phraseList.get(j));
                        break;
                    } else if(phraseList.get(j) instanceof VerbPhrase) {
                        vp.setSubject(((VerbPhrase)phraseList.get(j)).getSubject());
                        break;
                    }
                }
                
                /*for(int k = phraseList.indexOf(phraseList.getLast()); k >= 0; k--) {
                    if(phraseList.get(k) instanceof NounPhrase) {
                        //System.out.println("Found Noun Phrase: " + phraseList.get(k).getPhrase().getWord());
                        vp.setSubject(phraseList.get(k));
                        break;
                    }
                }*/
                /**
                 * After that we scan forward for a noun to wich the verb refers to, i.e
                 * I like ORANGES.
                 */
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
    public void idNoun(String[] words, String[] tags) {
        
        LinkedList<String> t = new LinkedList<String>();
        String np = "";
        
        for(int i = 0; i < tags.length; i++) {
            if(tags[i].contains("NN")) {
                if(i > 0 && tags[i-1].equals("DT")) {
                    if(i > 1 && tags[i-2].equals("IN")) {
                        // ignore since it's part of another NP
                    } else { 
                        np = words[i-1] + " " + words[i];
                        if(i <= tags.length-4 && tags[i+1].equals("IN") && tags[i+2].equals("DT") && tags[i+3].contains("NN")) {
                            np = np + " " + words[i+1] + " " + words[i+2] + " " + words[i+3];
                        } else {
                            // Incorrect sentence ?
                        }
                    }
                } else {
                    // might be an object
                }
            } else if(tags[i].equals("PRP")) {
                np = words[i];
            }
            if(t.isEmpty() || !np.equals(t.getLast())) {
                t.add(np);
            }
        }
        for(int i = 0; i < t.size(); i++) {
            System.out.println(t.get(i));
        }
        
    }
}