
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
    private String[] words;
    private String[] tags;
    
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
        words = Tokenizer.wordsToArray(arg);
        tags = fastTag.tag(words);
        
        
        
        /**
         * Debug code for the result of the parsing
         */
        output = "";
        for(int i = 0; i < words.length; i++) {
            output += words[i] + "/" + tags[i] + " ";
            //System.out.print(words[i] + "/" + tags[i] + " ");
        }
        //System.out.println("\n---------------------------------------");
                 
        sentence();
        
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
    public LinkedList<String[]> sentence() {
        LinkedList<String[]> phrases = new LinkedList<String[]>();
        String np[] = new String[1];
        
        for(int i = 0; i < tags.length; i++) {
            if(tags[i].contains("NN") || tags[i].equals("PRP")) {
                np = idNoun(i);
            } else if(tags[i].contains("VB")) {
                idVerb(i);
            } else if(tags[i].equals("RB")) {
                // Adverb
                if(words[i].toLowerCase().equals("not")) {
                    // Negation
                }
            }
            if(phrases.isEmpty() || !np.equals(phrases.getLast())) {
                phrases.add(np);
            }
        }
        /*
        for(int i = 0; i < phrases.size(); i++) {
            System.out.println(phrases.get(i));
        }
         */
        
        return phrases;
    }
    /**
     * Extracts the noun phrase(s) from the inputed list of Words given the 
     * list of grammatical tags using a set of rules to determine what kind of 
     * noun phrase it is. 
     * @param words The inputted sentence divided into seperate words stored in a
     * list of strings
     * @param tags The list grammatical tags for the word list
     */
    public String[] idNoun(int i) {
        String[] np = new String[1];
        if(i > 0 && tags[i-1].equals("DT")) {
            if(i > 1 && tags[i-2].equals("IN")) {
                // ignore since it's part of another NP
            } else { 
                np = new String[2];
                np[0] = words[i-1] + "/" + tags[i-1];
                np[1] = words[i] + "/" + tags[i];
                if(i <= tags.length-4 && tags[i+1].equals("IN") && tags[i+2].equals("DT") && tags[i+3].contains("NN")) {
                    np = new String[5];
                    np[0] = words[i-1] + "/" + tags[i-1];
                    np[1] = words[i] + "/" + tags[i];
                    np[2] = words[i+1] + "/" + tags[i+1];
                    np[3] = words[i+2] + "/" + tags[i+2];
                    np[4] = words[i+3] + "/" + tags[i+3];
                } else {
                    // Incorrect sentence ?
                }
            }
        } else {
            // single noun or pronoun?
            np = new String[1];
            np[0] = words[i] + "/" + tags[i];
        }
        /*
        System.out.print("From array: ");
        for(int j = 0; j < np.length; j++) {
            System.out.print(np[j] + " ");
        }
        System.out.println("");
         */
        logic_noun(np);
        return np;
    }
    /**
     * Creates a set of logical noun phrases of from the inputed noun_phrase
     * The inputed noun phrases will be of forms:
     *      Noun
     *      Determiner + Noun
     *      Determiner + Noun + Prepositional Phrase
     *      Pronoun
     * All words will be followed by a '/' and its grammatical tag
     * @param phrases
     */
    public String logic_noun(String[] noun_phrase) {
        int length = noun_phrase.length;
        String logic_noun_phrase = "";
        
        if(noun_phrase[0] == null) {
            
        }
        else if(length == 1) {
            // Noun or Pronoun
            //System.out.println("Noun or Pronoun");
            return noun_phrase[0];
        } else if(length == 2) {            
            // Determiner + Noun
            //System.out.println("Determiner + Noun");
            String word = noun_phrase[0].substring(0, noun_phrase[0].length()-3);
            if(word.equals("no")) {
                return "not(" + noun_phrase[1] + ")";
            } else {
                return noun_phrase[1];
            }
        } else if(length == 5) {
            // Determiner + Noun + Prepositional Phrase
            //System.out.println("Determiner + Noun + Prepositional Phrase");
            return noun_phrase[1];
        } else {
            //throw new Exception("Array \"noun_phrase\" is if incorrect length.");
        }
        return logic_noun_phrase;
        
    }
    public String[] idVerb(int i) {
        String[] vp = new String[1];
        if(tags[i].equals("VB")) {
            // Verb base form
        } else if(tags[i].equals("VBD")) {
            // Verb past tense
        } else if(tags[i].equals("VBG")) {
            // Gerund
        } else if(tags[i].equals("VBN")) {
            // Verb past participle
        } else if(tags[i].equals("VBP") || tags[i].equals("VBZ")) {
            for(int j = i; j < tags.length; j++) {
                if(tags[j].contains("VB")) {
                    
                } else if(tags[j].equals("CC")) {
                    // Sentence is split
                    //  e.g. I want apples BUT I do not want pears
                    break;
                }
            }
        }
        /*for(int j = 0; j < vp.length; j++) {
            System.out.print(vp[j] + " ");
        }
        System.out.println("\n-------------------");*/
        return vp;
    }
}