package grammar;
import java.util.*;

public class NG {
    
    private LinkedList sentenceTree;
    private String keyWord;
    
    public NG() {
        sentenceTree = new LinkedList();
    }
    /**
     * Testa lista
     * 
     * NP → Pronoun <br>
     *    | Noun<br>
     *    | Digit<br>
     *    | Gerund<br>
     *    | Modal Pronoun<br>
     *    | Article NP<br>
     *    | AP NP<br>
     *    | NP PP<br>
     *    | Gerund Noun<br>
     * <br>
     * VP → Verb<br>
     *    | Verb VP<br>
     *    | negation Verb<br>
     *    | AdvP Verb<br>
     *    | VP NP<br>
     *    | VP AP<br>
     *    | Modal VP<br>
     *    | Modal PP<br>
     * <br>
     * PP → Preposition NP<br>
     * <br>
     * AP → Adjective<br>
     *    | Adverb Adjective<br>
     *    | AP PP<br>
     *    | AP to VP<br>
     *    | AP to NP<br>
     * <br>
     * AdvP → Adverb<br>
     *      | Adverb AdvP<br>
     * 
     * @author Robert Krantz
     * @param tags
     * @param words
     * @return A LinkedList with grammatical objects forming a tree structure
     * @throws Exception
     * @since 2008-05-13
     * @see #convert
     * @see #identifyAdverbPhrases
     * @see #connectAdverb
     * @see #identifyAdjectivePhrases
     * @see #connectAdjectiveWithNoun
     * @see #identifyCompoundNouns
     * @see #identifyNounPhrases
     * @see #identifyInfinitiveForm
     * @see #identifyPrepositionalPhrases
     * @see #connectPrepPhraseWithNounPhrase
     * @see #identifyVerbPhrases
     * @see #identifyAuxiliaries
     * @see #connectModals
     * @see #connectVerbPhrasesWithNounPhrases
     * @see #connectVerbPhrasesWithPrep
     */
    public LinkedList parser(String[] tags, String[] words) throws Exception {
        if(identifyKeyWords(tags, words)) {
            throw new KeyWordException(keyWord);
        } else {
            convert(tags,words);
            identifyAdverbPhrases();
            connectAdverb();
            identifyAdjectivePhrases();
            connectAdjectiveWithNoun();
            identifyCompoundNouns();
            identifyNounPhrases();
            identifyInfinitiveForm();
            identifyPrepositionalPhrases();
            connectPrepPhraseWithNounPhrase();
            identifyVerbPhrases();
            identifyAuxiliaries();
            connectModals();
            connectVerbPhrasesWithNounPhrases();
            connectVerbPhrasesWithPrep();        
        
            System.out.println("Word identification");
            for (int i = 0; i < sentenceTree.size(); i++) {
                System.out.print(sentenceTree.get(i).toString()+" ");
            }
            System.out.println("\n----------------------");

            return sentenceTree;
        }
    }
    public boolean identifyKeyWords(String[] tags, String[] words) {
        if(words.length == 1) {
            String word = words[0];
            if(word.toLowerCase().equals("yes")) {
                keyWord = word;
                return true;
            } else if(word.toLowerCase().equals("no")) {
                keyWord = word;
                return true;
            } else if(word.toLowerCase().equals("quit")) {
                keyWord = word;
                return true;
            } else if(word.toLowerCase().equals("restart")) {
                keyWord = word;
                return true;
            }
        }
        return false;
    }
    /**
     * Goes through the input sentence and converts the words
     * into their respective class instances. They are stored in a 
     * globally know variable, sentenceTree.
     * 
     * @author Robert Krantz
     * @param tags The input sentence represented as a series of
     * grammatical tags
     * @param words The input sentence
     * @since 2008-05-13
     * 
     */
    public void convert(String[] tags, String[] words) {        
        for (int i = 0; i < words.length; i++) {
            if(tags[i].contains("PRP")) {
                sentenceTree.add(new Pronoun(words[i]));
            } else if(tags[i].contains("NN")) {
                sentenceTree.add(new Noun(words[i]));
            } else if(tags[i].contains("VB") && !tags[i].equals("VBG")) {
                if(words[i].toLowerCase().equals("minced")) {
                    sentenceTree.add(new Noun(words[i]));
                } else {
                    sentenceTree.add(new Verb(words[i]));
                }
            } else if(tags[i].equals("VBG")) {
                sentenceTree.add(new Gerund(words[i]));
            } else if(tags[i].equals("CD")) {
                sentenceTree.add(new Digit(words[i]));
            } else if(tags[i].equals("JJ")) {
                sentenceTree.add(new Adjective(words[i]));
            } else if(tags[i].equals("RB")) {
                sentenceTree.add(new Adverb(words[i]));
            } else if(tags[i].equals("MD")) {
                sentenceTree.add(new Modal(words[i]));
            } else if(tags[i].equals("IN")) {
                sentenceTree.add(new Preposition(words[i]));
            } else if(tags[i].equals("DT")) {
                sentenceTree.add(new Article(words[i]));
            } else if(tags[i].equals("CC")) {
                sentenceTree.add(new Conjunction(words[i]));
            } else if(tags[i].equals("TO")) {
                if(i < tags.length-1) {
                    if(tags[i+1].contains("NN")) {
                        sentenceTree.add(new Preposition(words[i]));
                    } else {
                        sentenceTree.add(new To(words[i]));
                    }
                } else {
                    sentenceTree.add(new To(words[i]));
                }                
            } else if(tags[i].equals("UH")) {
                sentenceTree.add(new Noun(words[i]));
            }
        }
//        System.out.println("Word identification");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    /**
     * Searches through a sentence to check for any compound 
     * Nouns, i.e. words comprised of two words.
     * 
     * Example: Apple Pie, Strawberry Souflé ...
     * 
     * @author Robert Krantz
     * @since 2008-05-13
     * 
     */
    public void identifyCompoundNouns() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof Noun) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof Noun) {
                        sentenceTree.remove(i);
                        sentenceTree.remove(i);
                        Noun n = (Noun)o;
                        n.makeCompoun(((Noun)next).getNoun());
                        sentenceTree.add(i,n);
                    }
                }
            }
        }
