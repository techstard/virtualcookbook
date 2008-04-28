package grammar;

/**
 *
 * @author rkrantz
 */
public class N {
    private String noun;
    public N(String s) {
        noun = s;
    }

    @Override
    public String toString() {
        return "Noun:"+noun;
    }

    public String getNoun() {
        return noun;
    }
    
}
