package grammar;

/**
 *
 * @author rkrantz
 */
public class Article {
    private String article;
    public Article(String s) {
        article = s;
    }

    @Override
    public String toString() {
        return "Article:"+article;
    }

    public String getArticle() {
        return article;
    }
    
}