//        System.out.println("identifyCompoundNouns");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    /**
     * Searches through a sentence and if it finds an 
     * Adjective Phrase tries to connect it with a following Noun.
     * If it doesn't find any Noun following the Phrase it 
     * assumes the Adjective is the object of the action and
     * creates a Noun Phrase of it.
     * 
     * @author Robert Krantz
     * @since 2008-05-13
     */
    public void connectAdjectiveWithNoun() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            
            if(o instanceof AdjectivePhrase) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof Noun) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i, new NounPhrase((AdjectivePhrase)o, 
                                (Noun)next));
                    } else {
                        sentenceTree.set(i, new NounPhrase((AdjectivePhrase)o));
                    }
                } else {
                    sentenceTree.set(i, new NounPhrase((AdjectivePhrase)o));
                }
            }
        }
//        System.out.println("connectAdjectiveWithNoun");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    /**
     * @author Robert Krantz
     * @since 2008-05-13
     */
    public void connectAdverb() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof AdverbPhrase) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof Verb) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i, new VerbPhrase((AdverbPhrase)o,
                                (Verb)next));
                    }
                }
            }
        }
//        System.out.println("connectAdverb");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void connectPrepPhraseWithNounPhrase() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof NounPhrase) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof PrepositionalPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.remove(i);
                        sentenceTree.add(i,new NounPhrase((NounPhrase)o, (PrepositionalPhrase)next));
                    }
                }
            }
        }
//        System.out.println("connectPrepPhraseWithNounPhrase");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void identifyInfinitiveForm() throws Exception {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof To) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof Verb) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i, new VerbPhrase((To)o, (Verb)next));
                    } else {
                        throw new Exception("Incorrect placement of infinitive marker");
                    }
                }
            }
        }
