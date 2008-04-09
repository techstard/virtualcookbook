
package virtualgusteau;

import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author rkrantz
 */
public class Controller implements ActionListener {
    private Model model;
    public Controller(Model model) {
        this.model = model;
    }
    public void actionPerformed(ActionEvent event) {
        int cmd = Integer.parseInt(event.getActionCommand());
        
        switch(cmd) {
            case 1:
                System.exit(0);
                break;
            case 2:
                JTextField t = (JTextField)event.getSource();
                model.parse(t.getText());
        }
    }
}
