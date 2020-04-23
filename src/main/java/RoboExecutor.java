import entities.Robot;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoboExecutor extends Thread {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    private int meters = 0;

    @Override
    public void run() {

        System.out.println("EXECUTED");

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                roboMove();
            }
        }, 1, 1, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                roboReport();
            }
        }, 0, 15, TimeUnit.MINUTES);


    }

    private void roboMove() {
        System.out.println("goes in");
    }

    private void roboReport() {
        System.out.println(Robot.getInstance().toString());
    }

    private int generatePM(){
        Random random = new Random();
        return random.nextInt(200);
    }
}
