import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class Robo extends ObjetoCena implements KeyListener{
    private int width = 28;
    private int height = 28;
    private int oldX, oldY;
    private static int speed = 3;
    private boolean bU = false, bD = false, bL = false, bR = false; // Determine a orientação do robô
    private Direction direction = Direction.STOP;
    private Direction kdirection = Direction.U;
    private int panelX = JogoFrame.getWindowX();
    private int panelY = JogoFrame.getWindowY();
    private int x = 0, y = 57; // local inicial do robo
    private static int limiteArmazena = 10; //modifica numero aqui para mudar limite de lixo q pode armazenar
    private static int escudo = 0; //Resista ao impacto dos veículos
    enum Direction {//enumeração de direções
        U, D, L, R, LU, LD, RU, RD, STOP
    };

    private static Image[] RobosImages = new Image[] { // imagens do robo em diferente estado
            new ImageIcon("resources/xinU.jpg").getImage(),
            new ImageIcon("resources/xinD.jpg").getImage(),
            new ImageIcon("resources/xinL.jpg").getImage(),
            new ImageIcon("resources/xinR.jpg").getImage(),
            new ImageIcon("resources/xinLU.jpg").getImage(),
            new ImageIcon("resources/xinLD.jpg").getImage(),
            new ImageIcon("resources/xinRU.jpg").getImage(),
            new ImageIcon("resources/xinRD.jpg").getImage(),
    };

    public void draw(Graphics g) {
        switch (kdirection) {
            case U -> g.drawImage(RobosImages[0], x, y, width, height, null);
            case D -> g.drawImage(RobosImages[1], x, y, width, height, null);
            case L -> g.drawImage(RobosImages[2], x, y, width, height, null);
            case R -> g.drawImage(RobosImages[3], x, y, width, height, null);
            case LU -> g.drawImage(RobosImages[4], x, y, width, height, null);
            case LD -> g.drawImage(RobosImages[5], x, y, width, height, null);
            case RU -> g.drawImage(RobosImages[6], x, y, width, height, null);
            case RD -> g.drawImage(RobosImages[7], x, y, width, height, null);
        }
    }

    void move() {
        this.oldX = x;
        this.oldY = y;
        if (direction == Direction.U) {
            y -= speed;
        } else if (direction == Direction.D) {
            y += speed;
        } else if (direction == Direction.L) {
            x -= speed;
        } else if (direction == Direction.R) {
            x += speed;
        } else if (direction == Direction.LU) {
            x -= speed;
            y -= speed;
        } else if (direction == Direction.LD) {
            x -= speed;
            y += speed;
        } else if (direction == Direction.RU) {
            x += speed;
            y -= speed;
        } else if (direction == Direction.RD) {
            x += speed;
            y += speed;
        } else if (direction == Direction.STOP) {
        }
        //Determinar se a direção de parada é consistente com
        //a direção de condução
        if (this.direction != Direction.STOP)
            this.kdirection = this.direction;
        if (x < 0) {x = 0;}
        if (y < 57) {y = 57;}
        if (x > this.panelX-this.getWidth()-8*2)
        {x = this.panelX-this.getWidth()-8*2;}
        if (y > this.panelY-this.getHeight()-8-31-23)
        {y = this.panelY-this.getHeight()-8-31-23;}
        //8:pixel do bordor, 31:pixel do titulo, 23:pixel do JMenuBar
    }

    //validar direcao
    public void locateDirection() {
        if(bL && !bU && !bR && !bD) direction = Direction.L;
        else if(bL && bU && !bR && !bD) direction = Direction.LU;
        else if(!bL && bU && !bR && !bD) direction = Direction.U;
        else if(!bL && bU && bR && !bD) direction = Direction.RU;
        else if(!bL && !bU && bR && !bD) direction = Direction.R;
        else if(!bL && !bU && bR && bD) direction = Direction.RD;
        else if(!bL && !bU && !bR && bD) direction = Direction.D;
        else if(bL && !bU && !bR && bD) direction = Direction.LD;
        else if(!bL && !bU && !bR && !bD) direction = Direction.STOP;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, this.width, this.height);
    }

    // Lógica de movimento após a detecção de colisão
    private void goToOldDirection(double x,double y,
                                  double width,double height) {
        if(direction == Direction.L) {
            this.x = (int) (x + width);
        }
        if(direction == Direction.LU) {
            this.x = oldX;
            this.y = oldY;
        }
        if(direction == Direction.U) {
            this.y = (int) (y + height);
        }
        if(direction == Direction.RU) {
            this.x = oldX;
            this.y = oldY;
        }
        if(direction == Direction.R) {
            this.x = (int) (x - this.width);
        }
        if(direction == Direction.RD) {
            this.x = oldX;
            this.y = oldY;
        }
        if(direction == Direction.D) {
            this.y = (int) (y - this.height);
        }
        if(direction == Direction.LD) {
            this.x = oldX;
            this.y = oldY;
        }
    }

    /** Detectar colisão do robô com a parede */
    public boolean collideWall(Wall wall) {
        if (this.getRect().intersects(wall.getRect())) {
            this.goToOldDirection(wall.getRect().getX(),
                    wall.getRect().getY(),
                    wall.getRect().getWidth(),
                    wall.getRect().getHeight());
            return true;
        }
        return false;
    }

    /** Detectar colisão do robô com a lixeira */
    public boolean collideBin(RubbishBin bin) {
        if (this.getRect().intersects(bin.getRect())&&JogoFrame.getLixosColhidos().size()!=0) {
            JogoFrame.getLixosColhidos().clear();
            Music.playBGM(Music.esvazieLixo);
            return true;
        }
        if (JogoFrame.getLixos().size()==0&&JogoFrame.getLixosColhidos().size()==0&&JogoFrame.isRoboAlive()) {
            JogoFrame.getMap().aumentaQuantiLixo();
            JogoFrame.getMap().addLixo();
            TimerCarro.reduceChance();
            EstadoLabel.levelUp();
            Music.playBGM(Music.esvazieLixo);
            return true;
        }
        return false;
    }

    /** Detectar colisão do robô com o carro */
    public void collideCarro(Carro carro) {
        if (this.getRect().intersects(carro.getRect())) {
            Music.playBGM(Music.explode);
            if (escudo>0) {
                escudo--;
            } else {
                TimerJogo.reduceTime();
            }
            JogoFrame.getCarros().remove(carro);
        }
    }

    /** Verifique se o lixo está cheio */
    public static boolean checkLixoFull() {
        if (JogoFrame.getLixosColhidos().size() == limiteArmazena) {
            return true;
        } else {
            return false;
        }
    }

    /** Detectar colisão do robô com lixos */
    public void collideLixo(Lixo lixo) {
        if (this.getRect().intersects(lixo.getRect())) {
            if (!Robo.checkLixoFull()) {
                Music.playBGM(Music.coletaLixo);
                JogoFrame.getLixosColhidos().add(lixo);
                JogoFrame.getLixos().remove(lixo);
                EstadoLabel.addScoreLixo();
                TimerJogo.addTime();
            } else {
                this.goToOldDirection(lixo.getRect().getX(),
                        lixo.getRect().getY(),
                        lixo.getRect().getWidth(),
                        lixo.getRect().getHeight());
            }
        }
    }

    public void collideMod(ModAtualiza mod) {
        if (this.getRect().intersects(mod.getRect())) {
            JogoFrame.getMods().remove(mod);
            TimerJogo.addTime10();
            EstadoLabel.addScoreMod();
            Music.playBGM(Music.update);
            if (ModAtualiza.getLevelUpgrade()==0) {
                limiteArmazena = 20;
            } else if (ModAtualiza.getLevelUpgrade()==1) {
                this.speed = 4;
            } else {
                escudo++;
            }
            ModAtualiza.aumentaLevel();
        }
    }

    public void setRoboLocation(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W -> this.setbU(true);
            case KeyEvent.VK_S -> this.setbD(true);
            case KeyEvent.VK_A -> this.setbL(true);
            case KeyEvent.VK_D -> this.setbR(true);
            case KeyEvent.VK_SPACE -> { // pausar jogo e musica
                JogoFrame.setPause(!JogoFrame.isPause());
            }
            case KeyEvent.VK_ENTER -> JogoFrame.getLixos().clear();
        }
        locateDirection();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W -> this.setbU(false);
            case KeyEvent.VK_S -> this.setbD(false);
            case KeyEvent.VK_A -> this.setbL(false);
            case KeyEvent.VK_D -> this.setbR(false);
        }
        locateDirection();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void setbU(boolean b) {
        this.bU = b;
    }

    public void setbL(boolean b) {
        this.bL = b;
    }

    public void setbR(boolean b) {
        this.bR = b;
    }

    public void setbD(boolean b) {
        this.bD = b;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static void setLimiteArmazena(int limiteArmazena) {
        Robo.limiteArmazena = limiteArmazena;
    }

    public static void setEscudo(int escudo) {
        Robo.escudo = escudo;
    }

    public static int getEscudo() {
        return escudo;
    }

    public static void setSpeed(int speed) {
        Robo.speed = speed;
    }
}
