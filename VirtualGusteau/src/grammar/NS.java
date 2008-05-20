package grammar;
import java.util.*;
import virtualgusteau.*;


public class NS {
    private LinkedList sentenceTree;
    private LinkedList logicSentences;
    
    private Object action;
    private Object subject;
    private Object object;
    private Object secondaryObject;
    private boolean negation;
        
    private String objectName = null;
    private int numberOfPeople = 0;
    
    private LinkedList<PrepositionalPhrase> nounSpecifiers;
    
    public NS() {
        logicSentences = new LinkedList();
        nounSpecifiers = new LinkedList<PrepositionalPhrase>();
    }
    /**
     * The input sentence will have the following configuration: <br>
     * 
     * NP <br>
     * NP VP  <br>
     * NP CC NP ...  <br>
     * NP VP CC NP ...  <br>
     * NP VP CC NP VP ...  <br>
     * 
     * @author Robert Krantz
     * @param sentenceTree The input sentence represented by various
     * grammatical objects forming a tree structure
     * @return A LinkedList with logical representations of the input sentence
     * @since 2008-05-13
     * @throws Exception
     * @see #findNoun
     * @see #findVerb
     * @see #createLogic
     * @see #conjunctions
     * 
     */
    public LinkedList parser(LinkedList sentenceTree) throws Exception {
        this.sentenceTree = sentenceTree;
        if(sentenceTree.size() == 1) {
            Object o = sentenceTree.getFirst();
            if(o instanceof NounPhrase) {
                findNoun((NounPhrase)o);
            } else if(o instanceof VerbPhrase) {
                findVerb((VerbPhrase)o);
            }       
            createLogic();
        } else if(sentenceTree.size() == 2) {
            findNoun((NounPhrase)sentenceTree.get(0));
            subject = object; object = null;
            findVerb((VerbPhrase)sentenceTree.get(1));
            createLogic();
        } else if(sentenceTree.size() > 2) {
            conjunctions();
        }
//        System.out.println("Actions:");
//        for(int i = 0; i < actions.size(); i++) {
//            System.out.print(actions.get(i)+" ");
//        }
//        System.out.println("\n---------------------------");
//        System.out.println("Subjects:");
//        for(int i = 0; i < subjects.size(); i++) {
//            System.out.print(subjects.get(i)+" ");
//        }
//        System.out.println("\n---------------------------");
//        System.out.println("Objects:");
//        for(int i = 0; i < objects.size(); i++) {
//            System.out.print(objects.get(i)+" ");
//        }
//        System.out.println("\n---------------------------");
//        System.out.println("NounSpecifiers:");
//        for(int i = 0; i < nounSpecifiers.size(); i++) {
//            System.out.print(nounSpecifiers.get(i)+" ");
//        }
//        System.out.println("\n---------------------------");
        
//        for(int i = 0; i < logicSentences.size(); i++) {
//            Object o = logicSentences.get(i);
//            if(o instanceof Action) {
//                System.out.println("Action: "+((Action)logicSentences.get(i)).getName());
//                System.out.println("Negation: "+((Action)logicSentences.get(i)).isNegation());
//                System.out.println("Target: "+((Action)logicSentences.get(i)).getTarget());
//                System.out.println("Number of People: "+((Action)logicSentences.get(i)).getNumberOfPeople());
//            } else {
//                System.out.println("Target: "+((Target)logicSentences.get(i)).getName());
//                System.out.println("Negation: "+((Target)logicSentences.get(i)).isNegation());
//                System.out.println("Number of People: "+((Target)logicSentences.get(i)).getNumberOfPeople());
//            }            
//            System.out.println("\n---------------------------");
//        }
        return logicSentences;
    }
    /**
     * @author Robert Krantz
     * @throws Exception
     * @since 2008-05-13
     * @see
     */
    public void createLogic() throws Exception {
        String actionWord = null;
        //boolean neg = false;
        if(action != null) {
            Object o = action;
            if(negation) {
                if(o instanceof Verb) {
                    actionWord = ((Verb)o).getVerb();
                }
            } else if(o instanceof Verb) {
                actionWord = ((Verb)o).getVerb();
            } else if(o instanceof Preposition) {
                actionWord = ((Preposition)o).getPreposition();
            } else if(o instanceof Gerund) {
                actionWord = ((Gerund)o).getGerund();
            }
        } else {
            // This means there is no actionWord and most likely
            // a list of Nouns
        }
        
        if(subject != null) {
            // extract the subject
        } else {
            // No subject means either a list of objects or 
            // a syntax error that has gotten through
        }
        numberOfPeople = 0;
        
        extractObject();        
        
        if(actionWord != null) {
            logicSentences.addLast(new Action(actionWord));
        } else {
            // No actionWord
        }
        if(objectName != null) {
            if(actionWord == null) {
                // no action
                logicSentences.add(new Target(objectName));
                ((Target)logicSentences.getLast()).setNegation(negation);
            } else {
                ((Action)logicSentences.getLast()).setTarget(
                    new Target(objectName));
                ((Action)logicSentences.getLast()).setNegation(negation);
            }            
        } else {
            // No objectName
        }        
        
        // Extract information from nounSpecifiers
        for(int i = 0; i < nounSpecifiers.size(); i++) {
            object = null;
            PrepositionalPhrase pp = nounSpecifiers.get(i);
            Preposition p = (Preposition)pp.getLeft();
            
            if(p.getPreposition().toLowerCase().equals("with")) {
                findNoun((NounPhrase)pp.getRight());
                Object o = object;
                if(o instanceof Noun) {
                    Object last = logicSentences.getLast();
                    if(last instanceof Action) {
                        ((Action)logicSentences.getLast()).getTarget().setSubTarget(new Target(
                            ((Noun)o).getNoun()));
                    } else {
                        ((Target)logicSentences.getLast()).setSubTarget(new Target(
                            ((Noun)o).getNoun()));
                    }
                } else if(o instanceof Digit) {
                    Object last = logicSentences.getLast();
                    Object next = object;
                    if(last instanceof Action) {
                        ((Action)logicSentences.getLast()).getTarget().setSubTarget(new Target(
                            ((Noun)next).getNoun()));
                    } else {
                        ((Target)logicSentences.getLast()).setSubTarget(new Target(
                            ((Noun)next).getNoun()));
                    }
                    
                    
                    
                }
                
            } else if(p.getPreposition().toLowerCase().equals("for")) {
                findNoun((NounPhrase)pp.getRight());
                extractObject();
            } else {
            }
        }
        if(logicSentences.getLast() instanceof Action) {
            ((Action)logicSentences.getLast()).setNumberOfPeople(numberOfPeople);
        } else {
            ((Target)logicSentences.getLast()).setNumberOfPeople(numberOfPeople);
        }
        
    }
    /**
     * Extracts the correct information from object and secondary object
     * 
     * @author Robert Krantz
     * @since 2008-05-13
     */
    public void extractObject() {
        if(object != null) {
            if(secondaryObject != null) {
                // more than one objectName
                // can be '# objectName'
                Object first = object;
                Object second = secondaryObject;
                if(first instanceof Digit) {
                    numberOfPeople = ((Digit)first).getDigit();
                } else {
                    // Not sure what to do
                }
                if(second instanceof Noun) {
                    objectName = ((Noun)second).getNoun();
                } else {
                    // Not sure what to do
                }
            } else {
                Object o = object;
                if(o instanceof Noun) {
                    objectName = ((Noun)o).getNoun();
                } else if(o instanceof Digit) {
                    numberOfPeople = ((Digit)o).getDigit();
                } else if(o instanceof Adjective) {
                    objectName = ((Adjective)o).getAdjective();
                }
            }
        } else {
            // The sentence might have a gerund as noun
        }
    }
    /**
     * When a sentence contains one or more conjunctions (and, or, but) they
     * will act as seperators for several smaller subsentences. The function will 
     * traverse the LinkedList sentenceTree and call appropriate functions
     * depending on what it finds. If it finds a conjunction it will call 
     * createLogic to finilize the subsentence preceeding the conjuntion. <br>
     * 
     * @author Robert Krantz
     * @since 2008-05-13
     * @see #createLogic
     * @see #findNoun
     * @see #findVerb
     * 
     * @throws java.lang.Exception
     */
    public void conjunctions() throws Exception {
        for(int i = 0; i < sentenceTree.size(); i++) {
            Object o = sentenceTree.get(i);
            if(o instanceof NounPhrase) {
                findNoun((NounPhrase)o);
            } else if(o instanceof VerbPhrase) {
                subject = object; object = null;
                findVerb((VerbPhrase)o);
            } else if(o instanceof Conjunction) {
                /* finilize this part of the sentence 
                 * by calling createLogic
                 */
                createLogic();
                nounSpecifiers.clear();
            }
        }
        /* You need to call createLogic here as well, 
         * otherwise the last part of the sentence 
         * wont be included
         */
        createLogic();
    }
    /**
     * Locates the main verb of a VerbPhrase
     * 
     * @author Robert Krantz
     * @param vp The VerbPhrase in which the main verb needs to be found
     * @throws java.lang.Exception
     * @since 2008-05-13
     * @see #findNoun
     * @see #adverbisNegation
     */
    public void findVerb(VerbPhrase vp) throws Exception {
        while(vp.getLeft() instanceof VerbPhrase) {
            if(vp.getRight() instanceof PrepositionalPhrase) {
                nounSpecifiers.addLast((PrepositionalPhrase)vp.getRight());
            } else if(vp.getRight() instanceof NounPhrase) {
                findNoun((NounPhrase)vp.getRight());
            }
            vp = (VerbPhrase)vp.getLeft();
        }
        if(vp.getLeft() instanceof Verb) {
            if(vp.getRight() instanceof VerbPhrase) {
                findVerb((VerbPhrase)vp.getRight());
            } else {
                if(vp.getRight() instanceof NounPhrase) {
                    findNoun((NounPhrase)vp.getRight());
                }
                // This is the verb I'm looking for
                action = vp.getLeft();
            }
        } else if(vp.getLeft() instanceof AdverbPhrase) {
            AdverbPhrase ap = (AdverbPhrase)vp.getLeft();
            negation = adverbisNegation(ap);
            // This is the verb I'm looking for
            action = vp.getRight();
        } else if(vp.getLeft() instanceof Modal) {
            if(vp.getRight() instanceof VerbPhrase) {
                findVerb((VerbPhrase)vp.getRight());
            } else if(vp.getRight() instanceof PrepositionalPhrase) {
                PrepositionalPhrase pp = (PrepositionalPhrase)vp.getRight();
                if(pp.getRight() instanceof NounPhrase) {
                    findNoun((NounPhrase)pp.getRight());
                }
                // This is probably the actionWord I'm looking for
                action = pp.getLeft();
            }
        }
    }
    /**
     * Locates the head noun of the inputed NounPhrase <br>
     * <br>
     * If there is only an AdjectivePhrase it calls adjectiveisObject
     * 
     * @author Robert Krantz
     * @param np The NounPhrase in which the main noun needs to be found
     * @throws java.lang.Exception
     * @since 2008-05-13
     * @see #adjectiveisObject
     */
    public void findNoun(NounPhrase np) throws Exception {
        while(np.getLeft() instanceof NounPhrase) {
            if(np.getRight() instanceof PrepositionalPhrase) {
                nounSpecifiers.addLast((PrepositionalPhrase)np.getRight());
            }
            np = (NounPhrase)np.getLeft();
        }
        if(np.getLeft() instanceof Noun) {
            object = (Noun)np.getLeft();
        } else if(np.getLeft() instanceof Article) {
            Article a = (Article)np.getLeft();
            if(a.getArticle().toLowerCase().equals("no")) {
                negation = true;
            }
            if(np.getRight() instanceof NounPhrase) {
                findNoun((NounPhrase)np.getRight());
            } else {
                object = (Noun)np.getRight();
            }
        } else if(np.getLeft() instanceof AdjectivePhrase) {
            if(np.getRight() instanceof Noun) {
                object = (Noun)np.getRight();
            } else {
                object = adjectiveisObject((AdjectivePhrase)np.getLeft());
            }
        } else if(np.getLeft() instanceof Pronoun) {
            String word = ((Pronoun)np.getLeft()).getWord();
            if(word.toLowerCase().equals("something") ||
                    word.toLowerCase().equals("anything")) {
                object = new Noun(word);
            } else {
                object = np.getLeft();
            }
        } else if(np.getLeft() instanceof Digit) {
            object = np.getLeft();
            if(np.getRight() instanceof Noun) {
                secondaryObject = np.getRight();
            }
        } else if(np.getLeft() instanceof Gerund) {
            if(np.getRight() instanceof Noun) {
                // Not sure what this means
            } else {
                // This is what is being done
                action = np.getLeft();
            }
        }
    }
    /**
     * Locates the main Adjective in the inputed AdjectivePhrase
     * 
     * @author Robert Krantz
     * @param ap The AdjectivePhrase in which the main Adjective needs 
     * to be found
     * @return The main Adjective of the AdjectivePhrase ap
     * @throws java.lang.Exception
     * @since 2008-05-13
     * @see
     */
    public Adjective adjectiveisObject(AdjectivePhrase ap) throws Exception {
        while(ap.getRight() instanceof AdjectivePhrase) {
            ap.getRight();
            adjectiveisObject((AdjectivePhrase)ap);
        }
        if(ap.getLeft() instanceof Adjective) {
            return (Adjective)ap.getLeft();
        } else if(ap.getRight() instanceof Adjective) {
            return (Adjective)ap.getRight();
        } else {
            throw new Exception("Syntax error - Adjective");
        }
    }
    /**
     * Checks if the inputed AdverbPhrase is a neg or not
     * 
     * @author Robert Krantz
     * @param ap The AdverbPhrase to be checked
     * @return True - Adverb is neg<br>
     *          False - Adverb is not neg
     * @throws java.lang.Exception
     * @since 2008-05-13
     */
    public boolean adverbisNegation(AdverbPhrase ap) {
        if(ap.getRight() instanceof AdverbPhrase) {
            if(((Adverb)ap.getLeft()).getAdverb().toLowerCase().equals("not")) {
                return true;
            }
            adverbisNegation((AdverbPhrase)ap.getRight());
        } else {
           if(((Adverb)ap.getLeft()).getAdverb().toLowerCase().equals("not")) {
                return true;
            } 
        }        
        return false;
    }
}