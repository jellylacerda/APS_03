import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Music {
    /** carro e robo batendo */
    public final static String explode = "music/explode.wav";

    /** coleta lixo */
    public final static String coletaLixo = "music/coletaLixo.wav";

    /** musica fundo */
    public final static String bgMusic = "music/musicaFundo.wav";

    /** esvazie lixo */
    public final static String esvazieLixo = "music/recycle.wav";

    /** atualizar robo */
    public final static String update = "music/update.wav";

    /** buttom pressed */
    public final static String start = "music/bottom.wav";
    private Clip clip;
    public Music(String fileName) {
        try {
            File file = new File(fileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            // carregar o som na memória
            clip = AudioSystem.getClip();
            clip.open(sound);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    /*public void play(){
        clip.setFramePosition(0);
        clip.start();
    }*/
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }

    public static void playBGM(String sound) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(sound));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start(); // toca o som
        } catch(Exception exc) {
            System.out.println(exc);
        }
    }
}