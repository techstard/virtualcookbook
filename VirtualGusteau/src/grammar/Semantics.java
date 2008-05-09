package grammar;
import java.util.*;
import virtualgusteau.*;

public class Semantics {
    
    /**
     * Not Working:
     * We will be 20 people dining today
     * The dish should not contain any milk products
     */
    
    public Semantics() {
        semSentences = new LinkedList<Object>();
    }
    /*
     * NP → Pronoun         I
     *    | Noun            dog
     *    | Article Noun    the + dog
     *    | Article NP      the + dog on the log
     *    | Adjective Noun  yellow + submarine
     *    | Digit           4
     *    | NP PP           the dog + on the log
     *    | Modal NP        can + the dog
     * 
     * 
     * VP → Verb            stinks
     *    | VP NP           feel + a breeze
     *    | VP Adjective    is + smelly
     *    | VP PP           turn + to the east
     *    | VP Adverb       go + ahead
     *    | VP Gerund       want a pie + containing
     *    | Modal PP        would + like
     */
    
    
    private String object = null;
    private String action = null;
    private String subject = null;
    
    private String[] atomic = new String[3];
    
    private LinkedList<Object> semSentences;
    
    private boolean negation = false;

    public int getNumberOfPeople() {
        return numberOfPeople;
    }
    
    /**
     * In a sentence we limit the number of PrepositionalPhrase's there can be 
     * to only two and only one 'with' and one 'for'.
     * This seems reasonable since sentences with three or more either 
     * sounds odd or are quite complex
     */
    private PrepositionalPhrase withPP;
    private PrepositionalPhrase forPP;
    private VerbPhrase gerundPhrase;
    
    private int numberOfPeople;
    
