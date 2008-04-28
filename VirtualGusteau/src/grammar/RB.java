package grammar;

/**
 *
 * @author rkrantz
 */
public class RB {
    private String adverb;
    public RB(String word) {
        this.adverb = word;
    }

    @Override
    public String toString() {
        String tmp = "Adverb:"+adverb;
        return tmp;
    }

    public String getAdverb() {
        return adverb;
    }
    
}
