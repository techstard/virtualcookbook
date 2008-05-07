package grammar;

/**
 *
 * @author rkrantz
 */
public class Preposition {
    private String preposition;
    public Preposition(String s) {
        preposition = s;
    }

    @Override
    public String toString() {
        return "Prep:"+preposition;
    }

    public String getPreposition() {
        return preposition;
    }
    
}
