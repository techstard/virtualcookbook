package grammar;

/**
 *
 * @author rkrantz
 */
public class Adjective {
    private String word;
    public Adjective(String word) {
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
