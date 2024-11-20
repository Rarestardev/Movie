package com.rarestardev.movie.listener;


import com.rarestardev.movie.model.TVShow;

public interface WatchlistListener {

    void onTVShowClicked(TVShow tvShow);

    void removeTVShowFromWatchlist(TVShow tvShow,int position);
}
