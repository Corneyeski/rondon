import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import entities.Rondon;
import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    private static Rondon rondon = new Rondon();

    private static LatLng buckingham = new LatLng(51.501299, -0.141935);
    private static LatLng templeStation = new LatLng(51.510852, -0.114165);


    private static EncodedPolyline polyline = new EncodedPolyline("mpjyHx`i@VjAVKnAh@BHHX@LZR@Bj@Ml@WWc@]w@bAyAfBmCb@o@pLeQfCsDVa@@ODQR}AJ{A?{BGu\n" +
            "AD_@FKb@MTUX]Le@^kBVcAVo@Ta@|EaFh@m@FWaA{DCo@q@mCm@cC{A_GWeA}@sGSeAcA_EOSMa\n" +
            "@}A_GsAwFkAiEoAaFaBoEGo@]_AIWW{AQyAUyBQqAI_BFkEd@aHZcDlAyJLaBPqDDeD?mBEiA}@F]yKWqGSkI\n" +
            "CmCIeZIuZi@_Sw@{WgAoXS{DOcAWq@KQGIFQDGn@Y`@MJEFIHyAVQVOJGHgFRJBBCCSKBcAKoACyA?m@^y\n" +
            "VJmLJ{FGGWq@e@eBIe@Ei@?q@Bk@Hs@Le@Rk@gCuIkJcZsDwLd@g@Oe@o@mB{BgHQYq@qBQYOMSM\n" +
            "GBUBGCYc@E_@H]DWJST?JFFHBDNBJ?LED?LBv@WfAc@@EDGNK|@e@hAa@`Bk@b@OEk@Go@IeACoA@\n" +
            "a@PyB`@yDDc@e@K{Bi@oA_@w@]m@_@]QkBoAwC{BmAeAo@s@uAoB_AaBmAwCa@mAo@iCgAwFg@iD\n" +
            "q@}G[uEU_GBuP@cICmA?eI?qCB{FBkCI}BOyCMiAGcAC{AN{YFqD^}FR}CNu@JcAHu@b@_E`@}DVsB^mBTsAQ\n" +
            "KkCmAg@[YQOIOvAi@[m@e@s@g@GKCKAEJIn@g@GYGIc@ScBoAf@{A`@uAlBfAG`@");


    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1000);

    private static ScheduledFuture<?> taskRun = null;
    private static ScheduledFuture<?> taskReport = null;

    private static int meters = 0;

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int option = 0;
        while (option != 4) {
            //To pick an option, introduce the number
            //If there's no polyline introduce, the robot will run with the default
            printOptions();

            option = Integer.parseInt(reader.readLine());

            switch (option) {
                case 1:
                    if (taskRun == null || taskRun.isCancelled()){
                        taskRun = scheduler.scheduleAtFixedRate(Main::roboMove, 1, 1, TimeUnit.SECONDS);
                        taskReport = scheduler.scheduleAtFixedRate(Main::roport, 15, 15, TimeUnit.MINUTES);
                        System.out.println("Robot is now running");
                    }
                    else {
                        taskRun.cancel(true);
                        taskReport.cancel(true);
                        System.out.println("Robot stop");
                    }
                    break;
                case 2:
                    System.out.println("Introduce the new polyline (make sure there is no spaces):");
                    polyline= new EncodedPolyline(reader.readLine());
                    break;
                case 3:
                    roport();
                    break;
                case 4:
                    if(taskRun != null || !taskRun.isCancelled()){
                        taskRun.cancel(true);
                        taskReport.cancel(true);
                    }
                    break;
                default:
                    break;
            }

        }
    }

    private static void roboMove() {

        List<LatLng> positions = polyline.decodePath();

        for(int i = 0; i < positions.size()-1; i++) {

            rondon.setLocation(positions.get(i));
            LatLng nextStop = positions.get(i+1);

            float distanceDone = 0;
            float distance = Utils.distance(rondon.getLocation().lat, rondon.getLocation().lng, nextStop.lat, nextStop.lng);

            do{

                LatLng currentPosition = rondon.getLocation();

                double coefLat = 180 / Math.PI / 6371000;
                double coefLng = coefLat / Math.cos(Math.PI * (currentPosition.lat + nextStop.lat) / 360);

                double distLat = (nextStop.lat - currentPosition.lat) / coefLat; // meters
                double distLng = (nextStop.lng - currentPosition.lng) / coefLng; // meters

                double dist = Math.sqrt(distLat * distLat + distLng * distLng);

                Random random = new Random();
                int v = random.nextInt(3) + 1;
                meters = meters + v;
                distanceDone = distanceDone + v;

                if(meters >= 100){
                    rondon.setAverage();
                    meters = 0;
                    System.out.println("measure of PM 2.5 made");
                }

                double fullTime = dist / v; // seconds

                double ratio = 1 / fullTime;

                rondon.setLocation(new LatLng((float) (currentPosition.lat + coefLat * ratio * distLat), (float) currentPosition.lng + coefLng * ratio * distLng));

                checkpoints();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }while (distanceDone < distance);

        }
        System.out.println("route finished");
        printOptions();
        taskRun.cancel(false);
        taskReport.cancel(false);
    }

    private static void checkpoints(){
        if(Utils.distance(rondon.getLocation().lat, rondon.getLocation().lng, buckingham.lat, buckingham.lng) <= 100){
            rondon.setSource(" buckingham palace");
            roport();
        }else if(Utils.distance(rondon.getLocation().lat, rondon.getLocation().lng, templeStation.lat, templeStation.lng) <= 100){
            rondon.setSource("Temple station");
            roport();
        }
        rondon.setSource("robot");
    }

    private static void roport(){
        System.out.println(rondon.toString());
    }

    private static void printOptions(){
        System.out.println("Choose an option");
        System.out.println("1 - start/stop");
        System.out.println("2 - enter a new polyline (re-route)");
        System.out.println("3 - report");
        System.out.println("4 - exit");
    }
}
