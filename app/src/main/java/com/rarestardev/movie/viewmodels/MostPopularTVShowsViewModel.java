package com.rarestardev.movie.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rarestardev.movie.repositories.MostPopularTVShowsRepository;
import com.rarestardev.movie.responses.TVShowsResponse;

public class MostPopularTVShowsViewModel extends ViewModel {

    private MostPopularTVShowsRepository mostPopularTVShowsRepository;

    public MostPopularTVShowsViewModel (){
        mostPopularTVShowsRepository = new MostPopularTVShowsRepository();
    }

    public LiveData<TVShowsResponse> getMostPopularTVShows (int page){
        return mostPopularTVShowsRepository.getMostPopularTVShows(page);
    }
}
