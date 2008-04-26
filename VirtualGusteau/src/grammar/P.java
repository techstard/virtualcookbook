package grammar;

/**
 *
 * @author rkrantz
 */
public class P {
    private String preposition;
    public P(String s) {
        preposition = s;
    }

    @Override
    public String toString() {
        return "Prep:"+preposition;
    }
    
}
