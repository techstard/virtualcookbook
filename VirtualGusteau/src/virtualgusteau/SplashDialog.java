package virtualgusteau;

import javax.swing.*;
import java.awt.*;
import com.knowledgebooks.nlp.fasttag.FastTag;

class SplashDialog extends JWindow 
{    
    public SplashDialog(Frame f, Model model)
    {
        super(f);
        Image splash_image = Toolkit.getDefaultToolkit().getImage("Gusteau.jpg");
        JLabel l = new JLabel(new ImageIcon(splash_image));
        JLabel label = new JLabel("Hashing Lexicon...");
        getContentPane().add(l, BorderLayout.CENTER);
        getContentPane().add(label, BorderLayout.SOUTH);
        pack();
        Dimension screenSize =
          Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation(screenSize.width/2 - (labelSize.width/2),
                    screenSize.height/2 - (labelSize.height/2));
             
        setVisible(true);
        screenSize = null;
        labelSize = null;
        
        FastTag fastTag = new FastTag();
        model.setFastTag(fastTag);
        dispose();
    }
}