    public String findVerb(VerbPhrase vp) {
        while(vp.getLeft() instanceof VerbPhrase) {
            vp = (VerbPhrase)vp.getLeft();
        }
        if(vp.getLeft() instanceof Modal) {
            if(vp.getRight() instanceof VerbPhrase) {
                 return findVerb((VerbPhrase)vp.getRight());
            } else if(vp.getRight() instanceof PrepositionalPhrase) {
                /** 
                 * Not sure about this!
                 * As far as I know when using, e.g, the modal "would" 
                 * you leave out the verb which means there is no "action". 
                 * But can it be so that in these cases the preposition 
                 * can serve as the "action"?
                 */
                return ((Preposition)((PrepositionalPhrase)vp.getRight()).getLeft()).getPreposition();
            } else {
                return "Error - findVerb - incorrect use of Modal";
            }
        } else if(vp.getLeft() instanceof Verb) {
            return ((Verb)vp.getLeft()).getVerb();
        } else {
            return "Error - findVerb - incorrect use of Verb Phrase";
        }        
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
            
            if(np.getRight() instanceof Adjective) {
                numberOfPeople = ((Digit)np.getLeft()).getDigit();
                
            }
            
            int a = ((Digit)np.getLeft()).getDigit();
            return Integer.toString(a);
        } else if(np.getLeft()instanceof NounPhrase) {
            return findNoun((NounPhrase)np.getLeft());
        }
        return "Fail";
    }
    /**
     * 
     * 
     * @param vp The Verb Phrase in which we want to find a noun
     * @return The result of findNoun
     */
    private VerbPhrase parent;
    public String findNinV(VerbPhrase vp) {
        /**
         * Check if the input VP's left child is a Verb, if so 
         * we cannot do traverse through the tree
         */
        if(vp.getLeft() instanceof Verb) {
            /**
             * The Verbs sibling is a NounPhrase and should therefor
             * contain a noun or a digit. We must therefor extract it
             */
            if(parent.getRight() instanceof NounPhrase) {
                return findNoun((NounPhrase)parent.getRight());
            } else if(parent.getRight() instanceof Adjective) {
                /**
                 * Verbs sibling is an Adjective, can also be seen
                 * as a target
                 */
                return ((Adjective)parent.getRight()).getAdjective();
            } else if(parent.getRight() instanceof Adverb) {
                /**
                 * Verbs sibling is an Adverb, can probably also 
                 * be considered as a target but we're stretching 
                 * the analogy pretty thin
                 * 
                 * If the Advers is "not" that means we have a 
                 * negation of the target
                 */
                String adverb = ((Adverb)parent.getRight()).getAdverb();
                if(adverb.toLowerCase().equals("not")) {
                    return findNoun((NounPhrase)parent.getRight());
                } else {
                    return ((Adverb)parent.getRight()).getAdverb();
                }                
            } else if(parent.getRight() instanceof Gerund) {
                /**
                 * Verbs sibling is a Gerund, as with Adverb, 
                 * this is stretching the analogy
                 */
                return ((Gerund)parent.getRight()).getGerund();
            } else {
                return "Error - findNinV - Verb";
            }
        } else if(vp.getLeft() instanceof Modal) {
            if(vp.getRight() instanceof VerbPhrase) {
                return findNinV((VerbPhrase)vp.getRight());
            } else if(vp.getRight() instanceof PrepositionalPhrase) {
                return findNinPP((PrepositionalPhrase)vp.getRight());
            } else {
                return "Error - findNinV - incorrect use of Modal";
            }
        } else if(vp.getLeft() instanceof VerbPhrase) {
            parent = vp;
            if(vp.getRight() instanceof PrepositionalPhrase) {
                PrepositionalPhrase tmp = (PrepositionalPhrase)vp.getRight();
                Preposition left = (Preposition)tmp.getLeft();
                NounPhrase right = (NounPhrase)tmp.getRight();

                if(left.getPreposition().toLowerCase().equals("with")) {
                    withPP = tmp;
                } else if(left.getPreposition().toLowerCase().equals("for")) {
                    forPP = tmp;
                }
            } else if(vp.getRight() instanceof Gerund) {
                Gerund tmp = (Gerund)vp.getRight();
                if(tmp.getGerund().toLowerCase().equals("containing")) {
                    gerundPhrase = vp;
                }
            }            
            return findNinV((VerbPhrase)vp.getLeft());
        } else {
            return "Error - findNinV";
        }        
    }
    public String findNinPP(PrepositionalPhrase pp) {
        return findNoun((NounPhrase)pp.getRight());
    }
    
    
    public LinkedList<Object> parser(LinkedList<Object> sentence) {
        semSentences.clear();
        withPP = null;
        forPP = null;
        Object o = null;
        numberOfPeople = 0;
        negation = false;
        parent = null;
        
        if(sentence.size() == 1) {
            /* One phrase sentence
             */
            
            if(sentence.getLast() instanceof NounPhrase) {
                if(((NounPhrase)sentence.getLast()).getLeft() instanceof Digit) {
                    /**
                     * Probably and answer to a question
                     */
                    numberOfPeople = ((Digit)((NounPhrase)sentence.getLast()).getLeft()).getDigit();
                } else {
                    //stat[1] = findNoun((NounPhrase)sentence.getLast());
                }
                if(((NounPhrase)sentence.getLast()).getRight() instanceof PrepositionalPhrase) {
                    NounPhrase np = (NounPhrase)sentence.getLast();
                    PrepositionalPhrase pp = (PrepositionalPhrase)np.getRight();
                    Preposition p = (Preposition)pp.getLeft();
                    String ob = (p.toString()).substring((p.toString()).indexOf(":")+1);
                    String t = findNinPP(pp);
                    
                    semSentences.add(new Action(ob, new Target(t)));
                }
            }
        } else {
            for (int i = 0; i < sentence.size(); i++) {
                o = sentence.get(i);
                if(o instanceof VerbPhrase) {
                    String action = findVerb((VerbPhrase)o);
                    String target = findNinV((VerbPhrase)o);
                    
                    semSentences.add(new Action(action, new Target(target)));
                    
                    if(negation) { 
                        ((Action)semSentences.getLast()).setNegation(negation);
                    }
                    negation = false;

                    if(((Action)semSentences.getLast()).getName().toLowerCase().equals("would")) {
                        ((Action)semSentences.getLast()).setName("want");
                    }


                    if(withPP != null) {                        
                        Target t = new Target(findNoun((NounPhrase)withPP.getRight()));
                        
                        ((Target)((Action)semSentences.getLast()).getTarget()).setSubTarget(t);
                    }

                    if(forPP != null) {
                        numberOfPeople = Integer.parseInt(findNoun((NounPhrase)forPP.getRight()));
                    }
                    
                    if(gerundPhrase != null) {
                        VerbPhrase tmp = (VerbPhrase)o;
                        while(!gerundPhrase.equals((VerbPhrase)tmp.getLeft())) {
                            tmp = (VerbPhrase)tmp.getLeft();
                        }
                        Target t = ((Action)semSentences.getLast()).getTarget();
                        t.setSubTarget(new Target(findNoun((NounPhrase)tmp.getRight())));
                    }

                    //System.out.println(findNoun((NounPhrase)withPP.getRight()));

                } else if(o instanceof Conjunction) {
                    // uhm, maybe do something...
                    
                } else if(o instanceof NounPhrase) {

                    if(i < sentence.size()-1) {
                        // Not last Phrase in sentence 
                    } else if(i == sentence.size()-1) {
                        
                        NounPhrase np = (NounPhrase)o;
                        if(np.getLeft() instanceof Modal) {
                            if(np.getRight() instanceof PrepositionalPhrase) {
                                String prep = ((Preposition)((PrepositionalPhrase)
                                        np.getRight()).getLeft()).getPreposition();
                                String obj = findNinPP((PrepositionalPhrase)np.getRight());
                                semSentences.add(new Action(prep, new Target(obj)));
                            } else if(np.getRight() instanceof NounPhrase) {
                                findNoun((NounPhrase)np.getRight());
                            } 
                        } else {
                        
                            /* Last Phrase in sentence
                             * Since we cannot have Noun Phrase by themselves 
                             * we can assume this is refering back to a previous 
                             * Verb Phrase or is an answer to a question
                             */
                            String name = ((Action)semSentences.getLast()).getName();
                            String t = findNoun((NounPhrase)o);
                            boolean n = ((Action)semSentences.getLast()).isNegation();
                            semSentences.add(new Action(name, new Target(t)));
                            if(n) {
                                ((Action)semSentences.getLast()).setNegation(n);
                            } else {
                                if(negation) {
                                    ((Action)semSentences.getLast()).setNegation(negation);
                                }
                            }
                        }                        
                    } else {
                        // Something is wrong
                    }
                }
            }
        }
        
        
        /*semSentences.add(stat);
        for (int i = 0; i < semSentences.size(); i++) {
            String a = ((String[])semSentences.get(i))[0];
            String b = ((String[])semSentences.get(i))[1];
            String c = ((String[])semSentences.get(i))[2];
            //System.out.println(a+"("+b+","+c+")");
        }
        //System.out.println("Number of people: "+numberOfPeople);*/
        
        return semSentences;
    }
}