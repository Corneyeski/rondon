package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.LatLng;

public class Robot {

    private static Robot robot = null;

    public float lat;
    public float lng;
    public transient int averagePM25;
    public transient int totalPM25;
    public transient int numberMesures;
    public String levelPM25;
    public LatLng location;

    private Robot(){
        lat = 0;
        lng = 0;
        averagePM25 = 0;
        totalPM25 = 0;
        numberMesures = 0;
        levelPM25 = "";
        location = new LatLng();
    }

    private static class LazyHolder {
        private static final Robot robot = new Robot();
    }

    public static Robot getInstance() {
        if (robot == null)
            robot = new Robot();

        return LazyHolder.robot;
    }

    public void setAverage(int lastMesure){
        numberMesures++;
        totalPM25 = totalPM25 + lastMesure;
        averagePM25 = totalPM25 / numberMesures;

        if(averagePM25 <= 50)
            levelPM25 = "Good";
        else if(averagePM25 > 50 && averagePM25 <= 100)
            levelPM25 = "Moderate";
        else if(averagePM25 > 100 && averagePM25 <= 150)
            levelPM25 = "USG";
        else levelPM25 = "unhealthy";
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(getInstance());
    }
}
