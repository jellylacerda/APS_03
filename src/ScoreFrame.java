import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

import javax.print.attribute.standard.OutputDeviceAssigned;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ScoreFrame extends JFrame {
    private static String fileName = "text/Score.txt";

    public static File getFile() {
        return file;
    }

    private static File file = new File(fileName);
    private JPanel jpNorth = new JPanel();
    private JLabel jl = new JLabel("Registro Histórico");
    private static JTextArea jta =new JTextArea(11,34);
    private JScrollPane jspCenter = new JScrollPane(jta);

    public ScoreFrame() throws IOException{
        setLayout(new BorderLayout());
        jpNorth.add(jl);
        jl.setFont(new Font("Arial",1,20));
        jpNorth.setLayout(new FlowLayout());

        jta.setEditable(false);
        add(jpNorth,BorderLayout.NORTH);
        add(jspCenter,BorderLayout.CENTER);
        if(!file.exists()) { //Criar um arquivo se ele não existir
            file.createNewFile();
        }
        FileReader fReader =new FileReader(fileName);
        BufferedReader bReader =new BufferedReader(fReader);
        ArrayList<String> scoreList =new ArrayList<String>();
        String string;
        while((string=bReader.readLine())!=null){
            scoreList.add(string);
        }
        for (int i = 0; i <scoreList.size(); i++) {
            jta.append(scoreList.get(i)+"\n");
        }

        setSize(450, 300);
        setVisible(true);
        setResizable(false);
        setTitle("Score");
        this.setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                setVisible(false);
                jta.setText("");
            }
        });
    }
}