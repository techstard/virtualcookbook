/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author rkrantz
 */
public class OptionDialog extends JDialog {
    OptionDialog() {
        setTitle("Virtual Gusteau");
        
        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);
        
        JPanel topPanel = new JPanel(new BorderLayout(0,0));
        topPanel.setMaximumSize(new Dimension(450,0));
        JLabel hint = new JLabel("Preferences");
        hint.setBorder(BorderFactory.createEmptyBorder(0,25,0,0));
        topPanel.add(hint);
        
        JLabel label = new JLabel("bild?");
        label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        topPanel.add(label, BorderLayout.EAST);
        
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);
        
        topPanel.add(separator, BorderLayout.SOUTH);
        
        basic.add(topPanel);
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JTextPane pane = new JTextPane();
        
        pane.setContentType("text/html");
        String text = "<p><b>Closing windows using the mouse wheel</b></p>" +
            "<p>Clicking with the mouse wheel on an editor tab closes the window. " +
            "This method works also with dockable windows or Log window tabs.</p>";
        pane.setText(text);
        pane.setEditable(false);
        textPanel.add(pane);
        
        basic.add(textPanel);
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton cancel = new JButton("Cancel");
        cancel.setMnemonic(KeyEvent.VK_C);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JButton close = new JButton("OK");
        close.setMnemonic(KeyEvent.VK_O);

        bottom.add(cancel);
        bottom.add(close);
        basic.add(bottom);

        bottom.setMaximumSize(new Dimension(450, 0));

        setSize(new Dimension(450, 350));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
