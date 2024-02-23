import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;

import javax.swing.*;

public class JogoFrame extends JFrame implements Runnable, ActionListener {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int windowSizeX = 1232;
    private static final int windowSizeY = 823; // width e height da janela

    private static boolean roboAlive = true;
    private static volatile boolean pause = false; // Julgando o jogo está pausado
    private static final Robo robo = new Robo();
    private final EstadoLabel eLabel = new EstadoLabel();
    private static ArrayList<Wall> walls = new ArrayList<>();
    private static ArrayList<Lixo> lixos = new ArrayList<>();
    private static ArrayList<Lixo> lixosColhidos = new ArrayList<>();
    private static ArrayList<RubbishBin> bins = new ArrayList<>();
    private static ArrayList<Estrada> estradas = new ArrayList<>();
    private static ArrayList<ModAtualiza> mods = new ArrayList<>();
    private static volatile ArrayList<Carro> carros = new ArrayList<>(); // Criar as contêineres para elementos do jogo
    private static Map map = new Map(); // cria mapa
    static {map.fillMap();} // geras os elementos: parede, lixo, estrada etc.

    private TimerCarro tc; // controle de geração de veículos
    private TimerJogo tj; // cronômetro de jogo

    public JogoFrame() throws IOException {
        roboAlive = true;
        createMenu();

        if(!ScoreFrame.getFile().exists()) { //Criar um arquivo se ele não existir
            ScoreFrame.getFile().createNewFile();
        }

        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();// obter o tamanho da tela
        int centerX = screenSize.width/2;
        int centerY = screenSize.height/2;

        this.setTitle("Test");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(centerX-JogoFrame.windowSizeX/2,centerY-JogoFrame.windowSizeY/2);
        this.setSize(JogoFrame.windowSizeX,JogoFrame.windowSizeY);// cria janela e colocar no centro da tela

        Thread th = new Thread(this);
        th.start();

        JPanel jp = new JPanel() {
            /**
             *
             */
            @Serial
            private static final long serialVersionUID = 1L;

            public void paint(Graphics g) {
                super.paint(g);
                for (Wall i : walls) {
                    i.draw(g);
                    robo.collideWall(i);
                }
                for (RubbishBin i : bins) {
                    i.draw(g);
                    robo.collideBin(i);
                }
                for (Estrada i : estradas) {i.draw(g);}
                for (Lixo i : lixos) {
                    i.draw(g);
                    robo.collideLixo(i);
                }
                for (int i = carros.size() - 1; i >= 0; i--) { // O loop reverso evita erros de exclusão
                    if (carros.get(i).isEstado()) {
                        carros.get(i).draw(g);
                        for (int j = carros.size() - 1; j >= 0; j--) {
                            // Detectar colisões entre carros
                            if (carros.get(i).collideEachOther(carros.get(j))) {
                                carros.remove(carros.get(i));
                                carros.remove(carros.get(j));
                            }
                        }
                        robo.collideCarro(carros.get(i));
                    } else {carros.remove(carros.get(i));}
                }
                for (ModAtualiza i : mods) {
                    i.draw(g);
                    robo.collideMod(i);
                }
                robo.draw(g);
                eLabel.draw(g);
            }
        };
        this.add(jp);

        this.setVisible(true);
        this.addKeyListener(robo);

        if (!TimerJogo.isCreated()) {
            tj = new TimerJogo();
            TimerJogo.setCreated(true);
        }
        tj.start();
        TimerJogo.setTime(45);

        if (!TimerCarro.isCreated()) {
            tc = new TimerCarro();
            TimerCarro.setCreated(true);
        }
        tc.start();
        TimerCarro.setChanceCria(1000);
        Carro.setSpeed(3);

    }

