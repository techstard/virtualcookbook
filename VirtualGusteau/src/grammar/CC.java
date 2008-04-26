package grammar;

/**
 *
 * @author rkrantz
 */
public class CC {
    private String conjunction;
    public CC(String s) {
        conjunction = s;
    }

    @Override
    public String toString() {
        return "Conjunction:"+conjunction;
    }
    
}
