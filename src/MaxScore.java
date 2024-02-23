import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class MaxScore {
    private static String maxString;
    private static String fileName = "text/maxScore.txt";
    private static File file = new File(fileName);

    public static String readTxt(){
        BufferedReader reader = null;
        try {
            if(!file.exists()) { //Criar um arquivo se ele não existir
                file.createNewFile();
                write(null,0,1);
            }
            reader = new BufferedReader(new FileReader(file));
            String temp = null;
            // Leia uma linha de cada vez até que null seja lido como final do arquivo
            while ((temp = reader.readLine()) != null) {
                maxString = temp;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {

                }
            }
        }
        return maxString;
    }

    public static void write(String username,int score,int level){
        if (username==null) {
            username = "jogador anônimo";
        }
        String scoreString = username + ":" + score + ":" + level;
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println("Este documento é gerado automaticamente pelo jogo, pode ser deletado mas ainda assim será " +
                    "gerado. Eu posso finalmente ser um ladino de software uma vez, rsrs");//escreve string no arquivo
            ps.append(scoreString).append("\n");
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
