package utils;

import com.google.maps.model.LatLng;

import java.util.Random;

public class Utils {

    public static float distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    /*public static void getCurrentCoords() {

        LocalDateTime tA = LocalDateTime.now();
        double latA = 51.504870000000004;
        double lngA = -0.21533000000000002;
        double latB = 51.50475;
        double lngB = -0.21571;

        double coefLat = 180 / Math.PI / 6371000;
        double coefLng = coefLat / Math.cos(Math.PI * (latA + latB) / 360);

        double distLat = (latB - latA) / coefLat; // meters
        double distLng = (lngB - lngA) / coefLng; // meters

        double dist = Math.sqrt(distLat * distLat + distLng * distLng);
        System.out.println("distLat = " + distLat + "m; distLng = " + distLng + "m; full dist from A to B = " + dist + "m");
        double v = 1;

        double fullTime = dist / v; // seconds
        System.out.println("full time from A to B = " + fullTime + "s");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger("", ex.getLocalizedMessage());
        }

        LocalDateTime tX = LocalDateTime.now();
        long dT = Duration.between(tA, tX).getSeconds();
        double ratio = 1 / fullTime;

        double latX = latA + coefLat * ratio * distLat;
        double lngX = lngA + coefLng * ratio * distLng;

        System.out.println("Moving " + dT + " seconds; latX = " + latX + "; lngX = " + lngX);
    }*/

    /*public static LatLng getCurrentCoords(LatLng a, LatLng b) {
        double coefLat = 180 / Math.PI / 6371000;
        double coefLng = coefLat / Math.cos(Math.PI * (a.lat + b.lat) / 360);

        double distLat = (b.lat - a.lat) / coefLat; // meters
        double distLng = (b.lng - a.lng) / coefLng; // meters

        double dist = Math.sqrt(distLat * distLat + distLng * distLng);

        Random random = new Random();
        int v = random.nextInt(3);

        double fullTime = dist / v; // seconds

        double ratio = 1 / fullTime;

        double latX = a.lat + coefLat * ratio * distLat;
        double lngX = a.lng + coefLng * ratio * distLng;

        return new LatLng(latX, lngX);
    }*/
}
