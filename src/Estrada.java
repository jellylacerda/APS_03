import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Estrada extends ObjetoCena{
    private Image estrada =  new ImageIcon("resources/estrada.jpg").getImage();

    public Estrada(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.drawImage(estrada, x, y, width, height, null);
    }
}
