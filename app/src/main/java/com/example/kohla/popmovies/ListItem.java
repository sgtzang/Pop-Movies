package com.example.kohla.popmovies;

/**
 * Created by kohla on 2015-12-01.
 */
public class ListItem {

    private String trailerName;
    private String youtubeKey;

    public ListItem() {
        super();
    }

    public String getTailerName() { return trailerName;}
    public void setTrailerName(String trailerName) { this.trailerName = trailerName;}

    public String getYoutubeKey() { return youtubeKey;}
    public void setYoutubeKey(String youtubeKey) { this.youtubeKey = youtubeKey;}
}
