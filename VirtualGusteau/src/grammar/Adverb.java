package grammar;

/**
 *
 * @author rkrantz
 */
public class Adverb {
    private String adverb;
    public Adverb(String word) {
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
