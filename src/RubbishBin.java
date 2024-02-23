import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class RubbishBin extends ObjetoCena{
    private Image rubbishBin =  new ImageIcon("resources/rubBin.png").getImage();

    public RubbishBin(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.drawImage(rubbishBin, x, y, width, height, null);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}