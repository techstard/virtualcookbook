package grammar;

/**
 *
 * @author rkrantz
 */
public class Conjunction {
    private String conjunction;
    public Conjunction(String s) {
        conjunction = s;
    }

    @Override
    public String toString() {
        return "Conjunction:"+conjunction;
    }

    public String getConjunction() {
        return conjunction;
    }
    
}
