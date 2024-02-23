import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class StartFrame  extends JFrame implements ActionListener {
    private static final int windowSizeX = 1232;
    private static final int windowSizeY = 823; // width e height da janela

    private Icon iconStart = new ImageIcon("resources/startGame.png");
    private Icon iconQuit = new ImageIcon("resources/quit.png");
    private Icon startPressed = new ImageIcon("resources/startGamePressed.png");
    private Icon quitPressed = new ImageIcon("resources/quitPressed.png");
    private JLabel startGame = new JLabel(iconStart);
    private JLabel quitGame = new JLabel(iconQuit);
    private static String userName;

    private static Music acc;
    private static boolean bgm = true; // flag do musica fundo

    public StartFrame() {
        createMenu();

        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();// obter o tamanho da tela
        int centerX = screenSize.width/2;
        int centerY = screenSize.height/2;

        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage("resources/Cursor.png");
        Cursor cu = tk.createCustomCursor(img, new Point(10, 10), "stick");
        this.setCursor(cu);

        this.setTitle("Test");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(centerX-windowSizeX/2,centerY-windowSizeY/2);
        this.setSize(windowSizeX,windowSizeY);// cria janela e colocar no centro da tela

        JPanel jp = new JPanel();

        startGame.setBounds(windowSizeX/2-368/2-8,windowSizeY/3*2-99/2-31-65,368,99);
        startGame.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                startGame.setIcon(startPressed);
                Music.playBGM(Music.start);
            }
            public void mousePressed(MouseEvent e) {
                String name = JOptionPane.showInputDialog("Digite o nome do jogador：");
                if(name.equals("")){
                    userName = "jogador anônimo";
                }else{
                    userName = name;
                }
                dispose();
                try {
                    if (acc == null) {acc = new Music("music/musicaFundo.wav");}
                    if (bgm == true) {acc.loop();}
                    new JogoFrame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            public void mouseExited(MouseEvent e) {
                startGame.setIcon(iconStart);
            }
        });

        quitGame.setBounds(windowSizeX/2-368/2-8,windowSizeY/3*2-99/2-31+65,368,99);
        quitGame.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                quitGame.setIcon(quitPressed);
                Music.playBGM(Music.start);
            }
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
            public void mouseExited(MouseEvent e) {
                quitGame.setIcon(iconQuit);
            }
        });

        this.add(quitGame);
        this.add(startGame);
        this.add(jp);
        this.setVisible(true);
        this.requestFocusInWindow();

    }

    public void createMenu() {
        // Barra de menu
        JMenuBar jmb = new JMenuBar();
        // Item do menu
        JMenu jm2 = new JMenu("Historia");
        JMenu jm3 = new JMenu("Ajuda");
        // botão de item de menu
        JMenuItem jmi6 = new JMenuItem("Maior pontuação");
        JMenuItem jmi5 = new JMenuItem("Recorde de pontuação do jogador");
        JMenuItem jmi7 = new JMenuItem("Controle");
        JMenuItem jmi8 = new JMenuItem("Sobre jogo");
        // adicionar menu
        jmb.add(jm2);
        jmb.add(jm3);

        // Definir itens do menu do jogo
        jm2.add(jmi5);
        jm2.add(jmi6);
        jm3.add(jmi7);
        jm3.add(jmi8);

        // Adicionar evento de ouvinte ao botão
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

    @Override
    public void actionPerformed(ActionEvent e) {
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

    public static Music getAcc() {return acc;}
    public static boolean isBgm() {return bgm;}
    public static void setBgm(boolean bgm) {StartFrame.bgm = bgm;}
    public static String getUserName(){
        return userName;
    }

    public static void main(String[] args) {
        new StartFrame();
    }
}
