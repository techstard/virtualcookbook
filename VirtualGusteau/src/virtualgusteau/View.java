
package virtualgusteau;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;


/**
 * @author Robert Krantz
 */
public class View extends JFrame implements Observer {
    
    private Controller controller;
    private Model model;
    
    private JPanel right;
    private JPanel left;
    
    private JTextField textField;
    private JTextArea textArea;
    private JPanel pictureFrame;
    
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
        
        left = new JPanel();
        right = new JPanel();
        
        left.setLayout(new BorderLayout());
                        
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
        
        left.add(pane);
        left.add(textField,BorderLayout.SOUTH);
        
        pictureFrame = new JPanel();
        pictureFrame.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        pictureFrame.setBounds(63,13,124,124);
                                
        right.setPreferredSize(new Dimension(250,400));
        right.setBorder(BorderFactory.createEtchedBorder());
        right.setLayout(null);
        
        ImageIcon icon = new ImageIcon("Gusteau_icon.jpg");
        JLabel label = new JLabel();
        label.setIcon(icon);
        
        pictureFrame.setLayout(null);
        label.setBounds(2,2,120,120);
        pictureFrame.add(label);
        
        right.add(pictureFrame);
        
        add(left);
        add(right,BorderLayout.EAST);
        
        setSize(700,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
    public void update(Observable o, Object arg) {
        textArea.append(model.getArea() + "\n");
        textField.setText(model.getField());
    }
}
