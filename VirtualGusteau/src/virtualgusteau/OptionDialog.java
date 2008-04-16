/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author rkrantz
 */
public class OptionDialog extends JDialog {
    
    private JPanel topPanel;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    
    OptionDialog() {
        setTitle("Virtual Gusteau");
        
        setLayout(new BorderLayout());
        
        headerPanel();
        optionPanel();
        contentPanel();
        buttonPanel();    
        
        add(topPanel,BorderLayout.NORTH);
        add(leftPanel,BorderLayout.WEST);
        add(centerPanel,BorderLayout.CENTER);
        add(bottomPanel,BorderLayout.SOUTH);
        
        setSize(new Dimension(550, 350));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private void headerPanel() {
        topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(550,20));
        
        JLabel label = new JLabel("Preferences");        
        topPanel.add(label,BorderLayout.WEST);
        
        ImageIcon i = new ImageIcon("graphics/preferences-16x16.png");
        JLabel icon = new JLabel();
        icon.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
        icon.setIcon(i);
        topPanel.add(icon,BorderLayout.EAST);
        
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        topPanel.add(separator,BorderLayout.SOUTH);
        
    }
    private void optionPanel() {
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(130,350));
        leftPanel.setBackground(Color.gray);
    }
    private void contentPanel() {
        centerPanel = new JPanel();
    }
    private void buttonPanel() {
        bottomPanel = new JPanel(new BorderLayout());
        
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.black);
        bottomPanel.add(separator,BorderLayout.NORTH);
        
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(ok);
        buttons.add(cancel);
        
        bottomPanel.add(buttons,BorderLayout.CENTER);
        
        bottomPanel.setPreferredSize(new Dimension(550,40));
    }
}
