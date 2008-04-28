package grammar;

/**
 *
 * @author rkrantz
 */
public class A extends TreeNode {
    private String article;
    public A(String s) {
        article = s;
    }

    @Override
    public String toString() {
        return "Article:"+article;
    }
    
}
