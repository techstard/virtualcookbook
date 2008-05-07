package grammar;
import java.util.*;

public class RelClause {
    private Object left;
    private Object right;
    public RelClause(Preposition p, VerbPhrase vp) {
        left = p;
        right = vp;
    }

    public Object getLeft() {
        return left;
    }

    public void setLeft(Object left) {
        this.left = left;
    }

    public Object getRight() {
        return right;
    }

    public void setRight(Object right) {
        this.right = right;
    }
    @Override
    public String toString() {
        String tmp = "NP:[";
        tmp += left.toString();
        if(right != null) {
            tmp += " "+right.toString();
        }
        tmp += "]";
        return tmp;
    }
}