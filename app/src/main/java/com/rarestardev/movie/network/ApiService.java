package com.rarestardev.movie.network;


import com.rarestardev.movie.responses.TVShowDetailsResponse;
import com.rarestardev.movie.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTVShows(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailsResponse> getTVShowDetails(@Query("q") String tvShowId);

    @GET("search")
    Call<TVShowsResponse> searchTVShow(@Query("q") String query , @Query("page") int page);
}
