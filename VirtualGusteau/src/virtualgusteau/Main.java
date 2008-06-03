
package virtualgusteau;

/**
 *
 * @author Robert Krantz
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View(model);        
        SplashDialog sd = new SplashDialog(view.getFrames()[0],model);        
        model.addObserver(view);        
        view.setVisible(true);        
        model.initiate();
    }
}
