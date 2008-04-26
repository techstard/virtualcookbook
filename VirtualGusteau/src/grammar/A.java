package grammar;

/**
 *
 * @author rkrantz
 */
public class A {
    private String article;
    public A(String s) {
        article = s;
    }

    @Override
    public String toString() {
        return "Article:"+article;
    }
    
}
