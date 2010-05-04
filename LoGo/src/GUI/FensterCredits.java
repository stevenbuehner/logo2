package GUI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;



/**
 *
 * @author steven
 */
public class FensterCredits extends JFrame{

    private BackgroundImagePanel backgroundPanel;

    public FensterCredits() {
        super("Credits");

        this.init();

        this.setVisible(false);
    }

    private void init(){

        GrafikLib lib = GrafikLib.getInstance();
        BufferedImage bg = lib.getSprite("GUI/resources/Credits.jpg");
        this.backgroundPanel = new BackgroundImagePanel(bg);
        this.setContentPane(backgroundPanel);

        this.setSize(556, 392);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
    
}
