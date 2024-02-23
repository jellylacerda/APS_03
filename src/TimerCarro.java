
public class TimerCarro extends Thread{
    private static volatile boolean created = false;
    private static volatile long chanceCria = 1000;
    public void run() {
        while (JogoFrame.isRoboAlive()) {
            while (JogoFrame.isPause()) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                // Altere a velocidade dos carros gerados com base no nível do jogo
                Thread.sleep(chanceCria);
                JogoFrame.getCarros().add(new Carro());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setChanceCria(long chance) {TimerCarro.chanceCria = chance;}

    public static void reduceChance() {TimerCarro.chanceCria -= 80;}

    public static boolean isCreated() {
        return created;
    }

    public static void setCreated(boolean created) {
        TimerCarro.created = created;
    }
}