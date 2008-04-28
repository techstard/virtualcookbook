package grammar;

/**
 *
 * @author rkrantz
 */
public class V {
    private String verb;
    public V(String s) {
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
