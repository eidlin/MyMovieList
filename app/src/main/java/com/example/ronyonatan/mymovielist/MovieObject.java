package com.example.ronyonatan.mymovielist;

/**
 * Created by ronYonatan on 2/13/2017.
 */

public class MovieObject {
    String title;
    String plot;
    String poster;
    String imdbID;

    public MovieObject(String title, String imdbID) {
        this.title = title;
        this.imdbID = imdbID;

    }

    public MovieObject(String title, String plot, String poster) {
        this.title = title;
        this.plot = plot;
        this.poster = poster;
    }

    @Override
    public String toString() {
        return title;
    }
}
