package grammar;

/**
 *
 * @author rkrantz
 */
public class RB {
    private String word;
    public RB(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        String tmp = "Adverb:"+word;
        return tmp;
    }
    
}
