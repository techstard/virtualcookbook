package grammar;
import java.util.*;
import virtualgusteau.*;


public class NS {
    private LinkedList sentenceTree;
    private LinkedList logicSentences;
    
    private LinkedList actions;
    private LinkedList subjects;
    private LinkedList objects;
        
    private LinkedList<PrepositionalPhrase> nounSpecifiers;
    
    public NS() {
        logicSentences = new LinkedList();
        actions = new LinkedList();
        subjects = new LinkedList();
        objects = new LinkedList();
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
     * @author Robert Krantz     * 
     * @param sentenceTree The input sentence represented by various
     * grammatical objects forming a tree structure
     * @since 2008-05-13
     * @throws Exception
     * @see #findNoun
     * @see #findVerb
     * @see #createLogic
     * @see #conjunctions
     * 
     */
    public void parser(LinkedList sentenceTree) throws Exception {
        if(sentenceTree.size() == 1) {
            findNoun((NounPhrase)sentenceTree.get(0));
        } else if(sentenceTree.size() == 2) {
            findNoun((NounPhrase)sentenceTree.get(0));
            subjects.addLast(objects.pollLast());            
            findVerb((VerbPhrase)sentenceTree.get(1));
            createLogic();
        } else if(sentenceTree.size() > 3) {
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
    }
    /**
     * @author Robert Krantz
     * @throws Exception
     * @since 2008-05-13
     * @see
     */
    public void createLogic() throws Exception {
        String action = null;
        boolean negation = false;
        if(!actions.isEmpty()) {
            Object o = actions.getFirst();
            if(o instanceof Boolean) {
                negation = (Boolean)o;
                Object next = actions.getLast();
                if(next instanceof Verb) {
                    action = ((Verb)next).getVerb();
                }
            } else if(o instanceof Verb) {
                action = ((Verb)o).getVerb();
            } else if(o instanceof Preposition) {
                action = ((Preposition)o).getPreposition();
            } else if(o instanceof Gerund) {
                action = ((Gerund)o).getGerund();
            }
        } else {
            // This means there is no action and most likely
            // a list of Nouns
        }
        
        if(!subjects.isEmpty()) {
            // extract the subject
        } else {
            // No subject means either a list of objects or 
            // a syntax error that has gotten through
        }
        
        if(!objects.isEmpty()) {
            
        } else {
            // The sentence might actually 
        }
        
        System.out.println("Action: "+action);
        System.out.println("Negation: "+negation);
        
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
                subjects.addLast(objects.pollLast());
                findVerb((VerbPhrase)o);
            } else if(o instanceof Conjunction) {
                /* finilize this part of the sentence 
                 * by calling createLogic
                 */
                createLogic();
            }
        }
        /* You need to call createLogic here as well, 
         * otherwise the last part of the sentence 
         * wont be included
         */
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
                actions.addLast(vp.getLeft());
            }
        } else if(vp.getLeft() instanceof AdverbPhrase) {
            AdverbPhrase ap = (AdverbPhrase)vp.getLeft();
            if(adverbisNegation(ap)) {
                actions.addLast(true);
            }
            // This is the verb I'm looking for
            actions.addLast(vp.getRight());
        } else if(vp.getLeft() instanceof Modal) {
            if(vp.getRight() instanceof VerbPhrase) {
                findVerb((VerbPhrase)vp.getRight());
            } else if(vp.getRight() instanceof PrepositionalPhrase) {
                PrepositionalPhrase pp = (PrepositionalPhrase)vp.getRight();
                if(pp.getRight() instanceof NounPhrase) {
                    findNoun((NounPhrase)pp.getRight());
                }
                // This is probably the action I'm looking for
                actions.addLast(pp.getLeft());
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
            objects.addLast((Noun)np.getLeft());
        } else if(np.getLeft() instanceof Article) {
            Article a = (Article)np.getLeft();
            if(a.getArticle().toLowerCase().equals("no")) {
                actions.addLast(true);
            }
            if(np.getRight() instanceof NounPhrase) {
                findNoun((NounPhrase)np.getRight());
            } else {
                objects.addLast((Noun)np.getRight());
            }
        } else if(np.getLeft() instanceof AdjectivePhrase) {
            if(np.getRight() instanceof Noun) {
                objects.addLast((Noun)np.getLeft());
            } else {
                adjectiveisObject((AdjectivePhrase)np.getLeft());
            }
        } else if(np.getLeft() instanceof Pronoun) {
            objects.addLast((Pronoun)np.getLeft());
        } else if(np.getLeft() instanceof Digit) {
            objects.addLast(np.getLeft());
            if(np.getRight() instanceof Noun) {
                objects.addLast(np.getRight());
            }
        } else if(np.getLeft() instanceof Gerund) {
            if(np.getRight() instanceof Noun) {
                // Not sure what this means
            } else {
                // This is what is being done
                actions.addLast(np.getLeft());
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
     * Checks if the inputed AdverbPhrase is a negation or not
     * 
     * @author Robert Krantz
     * @param ap The AdverbPhrase to be checked
     * @return True - Adverb is negation<br>
     *          False - Adverb is not negation
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