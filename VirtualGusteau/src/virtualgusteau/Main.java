
package virtualgusteau;

/**
 *
 * @author rkrantz
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View(model);
        model.addObserver(view);
        view.setVisible(true);
    }

}
