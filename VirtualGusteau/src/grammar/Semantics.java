package grammar;
import java.util.*;

public class Semantics {
    
    public Semantics() {
        semSentences = new LinkedList<String[]>();
    }
    
    private String object = null;
    private String action = null;
    private String subject = null;
    
    private String[] atomic = new String[3];
    
    private LinkedList<String[]> semSentences;
    
    private boolean negation = false;
    
    /**
     * In a sentence we limit the number of PrepositionalPhrase's there can be 
     * to only two and only one 'with' and one 'for'.
     * This seems reasonable since sentences with three or more either 
     * sounds odd or are quite complex
     */
    private PrepositionalPhrase withPP;
    private PrepositionalPhrase forPP;
    
    private int numberOfPeople;
    
    public String findVerb(VerbPhrase vp) {
        while(vp.getLeft()instanceof VerbPhrase) {
            vp = (VerbPhrase)vp.getLeft();
        }
        return ((Verb)vp.getLeft()).getVerb();
    }
    public String findNoun(NounPhrase np) {
        if(np.getLeft()instanceof Pronoun) {
            return ((Pronoun)np.getLeft()).getWord();
        } else if(np.getLeft()instanceof Noun) {
            return ((Noun)np.getLeft()).getNoun();
        } else if(np.getLeft()instanceof Article) {
            Article a = (Article)np.getLeft();
            if(a.getArticle().toLowerCase().equals("no")) {
                negation = true;
            }
            if(np.getRight()instanceof Noun) {
                return ((Noun)np.getRight()).getNoun();
            } else if(np.getRight()instanceof NounPhrase) {
                return findNoun((NounPhrase)np.getRight());
            }
        } else if(np.getLeft()instanceof Adjective) {
            return ((Noun)np.getRight()).getNoun();
        } else if(np.getLeft()instanceof Digit) {
            int a = ((Digit)np.getLeft()).getDigit();
            return Integer.toString(a);
        } else if(np.getLeft()instanceof NounPhrase) {
            findNoun((NounPhrase)np.getLeft());
        }
        return "Fail";
    }
    /**
     * We want to find the direct Object of the Verb Phrase.
     * 
     * We do this by going down throught the Verb Phrase, 
     * constantly checking the current nodes left childs left child. 
     * If this is a verb that means we've reached the end of the VerbPhrase.
     * By looking at the grammar definition for we see that if there is 
     * a noun in the VerbPhrase there has to be a NounPhrase to the right.
     * 
     * 
     * @param vp The Verb Phrase in which we want to find a noun
     * @return The result of findNoun
     */
    public String findNinV(VerbPhrase vp) {
        VerbPhrase parent = vp;
        while(((VerbPhrase)vp.getLeft()).getLeft()instanceof VerbPhrase) {
            parent = vp;
            vp = (VerbPhrase)vp.getLeft();
            
            if(parent.getRight() instanceof PrepositionalPhrase) {
                // Check which preposition
                PrepositionalPhrase tmp = (PrepositionalPhrase)parent.getRight();
                Preposition left = (Preposition)tmp.getLeft();
                NounPhrase right = (NounPhrase)tmp.getRight();

                if(left.getPreposition().toLowerCase().equals("with")) {
                    withPP = tmp;
                } else if(left.getPreposition().toLowerCase().equals("for")) {
                    forPP = tmp;
                } else if(left.getPreposition().toLowerCase().equals("like")) {
                    
                }
            }            
        }       
        
        if(vp.getRight() instanceof NounPhrase) {
            return findNoun((NounPhrase)vp.getRight());
        } else if(vp.getRight() instanceof PrepositionalPhrase) {
           return findNinPP((PrepositionalPhrase)vp.getRight());
        } else if(vp.getRight() instanceof Adverb) {
            negation = true;
            return findNoun((NounPhrase)parent.getRight());
        } else {
            return "No NP";
        }
    }
    public String findNinPP(PrepositionalPhrase pp) {
        return findNoun((NounPhrase)pp.getRight());
    }
    
    
    public LinkedList<String[]> parser(LinkedList<Object> sentence) {
        semSentences.clear();
        withPP = null;
        forPP = null;
        String[] stat = new String[3];
        Object o = null;
        for (int i = 0; i < sentence.size(); i++) {
            o = sentence.get(i);
            if(o instanceof VerbPhrase) {
                stat[0] = findVerb((VerbPhrase)o);
                stat[2] = findNinV((VerbPhrase)o);
                if(negation) { 
                    stat[2] = "not("+stat[2]+")";
                }
                negation = false;
                
                if(stat[0].toLowerCase().equals("would")) {
                    // replace with want
                    stat[0] = "want";
                }
                
                
                if(withPP != null) {
                    String[] tmp = new String[3];
                    tmp[0] = stat[0];
                    tmp[1] = stat[1];
                    tmp[2] = findNoun((NounPhrase)withPP.getRight());
                    semSentences.add(tmp);
                }
                
                if(forPP != null) {
                    numberOfPeople = Integer.parseInt(findNoun((NounPhrase)forPP.getRight()));
                }
                
                //System.out.println(findNoun((NounPhrase)withPP.getRight()));
                
            } else if(o instanceof Conjunction) {
                // uhm, maybe do something...
                semSentences.add(stat);
                stat = new String[3];
            } else if(o instanceof NounPhrase) {
                
                if(i < sentence.size()-1) {
                    // Not last Phrase in sentence 
                } else if(i == sentence.size()-1) {
                    /* Last Phrase in sentence
                     * Since we cannot have Noun Phrase by themselves 
                     * we can assume this is refering back to a previous 
                     * Verb Phrase or is an answer to a question
                     */
                    stat[0] = semSentences.getLast()[0];
                    stat[1] = semSentences.getLast()[1];
                    
                    String tmp = findNoun((NounPhrase)o);
                    
                    stat[2] = (negation ? "not("+tmp+")" : tmp);
                } else {
                    // Something is wrong
                }
                
                
                if(stat[0] == null) {
                    // no verb found, this should then be the subject
                    stat[1] = findNoun((NounPhrase)o);
                }
            }
        }
        semSentences.add(stat);
        for (int i = 0; i < semSentences.size(); i++) {
            String a = ((String[])semSentences.get(i))[0];
            String b = ((String[])semSentences.get(i))[1];
            String c = ((String[])semSentences.get(i))[2];
            //System.out.println(a+"("+b+","+c+")");
        }
        //System.out.println("Number of people: "+numberOfPeople);
        
        return semSentences;

    }
}