//        System.out.println("identifyInfinitiveForm");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void connectModals() throws Exception {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof Modal) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof VerbPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i, new VerbPhrase((Modal)o, (VerbPhrase)next));
                    } else if(next instanceof PrepositionalPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i, new VerbPhrase((Modal)o, (PrepositionalPhrase)next));
                    } else {
                        throw new Exception("Incorrect placement of Modal");
                    }
                } else {
                    throw new Exception("Incorrect placement of Modal");
                }
            }
        }
//        System.out.println("connectModals");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void connectVerbPhrasesWithNounPhrases() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof VerbPhrase) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof NounPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i, new VerbPhrase(
                                (VerbPhrase)o,(NounPhrase)next));
                    }
                }
            }
        }
//        System.out.println("connectVerbPhrases");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void connectVerbPhrasesWithPrep() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof VerbPhrase) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof PrepositionalPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i, new VerbPhrase(
                                (VerbPhrase)o,(PrepositionalPhrase)next));
                    }
                }
            } else if(o instanceof PrepositionalPhrase) {
                if(i > 0) {
                    Object previous = sentenceTree.get(i-1);
                    if(previous instanceof VerbPhrase) {
                        sentenceTree.remove(i-1);
                        sentenceTree.set(i-1, new VerbPhrase(
                                (VerbPhrase)previous,(PrepositionalPhrase)o));
                    }
                }
            }
        }
//        System.out.println("connectVerbPhrasesWithPrep");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void identifyNounPhrases() throws Exception {        
        for (int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof Noun) {
                if(i > 0) {
                    Object previous = sentenceTree.get(i-1);
                    /**
                     * Article Noun
                     */
                    if(previous instanceof AdjectivePhrase) {
                        sentenceTree.remove(i-1);
                        sentenceTree.set(i-1,new NounPhrase((AdjectivePhrase)previous, (Noun)o));                       
                    } else if(previous instanceof Article) {
                        sentenceTree.remove(i-1);
                         sentenceTree.set(i-1,new NounPhrase((Article)previous, (Noun)o));
                    } else if(previous instanceof Preposition) {
                        sentenceTree.set(i,new NounPhrase((Noun)o));
                    } else if(previous instanceof Verb) {
                        sentenceTree.set(i, new NounPhrase((Noun)o));
                    } else if(previous instanceof VerbPhrase) {
                        sentenceTree.set(i, new NounPhrase((Noun)o));
                    } else if(previous instanceof Conjunction) {
                        sentenceTree.set(i, new NounPhrase((Noun)o));
                    } else if(previous instanceof Preposition) {
                        sentenceTree.set(i, new NounPhrase((Noun)o));
                    }
                } else {
                    /**
                     * First word in sentence is a noun, 
                     * can be an answer
                     */
                    sentenceTree.set(i, new NounPhrase((Noun)o));
                }
            } else if(o instanceof Pronoun) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof Verb || next instanceof Modal ||
                            next instanceof Preposition ||
                            next instanceof Adverb) {
                        sentenceTree.set(i,new  NounPhrase((Pronoun)o));
                    }
                } else {
                    /**
                     * Last word in sentence, object is Pronoun
                     */
                    sentenceTree.set(i,new  NounPhrase((Pronoun)o));
                }
            } else if(o instanceof Modal) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof Pronoun) {
                        sentenceTree.remove(i);
                        sentenceTree.remove(i);
                        sentenceTree.add(i,new NounPhrase((Modal)o, new NounPhrase((Pronoun)next)));
                    }
                }
            } else if(o instanceof Article) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof NounPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.remove(i);
                        sentenceTree.add(i, new NounPhrase((Article)o, (NounPhrase)next));
                    } else if(next instanceof AdjectivePhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.remove(i);
                        sentenceTree.add(i, new NounPhrase((Article)o, 
                                new NounPhrase((AdjectivePhrase)next)));
                    }
                }
            } else if(o instanceof Digit) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof Noun) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i,new NounPhrase((Digit)o,(Noun)next));
                    } else {
                        sentenceTree.set(i,new NounPhrase((Digit)o));
                    }
                } else {
                    sentenceTree.set(i,new NounPhrase((Digit)o));
                }
            } else if(o instanceof NounPhrase) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof PrepositionalPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i,new NounPhrase((NounPhrase)o, (PrepositionalPhrase)next));
                    }
                }
            } else if(o instanceof Gerund) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof Noun) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i, new NounPhrase((Gerund)o, (Noun)next));                        
                    }
                } else {
                    sentenceTree.set(i, new NounPhrase((Gerund)o));
                }
            }
        }
