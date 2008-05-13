package grammar;

public class AdjectivePhrase {
    private Object left;
    private Object right;
    
    public AdjectivePhrase(Adjective a) {
        left = a;
    }
    public AdjectivePhrase(AdverbPhrase ad, Adjective a) {
        left = ad;
        right = a;
    }
    public AdjectivePhrase(AdjectivePhrase ap, PrepositionalPhrase pp) {
        left = ap;
        right = pp;
    }
    public AdjectivePhrase(AdjectivePhrase ap, VerbPhrase vp) {
        left = ap;
        right = vp;
    }
    public Object getLeft() {
        return left;
    }
    public Object getRight() {
        return right;
    }
    public void setAdverb(Adverb a) {
        right = left;
        left = a;
    }
    @Override
    public String toString() {
        if(left instanceof Adjective) {
            return "AP:["+left.toString()+"]";
        } else if(left instanceof AdverbPhrase) {
            return "AP:["+left.toString()+" "+right.toString()+"]";
        } else if(left instanceof AdjectivePhrase && 
                (right instanceof PrepositionalPhrase ||
                right instanceof VerbPhrase)) {
            return "AP:["+left.toString()+" "+right.toString()+"]";
        } else {
            return "Instance of AdjectivePhrase is corrupt";
        }
    }
    
}