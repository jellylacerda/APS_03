
public class TimerJogo extends Thread{
    private static  volatile boolean created = false;
    private static volatile int time = 45;

    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (time > 0&&JogoFrame.isRoboAlive()) {
            while (JogoFrame.isPause()) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            time--;
            try {
                Thread.sleep(1000);
                if (time<=0) {
                    time = 0;
                    JogoFrame.setRoboAlive(false);
                }
                int hh = time / 60 / 60 % 60;
                int mm = time / 60 % 60;
                int ss = time % 60;
                String timeString = hh+":"+mm+":"+ss;
                EstadoLabel.setTime(timeString);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getTime() {
        return TimerJogo.time;
    }

    public static void setTime(int time) {
        TimerJogo.time = time;
    }

    public static void addTime() {
        TimerJogo.time++;
    }

    public static void addTime10() {
        TimerJogo.time += 10;
    }

    public static void reduceTime() {
        TimerJogo.time -= EstadoLabel.getLevel() * 2;
    }

    public static boolean isCreated() {
        return created;
    }

    public static void setCreated(boolean created) {
        TimerJogo.created = created;
    }
}
