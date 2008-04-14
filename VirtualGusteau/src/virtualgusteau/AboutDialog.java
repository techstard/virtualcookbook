package virtualgusteau;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author Robert
 */
public class AboutDialog extends JDialog {
    public AboutDialog() {
        setTitle("About Notes");
        
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        
        add(Box.createRigidArea(new Dimension(0,10)));
        
        JLabel label = new JLabel("Virtual Gusteau");
        label.setFont(new Font("Tahoma",Font.BOLD,20));
        label.setAlignmentX(0.5f);
        add(label);
        
        add(Box.createRigidArea(new Dimension(0,10)));
        
        printNames();
        
        add(Box.createRigidArea(new Dimension(0,20)));
        
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        close.setAlignmentX(0.5f);
        add(close);
        
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        setResizable(false);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300,250);
    }
    private void printNames() {
        JLabel name = new JLabel("Anders Lorentzen");
        name.setFont(new Font("Tahoma",Font.PLAIN,13));
        name.setAlignmentX(0.5f);
        add(name);
        
        name = new JLabel("Magnus Spånggård");
        name.setFont(new Font("Tahoma",Font.PLAIN,13));
        name.setAlignmentX(0.5f);
        add(name);
        
        name = new JLabel("Olof Millberg");
        name.setFont(new Font("Tahoma",Font.PLAIN,13));
        name.setAlignmentX(0.5f);
        add(name);
        
        name = new JLabel("Patrik Björkman");
        name.setFont(new Font("Tahoma",Font.PLAIN,13));
        name.setAlignmentX(0.5f);
        add(name);
        
        name = new JLabel("Robert Krantz");
        name.setFont(new Font("Tahoma",Font.PLAIN,13));
        name.setAlignmentX(0.5f);
        add(name);
        
        name = new JLabel("Tobias Alette");
        name.setFont(new Font("Tahoma",Font.PLAIN,13));
        name.setAlignmentX(0.5f);
        add(name);
    }
}