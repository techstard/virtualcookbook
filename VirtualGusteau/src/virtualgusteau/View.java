
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
        pictureFrame.setBounds(15,15,120,120);
        pictureFrame.setBackground(Color.red);
                                
        right.setPreferredSize(new Dimension(150,400));
        right.setBorder(BorderFactory.createEtchedBorder());
        right.setLayout(null);
        
        right.add(pictureFrame);
        
        add(left);
        add(right,BorderLayout.EAST);
        
        setSize(500,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
    public void update(Observable o, Object arg) {
        textArea.append(model.getArea() + "\n");
        textField.setText(model.getField());
    }
    
    public class AnImage extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D) g;
            Image image = new ImageIcon("Gusteau.jpg").getImage();
            g2d.drawImage(image, 10, 10,null);
        }
    }
}
