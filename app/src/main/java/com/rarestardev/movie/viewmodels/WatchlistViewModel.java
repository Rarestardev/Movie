package com.rarestardev.movie.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.rarestardev.movie.database.TVShowDatabase;
import com.rarestardev.movie.model.TVShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public class WatchlistViewModel extends AndroidViewModel {

    private TVShowDatabase tvShowDatabase;

    public WatchlistViewModel (@NonNull Application application){
        super(application);
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }

    public Flowable<List<TVShow>> loadWatchlist(){
        return tvShowDatabase.tvShowDao().getWatchlist();
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchlist(tvShow);
    }
}