    public void run() {
        while (true) {
            while (pause) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (TimerJogo.getTime() <= 0) {
                    roboAlive = false;
                    TimerCarro.setCreated(false); // Marca a morte do Thread
                    TimerJogo.setCreated(false); // Marca a morte do Thread
                    JOptionPane.showMessageDialog(null, "Game Over!",
                            "Ops", JOptionPane.PLAIN_MESSAGE); // aviso de fim de jogo
                    maxScore();
                    try {
                        String username;
                        FileWriter fw = new FileWriter("text/score.txt", true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        if (StartFrame.getUserName() == null) {
                            username = "jogador anônimo";
                        } else {
                            username = StartFrame.getUserName();
                        }
                        bw.write("\t" + "A pontuação obtida pelo " + username + " é：" + EstadoLabel.getScore() +
                                "\n");// Adicionar uma string a um arquivo existente
                        bw.close();
                        fw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    StartFrame.getAcc().stop();
                    JogoFrame.setPause(true);
                    Object[] options = {"Sim", "Não"};
                    int response = JOptionPane.showOptionDialog(this, "Deseja iniciar um " +
                                    "novo jogo?", "", JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (response == 0) {
                        resetEstadoJogo();
                        tj = new TimerJogo();
                        tj.start();
                        tc = new TimerCarro();
                        tc.start();
                        if (StartFrame.isBgm()) {
                            StartFrame.getAcc().loop();
                        }
                    } else {
                        if (StartFrame.isBgm()) {
                            StartFrame.getAcc().loop();
                        }
                        JogoFrame.setPause(true);
                        while (pause) {
                            try {
                                Thread.sleep(16);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                Thread.sleep(16);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = carros.size() - 1; i >= 0; i--) { // O loop reverso evita erros de exclusão
                carros.get(i).move();
            }
            robo.move();
            repaint();
        }
    }

    public void maxScore() {
        /** dicas de obter pontuação maior */
        String str = MaxScore.readTxt();
        String[] scoreStr = str.split(":");
        int sco = Integer.parseInt(scoreStr[1]);
        if (EstadoLabel.getScore() > sco) {
            sco = EstadoLabel.getScore();
            MaxScore.write(StartFrame.getUserName(), sco, EstadoLabel.getLevel());
            if (StartFrame.getUserName()==null||StartFrame.getUserName().equals("jogador anônimo")) {
                JOptionPane.showMessageDialog(null, "Jogador anônimo criou a pontuação " +
                        "mais alta: " + sco + ", em nivel " + EstadoLabel.getLevel());
            } else {
                JOptionPane.showMessageDialog(null, StartFrame.getUserName() +
                        ", você criou a pontuação mais alta: " + sco + ", em nivel " + EstadoLabel.getLevel());
            }
        }
    }

    public static void resetEstadoJogo() {
        roboAlive = true; // Redefinir estado do jogo
        // Redefinir a posição e sentido do robô
        robo.setRoboLocation(0,57, Robo.Direction.STOP);
        Robo.setSpeed(3); // redefinir velocidade do robo
        Robo.setEscudo(0); // zerar escudo
        Robo.setLimiteArmazena(10); // redefinir limite de armazenar dos lixos
        ModAtualiza.setLevelUpgrade(0); // zerar level de atualizar
        EstadoLabel.setScore(0);// redefinir pontuação
        EstadoLabel.setLevel(1);// redefinir o nível do jogo
        Carro.setSpeed(3); // reset velocidade de carro
        TimerJogo.setTime(45);// reset tempo inicial
        TimerCarro.setChanceCria(1000); // redefinir chance de geracao de carro
        Map.setChanceCria(0.94);// Redefinir chance de geração de lixo
        mods.clear();// Esvaziamento de mod
        lixos.clear();// Esvaziamento de lixo
        lixosColhidos.clear();// Esvaziamento de lixo colhido
        walls.clear();// Esvaziamento de wall
        bins.clear();// Esvaziamento de lixeira
        estradas.clear();// Esvaziamento de estrada
        carros.clear();// Esvaziamento de carros
        map.fillMap();// redefinir mapa
        pause = true; // Redefinir estado do jogo
    }

    public void createMenu() {
        // Barra de menu
        JMenuBar jmb = new JMenuBar();
        // Item do menu
        JMenu jm1 = new JMenu("Jogo");
        JMenu jm2 = new JMenu("Historia");
        JMenu jm3 = new JMenu("Ajuda");
        // botão de item de menu
        JMenuItem jmi1 = new JMenuItem("Pausar/Continuar");
        JMenuItem jmi2 = new JMenuItem("Reiniciar");
        JMenuItem jmi3 = new JMenuItem("BMG on/off");
        JMenuItem jmi4 = new JMenuItem("Exit");
        JMenuItem jmi6 = new JMenuItem("Maior pontuação");
        JMenuItem jmi5 = new JMenuItem("Recorde de pontuação do jogador");
        JMenuItem jmi7 = new JMenuItem("Controle");
        JMenuItem jmi8 = new JMenuItem("Sobre jogo");

        // adicionar menu
        jmb.add(jm1);
        jmb.add(jm2);
        jmb.add(jm3);

        // Definir itens do menu do jogo
        jm1.add(jmi1);
        jm1.add(jmi2);
        jm1.add(jmi3);
        jm1.add(jmi4);
        jm2.add(jmi5);
        jm2.add(jmi6);
        jm3.add(jmi7);
        jm3.add(jmi8);

        // Adicionar evento de ouvinte ao botão
        jmi1.addActionListener(this);
        jmi1.setActionCommand("stop");

        jmi2.addActionListener(this);
        jmi2.setAccelerator(KeyStroke.getKeyStroke("F1"));// Definir teclas de atalho
        jmi2.setActionCommand("restart");

        jmi3.addActionListener(this);
        jmi3.setAccelerator(KeyStroke.getKeyStroke("F2"));// Definir teclas de atalho
        jmi3.setActionCommand("music");

        jmi4.addActionListener(this);
        jmi4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));// Definir teclas de atalho
        jmi4.setActionCommand("exit");

        jmi5.addActionListener(this);
        jmi5.setActionCommand("history");

        jmi6.addActionListener(this);
        jmi6.setActionCommand("historyMax");

        jmi7.addActionListener(this);
        jmi7.setActionCommand("control");

        jmi8.addActionListener(this);
        jmi8.setActionCommand("help");

        // Coloque o menu na janela
        this.setJMenuBar(jmb);
    }

    //Definir o comportamento quando o botão da barra de menus é pressionado
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("stop")) {
            if (StartFrame.isBgm()) {
                StartFrame.getAcc().stop();
            } else {
                StartFrame.getAcc().loop();
            }
            JogoFrame.setPause(!JogoFrame.isPause());
        }
        if (e.getActionCommand().equals("restart")) {
            StartFrame.getAcc().stop();
            JogoFrame.setPause(true);
            Object[] options = { "Sim", "Não" };
            int response = JOptionPane.showOptionDialog(this, "Você confirma que deseja" +
                                                        " iniciar um novo jogo!", "",JOptionPane.YES_OPTION,
                                                        JOptionPane.QUESTION_MESSAGE, null,options, options[0]);
            if (response == 0) {
                try {
                    String username;
                    FileWriter fw = new FileWriter("text/score.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    if (StartFrame.getUserName() == null) {
                        username = "jogador anônimo";
                    } else {
                        username = StartFrame.getUserName();
                    }
                    bw.write("\t" + "A pontuação obtida pelo " + username + " é：" + EstadoLabel.getScore() +
                            "\n");// Adicionar uma string a um arquivo existente
                    bw.close();
                    fw.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                resetEstadoJogo();
                JogoFrame.setPause(false);
                if (StartFrame.isBgm()) {
                    StartFrame.getAcc().loop();
                }
            } else {
                if (StartFrame.isBgm()) {
                    StartFrame.getAcc().loop();
                }
                JogoFrame.setPause(true);
            }
        }
        if (e.getActionCommand().equals("music")) {
            if (StartFrame.isBgm()) {
                StartFrame.setBgm(false);
                StartFrame.getAcc().stop();
            } else {
                StartFrame.setBgm(true);
                StartFrame.getAcc().loop();
            }
        }
        if (e.getActionCommand().equals("exit")) {
            StartFrame.getAcc().stop();
            JogoFrame.setPause(true);
            Object[] options = { "Sim", "Não" };
            int response = JOptionPane.showOptionDialog(this, "Tem certeza de que " +
                                            "deseja sair do jogo?", "",JOptionPane.YES_OPTION,
                                            JOptionPane.QUESTION_MESSAGE, null,options, options[0]);
            if(response == 0){
                try {
                    String username;
                    FileWriter fw = new FileWriter("text/score.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    if (StartFrame.getUserName() == null) {
                        username = "jogador anônimo";
                    } else {
                        username = StartFrame.getUserName();
                    }
                    bw.write("\t" + "A pontuação obtida pelo " + username + " é：" + EstadoLabel.getScore() +
                            "\n");// Adicionar uma string a um arquivo existente
                    bw.close();
                    fw.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }else {
                if (StartFrame.isBgm()) {
                    StartFrame.getAcc().loop();
                }
                JogoFrame.setPause(true);
            }
        }
        if (e.getActionCommand().equals("history")) {
            JogoFrame.setPause(true);
            try {
                new ScoreFrame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getActionCommand().equals("historyMax")) {
            JogoFrame.setPause(true);
            String str = MaxScore.readTxt();
            String[] scoreStr=str.split(":");
            int sco = Integer.parseInt(scoreStr[1]);
            int nivel = Integer.parseInt(scoreStr[2]);
            if(scoreStr[0].equals("")){
                JOptionPane.showMessageDialog(null, "jogador anônimo"+" obteve a " +
                        "maior pontuação:" + sco + " em nivel " + nivel);
            }else{
                JOptionPane.showMessageDialog(null, scoreStr[0]+" obteve a maior pontuação:"
                        + sco + " em nivel " + nivel);
            }
        }
        if (e.getActionCommand().equals("help")) {
            JogoFrame.setPause(true);
            JOptionPane.showMessageDialog(null,
                    "O jogo é feito pelo:" +
                            "\nG3231B8 Allan Alves de Holanda" +
                            "\nG330285 Brenno Barbosa de Lima" +
                            "\nG3250G4 Chaoli Wang" +
                            "\nG270894 Carlos Vinícius Veras Rodrigue" +
                            "\nN6938G7 Jelly Lacerda" +
                            "\nG2030G6 Pamela Brandão Olival");
        }
        if (e.getActionCommand().equals("control")) {
            JogoFrame.setPause(true);
            JOptionPane.showMessageDialog(null, "WASD para controle robo, espaço para pausar,"+
                    "\nColeta lixo e esvazia-lo para subir o nível no jogo." + "\n\n" + "Sobre atualizações de robôs:"+
                    "\nOcasionalmente, algumas peças são descartadas no local do acidente, elas podem ser úteis");
        }
    }

    public static ArrayList<Wall> getWalls() {
        return walls;
    }

    public static ArrayList<Lixo> getLixos() {
        return lixos;
    }

    public static ArrayList<Lixo> getLixosColhidos() {
        return lixosColhidos;
    }

    public static ArrayList<RubbishBin> getBins() {
        return bins;
    }

    public static ArrayList<Estrada> getEstradas() {
        return estradas;
    }

    public static ArrayList<ModAtualiza> getMods() {
        return mods;
    }

    public static ArrayList<Carro> getCarros() {
        return carros;
    }

    public static Map getMap() {
        return map;
    }

    public static int getWindowX() {
        return windowSizeX;
    }

    public static int getWindowY() {
        return windowSizeY;
    }

    public static boolean isPause() {
        return JogoFrame.pause;
    }

    public static void setPause(boolean pause) {
        JogoFrame.pause = pause;
    }

    public static boolean isRoboAlive() {
        return roboAlive;
    }

    public static void setRoboAlive(boolean roboAlive) {
        JogoFrame.roboAlive = roboAlive;
    }
}