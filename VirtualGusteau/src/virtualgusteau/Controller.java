
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
            case 10:            // Close
                System.exit(0);
                break;
            case 2:
                JTextField t = (JTextField)event.getSource();
                model.parse(t.getText());
                break;
            case 20:            // Preferences
                OptionDialog o = new OptionDialog();
                o.setVisible(true);
                break;
            case 30:            // About
                AboutDialog ad = new AboutDialog();
                ad.setVisible(true);
                break;
            case 40:            // Help Topics
                throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
