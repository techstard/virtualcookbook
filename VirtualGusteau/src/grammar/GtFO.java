package grammar;
import java.util.*;
/**
 *
 * @author rkrantz
 */


/**
 * S → NounPhrase VerbPhrase
 *   | S Conjunction S
 * 
 * NounPhrase → Pronoun                 → PRP
 *    | Noun                    → NN
 *    | Article Noun            → DT NN
 *    | Adjective Noun          → Adjective NN
 *    | Article NounPhrase              → DT NounPhrase
 *    | Digit                   → Digit
 *    | NounPhrase PrepositionalPhrase
 *    | NounPhrase RelClause
 * 
 * VerbPhrase → Verb
 *    | VerbPhrase NounPhrase
 *    | VerbPhrase Adjective
 *    | VerbPhrase PrepositionalPhrase
 *    | VerbPhrase Adverb
 * 
 * PrepositionalPhrase → Preposition NounPhrase
 * RelClause → that VerbPhrase
 */

public class GtFO {
    private String object = null;
    private String action = null;
    private String subject = null;
    
    private String[] atomic = new String[3];
    
    
    /**
     * In a sentence we limit the number of PrepositionalPhrase's there can be 
     * to only two and only one 'with' and one 'for'.
     * This seems reasonable since sentences with three or more either 
     * sounds odd or are quite complex
     */
    private PrepositionalPhrase withPP;
    private PrepositionalPhrase forPP;
    
    private int numberOfPeople;
    
    public void rec(Object o) {
        if(o instanceof NounPhrase) {
            
            if(((NounPhrase)o).getLeft()instanceof Pronoun) {
                
                String tmp = ((Pronoun)((NounPhrase)o).getLeft()).getWord();
                System.out.println(tmp);
                
            } else if(((NounPhrase)o).getLeft() instanceof Noun) {
                
                String tmp = ((Noun)((NounPhrase)o).getLeft()).getNoun();
                System.out.println(tmp);
                
            } else if(((NounPhrase)o).getLeft() instanceof Article) {
                
                if(((NounPhrase)o).getRight() instanceof Noun) {
                                        
                    String tmp = ((Noun)((NounPhrase)o).getRight()).getNoun();
                    System.out.println(tmp);
                    
                } else if(((NounPhrase)o).getRight() instanceof NounPhrase) {
                    rec(((NounPhrase)o).getRight());
                }
            } else if(((NounPhrase)o).getLeft() instanceof Digit) {
                
                System.out.println(((Digit)((NounPhrase)o).getLeft()).getDigit());
                
            } else if(((NounPhrase)o).getLeft() instanceof NounPhrase) {
                
                rec(((NounPhrase)o).getLeft());
                
                if(((NounPhrase)o).getRight() instanceof PrepositionalPhrase) {
                    rec(((NounPhrase)o).getRight());
                }
            } else if(((NounPhrase)o).getLeft() instanceof Adjective) {
                if(((NounPhrase)o).getRight() instanceof Noun) {
                    String jj_tmp = ((Adjective)((NounPhrase)o).getLeft()).getAdjective();
                    String n_tmp = ((Noun)((NounPhrase)o).getRight()).getNoun();
                    System.out.println(jj_tmp + " " + n_tmp);
                }
            }
        } else if(o instanceof VerbPhrase) {
            if(((VerbPhrase)o).getLeft() instanceof Verb) {                
                System.out.println(((Verb)((VerbPhrase)o).getLeft()).getVerb());
            } else if(((VerbPhrase)o).getLeft() instanceof VerbPhrase) {
                rec(((VerbPhrase)o).getLeft());
                if(((VerbPhrase)o).getRight() instanceof NounPhrase) {
                    rec(((VerbPhrase)o).getRight());
                } else if(((VerbPhrase)o).getRight() instanceof PrepositionalPhrase) {
                    rec(((VerbPhrase)o).getRight());
                } else if(((VerbPhrase)o).getRight() instanceof Adverb) {
                    String tmp = ((Adverb)(((VerbPhrase)o).getRight())).getAdverb();
                    System.out.println(tmp);
                }
            }
        } else if(o instanceof Conjunction) {
            
        } else if(o instanceof PrepositionalPhrase) {
            String prep = ((Preposition)((PrepositionalPhrase)o).getLeft()).getPreposition();
            System.out.println(prep);
            if(((PrepositionalPhrase)o).getRight() instanceof NounPhrase) {
                rec(((PrepositionalPhrase)o).getRight());
            }
        } else if(o instanceof RelClause) {
            System.out.println(((RelClause)o).getLeft());
            rec(((RelClause)o).getRight());
        }
    }
    
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
        } else {
            return "No NP";
        }
    }
    public String findNinPP(PrepositionalPhrase pp) {
        return findNoun((NounPhrase)pp.getRight());
    }
    
    
    public LinkedList<Object> semantics(LinkedList<Object> sentence) {

        LinkedList<Object> list = new LinkedList<Object>();
        String[] stat = new String[3];
        Object o = null;
        for (int i = 0; i < sentence.size(); i++) {
            o = sentence.get(i);
            if(o instanceof VerbPhrase) {
                stat[0] = findVerb((VerbPhrase)o);
                stat[2] = findNinV((VerbPhrase)o);
                
                if(stat[0].toLowerCase().equals("would")) {
                    // replace with want
                    stat[0] = "want";
                }
                
                
                if(withPP != null) {
                    String[] tmp = new String[3];
                    tmp[0] = stat[0];
                    tmp[1] = stat[1];
                    tmp[2] = findNoun((NounPhrase)withPP.getRight());
                    list.add(tmp);
                }
                
                if(forPP != null) {
                    numberOfPeople = Integer.parseInt(findNoun((NounPhrase)forPP.getRight()));
                }
                
                //System.out.println(findNoun((NounPhrase)withPP.getRight()));
                
            } else if(o instanceof Conjunction) {
                // uhm, maybe do something...
                list.add(stat);
                stat = new String[3];
            } else if(o instanceof NounPhrase) {
                if(stat[0] == null) {
                    // no verb found, this should then be the subject
                    stat[1] = findNoun((NounPhrase)o);
                }
            }
        }
        list.add(stat);
        for (int i = 0; i < list.size(); i++) {
            String a = ((String[])list.get(i))[0];
            String b = ((String[])list.get(i))[1];
            String c = ((String[])list.get(i))[2];
            //System.out.println(a+"("+b+","+c+")");
        }
        //System.out.println("Number of people: "+numberOfPeople);
        
        return list;

    }
}
