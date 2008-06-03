
package virtualgusteau;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.ImageIcon;

import java.util.Date;
import java.util.Locale;

import java.text.DateFormat;
import java.net.URL;

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
 
    private Date date;
    private Locale locale;
    
    private JLabel label = new JLabel();
    
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
        Image gusteau_icon = Toolkit.getDefaultToolkit().getImage("Gusteau_icon.jpg");
        setIconImage(gusteau_icon);        
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
        chatArea.setEditable(true);
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
        chatArea.setText("<Gusteau>: Bienvenue, cher ami.\nWhat kind of magnifique recipe would you want today? I can prumise I will not disappoint you.\n\n");
        
//        chatArea.setCaretPosition(chatArea.getDocument().getLength());
        
        chatPane.setAutoscrolls(true);
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
        
        Image gusteau_large = Toolkit.getDefaultToolkit().getImage("Gusteau_icon_200px.jpg");
        ImageIcon icon = new ImageIcon(gusteau_large);
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
        recipePane.setAutoscrolls(true);
        recipePane.setBounds(23,206,204,280);
        recipePane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        right.add(pictureFrame);
        right.add(recipePane);
        
        add(left);
        add(right,BorderLayout.EAST);
        
        setSize(700,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(490,545));
        setResizable(true);
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
        Image close_image = Toolkit.getDefaultToolkit().getImage("close-16x16.png");
        ImageIcon close_icon = new ImageIcon(close_image);
        fileClose.setIcon(close_icon);
        fileClose.setMnemonic(KeyEvent.VK_C);
        fileClose.setActionCommand("10");
        fileClose.addActionListener(controller);

        JMenuItem about = new JMenuItem("About");
        Image about_image = Toolkit.getDefaultToolkit().getImage("info-about-16x16.png");
        ImageIcon about_icon = new ImageIcon(about_image);
        about.setIcon(about_icon);
        about.setMnemonic(KeyEvent.VK_A);
        about.setActionCommand("30");
        about.addActionListener(controller);

        file.add(fileClose);
        help.add(about);

        menubar.add(file);
        menubar.add(tools);
        menubar.add(help);

        setJMenuBar(menubar);
    }    
    public void update(Observable o, Object arg) {
        
        if(model.getClearText()) {
            model.setClearText();
            chatArea.setText("");
            textField.setText("");
            recipeArea.setText("");
        } else {
            textField.setText("");
            
            locale = Locale.getDefault();
            date = new Date();
            String s = DateFormat.getTimeInstance(DateFormat.MEDIUM,
                        locale).format(date);

            if (model.getInput() != null)
            	chatArea.append("<Linguini>: " + model.getInput() + "\n\n");
            chatArea.append("<Gusteau>: " + model.getOutput() + "\n\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
            recipeArea.setText("");
            recipeArea.setText(model.getIngredients());
        }
    }
}