//        System.out.println("IdentifyNounPhrase");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }    
    public void identifyVerbPhrases() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof AdverbPhrase) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof Verb) {
                        sentenceTree.remove(i);
                        sentenceTree.remove(i);
                        sentenceTree.add(i,new VerbPhrase((AdverbPhrase)o, (Verb)next));
                    }
                }
            } else if(o instanceof Verb) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof NounPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i,new VerbPhrase(new VerbPhrase((Verb)o),
                                (NounPhrase)next));
                    } else if(next instanceof AdjectivePhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i,new VerbPhrase(new VerbPhrase((Verb)o),
                                (AdjectivePhrase)next));
                    } else if(next instanceof AdverbPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i, new VerbPhrase((Verb)o,
                                (AdverbPhrase)next));
                    }
                } else if(i > 0) {
                    Object previous = sentenceTree.get(i-1);
                    if(previous instanceof NounPhrase) {
                        sentenceTree.set(i, new VerbPhrase((Verb)o));
                    }
                }
            }
        }
//        System.out.println("IdentifyVerbPhrase");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void identifyAuxiliaries() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof Verb) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof VerbPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i,new VerbPhrase((Verb)o,
                                (VerbPhrase)next));
                    }
                }
            }
        }
//        System.out.println("identifyAuxiliaries");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void identifyPrepositionalPhrases() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof Preposition) {
                if(i < sentenceTree.size()-1) {
                    Object next = sentenceTree.get(i+1);
                    if(next instanceof NounPhrase) {
                        sentenceTree.remove(i);
                        sentenceTree.set(i,new PrepositionalPhrase((Preposition)o, 
                                (NounPhrase)next));
                    } else if(next instanceof VerbPhrase) {
                        Object left = ((VerbPhrase)next).getLeft();
                        if(left instanceof To) {
                            sentenceTree.remove(i);
                            sentenceTree.set(i, new PrepositionalPhrase((Preposition)o, 
                                    (VerbPhrase)next));
                        }
                    }
                }
            }
        }
//        System.out.println("IdentifyPrep");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void identifyAdverbPhrases() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof Adverb) {
                if(i > 0) {
                    Object tmp = sentenceTree.get(i-1);
                    if(tmp instanceof AdverbPhrase) {
                        sentenceTree.remove(i-1);
                        sentenceTree.remove(i-1);
                        sentenceTree.add(i-1, new AdverbPhrase((AdverbPhrase)tmp, (Adverb)o));
                    } else {
                        sentenceTree.remove(i);
                        sentenceTree.add(i, new AdverbPhrase((Adverb)o));
                    }
                } else {
                    sentenceTree.remove(i);
                    sentenceTree.add(i, new AdverbPhrase((Adverb)o));
                }                
            }
        }
//        System.out.println("IdentifyAdverb");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
    public void identifyAdjectivePhrases() {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof Adjective) {
                sentenceTree.remove(i);
                sentenceTree.add(i, new AdjectivePhrase((Adjective)o));
            } else if(o instanceof AdverbPhrase) {
                if(i < sentenceTree.size()-1) {
                    Object tmp = sentenceTree.get(i+1);
                    if(tmp instanceof Adjective) {
                        sentenceTree.remove(i);
                        sentenceTree.remove(i);
                        sentenceTree.add(i,new AdjectivePhrase((AdverbPhrase)o, (Adjective)tmp));
                    }
                }
            }
        }
//        System.out.println("IdentifyAdjective");
//        for (int i = 0; i < sentenceTree.size(); i++) {
//            System.out.print(sentenceTree.get(i).toString()+" ");
//        }
//        System.out.println("\n----------------------");
    }
}