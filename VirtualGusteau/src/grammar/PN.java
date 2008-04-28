package grammar;

/**
 *
 * @author rkrantz
 */
public class PN {
    private String word;
    public PN(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        String tmp = "Pronoun:"+word;
        return tmp;
    }

    public String getWord() {
        return word;
    }
    
}
