package grammar;

/**
 *
 * @author rkrantz
 */
public class Pronoun {
    private String word;
    public Pronoun(String word) {
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
