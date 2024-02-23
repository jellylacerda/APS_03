import javax.swing.*;
import java.awt.*;

public class ModAtualiza extends ObjetoCena{
    private static int levelUpgrade = 0;
    private Image modImage =  new ImageIcon("resources/mod.png").getImage();

    public ModAtualiza(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.drawImage(modImage, x, y, width, height, null);
    }
    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public static int getLevelUpgrade() {
        return levelUpgrade;
    }

    public static void setLevelUpgrade(int levelUpgrade) {
        ModAtualiza.levelUpgrade = levelUpgrade;
    }

    public static void aumentaLevel() {
        ModAtualiza.levelUpgrade ++;
    }
}
