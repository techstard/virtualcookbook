
package virtualgusteau;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.text.DateFormat;


/**
 * @author Robert Krantz
 */
public class View extends JFrame implements Observer {
    
    private Controller controller;
    private Model model;
    
    private JPanel right;
    private JPanel left;
    
    private JTextField textField;
    private JScrollPane chatPane;
    private JTextArea chatArea;
    private JPanel pictureFrame;
    private JTextArea recipeArea;
    private JScrollPane recipePane;
    
    private Calendar cal;
    private Date date;
    private Locale locale;
    
    public View(Model model) {
        
        try {
	    // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException e) {
           // handle exception
        }
        catch (ClassNotFoundException e) {
           // handle exception
        }
        catch (InstantiationException e) {
           // handle exception
        }
        catch (IllegalAccessException e) {
           // handle exception
        }
        
        this.model = model;
        controller = new Controller(model);
        
        setTitle("Virtual Gusteau");      
        
        createMenu();
        
        left = new JPanel();
        right = new JPanel();
        
        left.setLayout(new BorderLayout());
                        
        textField = new JTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        textField.setActionCommand("2");
        textField.addActionListener(controller);
                
        chatPane = new JScrollPane();
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                textField.requestFocusInWindow();
            }

            public void focusLost(FocusEvent e) {
            } 
        });
        
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        
        chatPane.getViewport().add(chatArea);
        
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);
        
        left.add(chatPane);
        left.add(textField,BorderLayout.SOUTH);
        
        pictureFrame = new JPanel();
        pictureFrame.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        pictureFrame.setBounds(23,23,204,170);
                                
        right.setPreferredSize(new Dimension(250,400));
        right.setBorder(BorderFactory.createEtchedBorder());
        right.setLayout(null);
        
        ImageIcon icon = new ImageIcon("Gusteau_icon_200px.jpg");
        JLabel label = new JLabel();
        label.setIcon(icon);
        
        pictureFrame.setLayout(null);
        label.setBounds(2,2,200,166);
        pictureFrame.add(label);
        
        recipePane = new JScrollPane();
        recipeArea = new JTextArea();
        recipeArea.setWrapStyleWord(true);
        recipeArea.setLineWrap(true);
        recipeArea.setEditable(true);
        
        recipePane.getViewport().add(recipeArea);
        recipePane.setBounds(23,206,204,320);
        recipePane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        right.add(pictureFrame);
        right.add(recipePane);
        
        add(left);
        add(right,BorderLayout.EAST);
        
        setSize(700,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
    public void createMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        JMenu tools = new JMenu("Tools");
        tools.setMnemonic(KeyEvent.VK_T);

        JMenuItem fileClose = new JMenuItem("Close");
        fileClose.setMnemonic(KeyEvent.VK_C);
        fileClose.setActionCommand("1");
        fileClose.addActionListener(controller);

        JMenuItem about = new JMenuItem("About");
        about.setMnemonic(KeyEvent.VK_A);
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                AboutDialog ad = new AboutDialog();
                ad.setVisible(true);
            }
        });
        
        JMenuItem pref = new JMenuItem("Preferences");
        pref.setMnemonic(KeyEvent.VK_P);
        pref.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OptionDialog o = new OptionDialog();
                o.setVisible(true);
            }
        });
        
        tools.add(pref);
        
        JMenuItem helpMe = new JMenuItem("Help Topics");
        helpMe.setMnemonic(KeyEvent.VK_E);

        file.add(fileClose);
        help.add(about);
        help.add(helpMe);

        menubar.add(file);
        menubar.add(tools);
        menubar.add(help);

        setJMenuBar(menubar);
    }    
    public void update(Observable o, Object arg) {
        
        textField.setText("");
        
        locale = Locale.getDefault();
        date = new Date();
        String s = DateFormat.getTimeInstance(DateFormat.MEDIUM,
                    locale).format(date);
        
        chatArea.append("[" + s + "]<Linguini>: " + model.getInput() + "\n");
        chatArea.append("[" + s + "]<Gusteau>: " + model.getOutput() + "\n");
    }
}
