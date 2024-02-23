
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Carro extends ObjetoCena{
    protected int height = super.width * 2;

    private static volatile int speed = 3;
    private Image carro = null;
    private boolean estado = true;
    private Direction direction = Direction.U;
    private enum Direction {U, D};//enumeração de direções
    private int panelY = JogoFrame.getWindowY();
    private static Image[] carroImages = new Image[] {
            new ImageIcon("resources/carroRU.png").getImage(),
            new ImageIcon("resources/carroCU.png").getImage(),
            new ImageIcon("resources/carroYU.png").getImage(),
            new ImageIcon("resources/carroRD.png").getImage(),
            new ImageIcon("resources/carroYD.png").getImage(),
            new ImageIcon("resources/carroCD.png").getImage(),
    };

    public Carro() {
        //Determinar faixas aleatoriamente
        this.x = (((int)(Math.random() * 4)) + 16) * width;;

        //Determinar aleatoriamente a direção do veículo
        if (Math.random()>0.5) {
            this.y = 800-this.height;
            direction = Direction.U;
            carro = carroImages[(int)(Math.random()*2)];
        } else {
            this.y = 0;
            direction = Direction.D;
            carro = carroImages[((int)(Math.random()*2))+3];
        }
    }

    public void draw(Graphics g) {
        g.drawImage(carro, x, y, width, height, null);
    }

    public void move() {
        switch (direction) {
            case U:
                this.y -= speed;
                if (y < 0) {
                    this.estado = false;
                }
                break;
            case D:
                this.y += speed;
                if (y > this.panelY-this.height-8-31) {
                    this.estado = false;
                }
                break;
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isEstado() {
        return this.estado;
    }

    public boolean collideEachOther(Carro carro) {
        if (this.getRect().intersects(carro.getRect())&&this.direction!=carro.direction) {
            if (Math.random()>0.95) {
                ModAtualiza mod = new ModAtualiza(this.x,this.y);
                JogoFrame.getMods().add(mod);
            }
            return true;
        } else {
            return false;
        }
    }

    public static void speedUp() {
        Carro.speed += 2;
    }

    public static void setSpeed(int speed) {
        Carro.speed = speed;
    }
}