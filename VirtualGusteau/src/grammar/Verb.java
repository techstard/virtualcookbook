package grammar;

/**
 *
 * @author rkrantz
 */
public class Verb {
    private String verb;
    public Verb(String s) {
        verb = s;
    }

    @Override
    public String toString() {
        return "Verb:"+verb;
    }

    public String getVerb() {
        return verb;
    }
    
}
