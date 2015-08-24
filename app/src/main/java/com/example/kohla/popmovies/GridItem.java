package com.example.kohla.popmovies;

/**
 * Created by kohla on 2015-08-24.
 */
public class GridItem {
    private String image;
    private String title;
    private String releaseDate;
    private String overView;
    private String voteAverage;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title;  }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate;}

    public String getOverView() { return overView;}
    public void setOverView(String overView) { this.overView = overView;}

    public String getVoteAverage() { return voteAverage; }
    public void setVoteAverage(String voteAverage) { this.voteAverage = voteAverage;  }
}
