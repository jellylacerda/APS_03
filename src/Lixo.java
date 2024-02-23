import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Lixo extends ObjetoCena{
    private int width = super.width;
    private int height = super.height;
    private Image lixoImage =  new ImageIcon("resources/shit.png").getImage();

    public Lixo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.drawImage(lixoImage, x, y, width, height, null);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}