import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class EstadoLabel {
    private int x = 0, y = 35;
    private static int score = 0; // pontuação do jogador
    private static int level = 1; // nível do jogo
    private static String value = "Espaço de armazenamento: 0";
    private static String time = "";
    private Image labelBG =  new ImageIcon("resources/labelBg.png").getImage();
    private Image lixo = new ImageIcon("resources/shit.png").getImage();
    private Image escudo = new ImageIcon("resources/escudo.png").getImage();
    private Font font = new Font("Arial", Font.BOLD, 25);

    public void draw(Graphics g) {
        if (Robo.checkLixoFull()) {
            value = " * " + JogoFrame.getLixosColhidos().size() + "( Cheio!)";
        }
        else {
            // Armazenamento de lixo em robôs
            value = " * " + JogoFrame.getLixosColhidos().size();
        }
        g.drawImage(labelBG, 0, 0, JogoFrame.getWindowX()-16, 57, null);
        g.setFont(this.font);
        g.drawString(EstadoLabel.value, this.x+40, this.y);
        g.drawImage(lixo,10, 10,32,32,null);
        g.drawString(" * "+Robo.getEscudo(), this.x+140, this.y);
        g.drawImage(escudo,110, 10,32,32,null);
        g.drawString(""+score, 570, this.y);
        g.drawString("level: "+level, 900, this.y);
        g.drawString(time, 300, this.y);
        if (JogoFrame.isPause()==true) {
            Color color = new Color(63,63,63,100);
            g.setColor(color);
            g.fillRect(0, 0, JogoFrame.getWindowX(), JogoFrame.getWindowY());
        }
    }

    public static void setScore(int score) {
        EstadoLabel.score = score;
    }

    public static void addScoreLixo() {
        EstadoLabel.score += (1+0.1*(level-1))*100;
    }

    public static void addScoreMod() {
        EstadoLabel.score += 1000;
    }

    public static void levelUp() {
        if (EstadoLabel.level<10) {
            EstadoLabel.level ++;
            Carro.speedUp();
        }
    }

    public static void setTime(String time) {
        EstadoLabel.time = time;
    }

    public static int getLevel() {
        return EstadoLabel.level;
    }

    public static int getScore() {
        return score;
    }

    public static void setLevel(int level) {
        EstadoLabel.level = level;
    }
}
