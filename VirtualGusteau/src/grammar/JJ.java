package grammar;

/**
 *
 * @author rkrantz
 */
public class JJ {
    private String word;
    public JJ(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        String tmp = "Adjective:"+word;
        return tmp;
    }

    public String getWord() {
        return word;
    }
    
}
