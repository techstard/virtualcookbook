package virtualgusteau;

/**
 *
 * @author Tobias
 */
public class CategoryException extends Exception {
    private String ing;
    private String cat;

    public CategoryException(String cat) {
        this.cat = cat;
    }
    public CategoryException(String cat, String ing) {
        this.cat = cat;
        this.ing = ing;
    }
    public String getIngredient() {
        return ing;
    }
    public String getCategory() {
        return cat;
    }
}
