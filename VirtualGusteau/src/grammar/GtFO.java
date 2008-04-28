package grammar;
import java.util.*;
/**
 *
 * @author rkrantz
 */
public class GtFO {
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
                System.out.println("Pronoun");
            } else if(((NP)o).getLeft() instanceof N) {
                System.out.println("Noun");
            } else if(((NP)o).getLeft() instanceof A) {
                System.out.print("Article ");
                if(((NP)o).getRight() instanceof N) {
                    System.out.println("Noun");
                }
            } else if(((NP)o).getLeft() instanceof CD) {
                System.out.println("Digit");
            } else if(((NP)o).getLeft() instanceof NP) {
                rec(((NP)o).getLeft());
                if(((NP)o).getRight() instanceof PP) {
                    rec(((NP)o).getRight());
                }
            }
        } else if(o instanceof VP) {
            
        } else if(o instanceof CC) {
            System.out.println("CC");
        }
    }
}
