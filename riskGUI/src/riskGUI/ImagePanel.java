package riskGUI;

import java.awt.*;

import javax.swing.JComponent; 

public class ImagePanel extends JComponent{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image;
    public ImagePanel(Image image) {
        this.image = image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.drawImage(image, 100, 100, this);
    }

}
