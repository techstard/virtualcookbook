package grammar;

public class AdverbPhrase {
    private Object left;
    private Object right;
    
    public AdverbPhrase(Adverb a) {
        left = a;
    }
    public AdverbPhrase(AdverbPhrase a, Adverb ap) {
        left = a;
        right = ap;
    }
    public Object getLeft() {
        return left;
    }
    public Object getRight() {
        return right;
    }
    @Override
    public String toString() {
        if(left instanceof Adverb && !(right instanceof AdverbPhrase)) {
            return "AdvP:["+left.toString()+"]";
        } else if(left instanceof AdverbPhrase && right instanceof Adverb) {
            return "AdvP:["+left.toString()+" "+right.toString()+"]";
        } else {
            return "Corrupt instance of AdverbPhrase";
        }
    }
    
}