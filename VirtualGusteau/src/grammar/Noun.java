package grammar;

/**
 *
 * @author rkrantz
 */
public class Noun {
    private String noun;
    public Noun(String s) {
        noun = s;
    }

    @Override
    public String toString() {
        return "Noun:"+noun;
    }

    public String getNoun() {
        return noun;
    }
    public void makeCompoun(String s) {
        noun = noun + " "+s;
    }
}
