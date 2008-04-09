
package virtualgusteau;

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * @author Robert Krantz
 */
public class View extends JFrame implements Observer {
    
    private Controller controller;
    private Model model;
    
    private JTextField textField;
    private JTextArea textArea;
        
    public View(Model model) {
        this.model = model;
        controller = new Controller(model);
        
        setTitle("Virtual Gusteau");
        
        
        
        /*
         * Create the menu
         */
            JMenuBar menubar = new JMenuBar();
            JMenu file = new JMenu("File");

            JMenuItem fileClose = new JMenuItem("Close");
            fileClose.setActionCommand("1");
            fileClose.addActionListener(controller);
            
            file.add(fileClose);
            menubar.add(file);
            
            setJMenuBar(menubar);
        
                
        textField = new JTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        textField.setActionCommand("2");
        textField.addActionListener(controller);
                
        JScrollPane pane = new JScrollPane();
        textArea = new JTextArea();
        textArea.setEditable(false);
        
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        
        pane.getViewport().add(textArea);
        
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);
        
        add(pane);
        add(textField,BorderLayout.SOUTH);

        setSize(400,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
    public void update(Observable o, Object arg) {
        textArea.append(model.getArea() + "\n");
        textField.setText(model.getField());
    }
}
