package grammar;
import java.util.*;
/**
 *
 * @author rkrantz
 */
public class GtFO {
    private String object = null;
    private String action = null;
    private String subject = null;
    
    private String[] atomic = new String[3];
    
    public void converter(LinkedList<Object> sentence) {
        Iterator it = sentence.iterator();
        while(it.hasNext()) {
            Object o = it.next();
            rec(o);
        }
    }
    public void rec(Object o) {
        if(o instanceof NP) {
            /**
             * Pronoun
             * Noun
             * Article Noun
             * Digit
             * NP PP
             */
            if(((NP)o).getLeft()instanceof PN) {
                
                String tmp = ((PN)((NP)o).getLeft()).getWord();
                System.out.println(tmp);
                
                if(atomic[0] == null) {
                    /* This would mean that we haven't come across
                     * a verb, assume therefor that the PN is subject
                     */
                    subject = tmp;
                } else {
                    /* There is a verb there so the PN can be assumed to 
                     * be the object
                     */
                    object = tmp;
                }
                
            } else if(((NP)o).getLeft() instanceof N) {
                
                String tmp = ((N)((NP)o).getLeft()).getNoun();
                System.out.println(tmp);
                
                if(atomic[0] == null) {
                    /* This would mean that we haven't come across
                     * a verb, assume therefor that the N is subject
                     */
                    subject = tmp;
                } else {
                    /* There is a verb there so the N can be assumed to 
                     * be the object
                     */
                    object = tmp;
                }
                
            } else if(((NP)o).getLeft() instanceof A) {
                
                if(((NP)o).getRight() instanceof N) {
                                        
                    String tmp = ((N)((NP)o).getRight()).getNoun();
                    
                    if(atomic[0] == null) {
                        /* This would mean that we haven't come across
                         * a verb, assume therefor that the N is subject
                         */
                        subject = tmp;
                    } else {
                        /* There is a verb there so the N can be assumed to 
                         * be the object
                         */
                        object = tmp;
                    }
                    
                    System.out.println(tmp);
                    
                } else if(((NP)o).getRight() instanceof NP) {
                    rec(((NP)o).getRight());
                }
            } else if(((NP)o).getLeft() instanceof CD) {
                
                System.out.println(((CD)((NP)o).getLeft()).getDigit());
                
            } else if(((NP)o).getLeft() instanceof NP) {
                
                rec(((NP)o).getLeft());
                
                if(((NP)o).getRight() instanceof PP) {
                    rec(((NP)o).getRight());
                }
            } else if(((NP)o).getLeft() instanceof JJ) {
                if(((NP)o).getRight() instanceof N) {
                    String jj_tmp = ((JJ)((NP)o).getLeft()).getWord();
                    String n_tmp = ((N)((NP)o).getRight()).getNoun();
                    System.out.println(jj_tmp + " " + n_tmp);
                }
            }
        } else if(o instanceof VP) {
            if(((VP)o).getLeft() instanceof V) {                
                System.out.println(((V)((VP)o).getLeft()).getVerb());
                
                action = ((V)((VP)o).getLeft()).getVerb();
            } else if(((VP)o).getLeft() instanceof VP) {
                rec(((VP)o).getLeft());
                if(((VP)o).getRight() instanceof NP) {
                    rec(((VP)o).getRight());
                } else if(((VP)o).getRight() instanceof PP) {
                    rec(((VP)o).getRight());
                } else if(((VP)o).getRight() instanceof RB) {
                    String tmp = ((RB)(((VP)o).getRight())).getAdverb();
                    System.out.println(tmp);
                }
            }
        } else if(o instanceof CC) {
            
        } else if(o instanceof PP) {
            String prep = ((P)((PP)o).getLeft()).getPreposition();
            System.out.println(prep);
            if(((PP)o).getRight() instanceof NP) {
                rec(((PP)o).getRight());
            }
        }
        atomic[0] = action;
        atomic[1] = subject;
        atomic[2] = object;
        
        //System.out.println(atomic[0]+"("+atomic[1]+","+atomic[2]+")");
    }
}
