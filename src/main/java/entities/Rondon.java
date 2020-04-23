package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.LatLng;

import java.util.Date;
import java.util.Random;

public class Rondon {

    private Long timestamp;
    private LatLng location;
    private transient int averagePM25;
    private transient int totalPM25;
    private transient int numberMesures;
    private String levelPM25;
    private String source;

    public Rondon() {
        timestamp = new Date().getTime();
        averagePM25 = 0;
        totalPM25 = 0;
        numberMesures = 0;
        levelPM25 = "N/A";
        location = new LatLng();
        source = "robot";
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public int getAveragePM25() {
        return averagePM25;
    }

    public void setAveragePM25(int averagePM25) {
        this.averagePM25 = averagePM25;
    }

    public int getTotalPM25() {
        return totalPM25;
    }

    public void setTotalPM25(int totalPM25) {
        this.totalPM25 = totalPM25;
    }

    public int getNumberMesures() {
        return numberMesures;
    }

    public void setNumberMesures(int numberMesures) {
        this.numberMesures = numberMesures;
    }

    public String getLevelPM25() {
        return levelPM25;
    }

    public void setLevelPM25(String levelPM25) {
        this.levelPM25 = levelPM25;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setAverage(){
        Random random = new Random();
        int measure = random.nextInt(200);

        numberMesures++;
        totalPM25 = totalPM25 + measure;
        averagePM25 = totalPM25 / numberMesures;

        if(averagePM25 <= 50)
            levelPM25 = "Good";
        else if(averagePM25 <= 100)
            levelPM25 = "Moderate";
        else if(averagePM25 <= 150)
            levelPM25 = "USG";
        else levelPM25 = "unhealthy";
    }

    @Override
    public String toString() {
        this.timestamp = new Date().getTime();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
