
package virtualgusteau;

import java.io.*;
import java.util.*;

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
        
        
        SplashDialog sd = new SplashDialog("graphics/Gusteau.jpg",view.getFrames()[0],model);
        
        model.addObserver(view);
        
        view.setVisible(true);
        
        model.initiate();
    }
}
