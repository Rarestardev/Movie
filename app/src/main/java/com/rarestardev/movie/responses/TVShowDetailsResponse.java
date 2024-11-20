package com.rarestardev.movie.responses;

import com.google.gson.annotations.SerializedName;
import com.rarestardev.movie.model.TVShowsDetails;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    private TVShowsDetails tvShowsDetails;

    public TVShowsDetails getTvShowsDetails() {
        return tvShowsDetails;
    }
}
