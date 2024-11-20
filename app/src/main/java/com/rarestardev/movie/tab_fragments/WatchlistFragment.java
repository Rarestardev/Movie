package com.rarestardev.movie.tab_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rarestardev.movie.activities.TVShowDetailsActivity;
import com.rarestardev.movie.adapters.WatchlistAdapter;
import com.rarestardev.movie.databinding.FragmentWatchlistBinding;
import com.rarestardev.movie.listener.WatchlistListener;
import com.rarestardev.movie.model.TVShow;
import com.rarestardev.movie.utilities.TempDataHolder;
import com.rarestardev.movie.viewmodels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchlistFragment extends Fragment {

    private FragmentWatchlistBinding binding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String mParam1;
    String mParam2;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchlistFragment newInstance(String param1, String param2) {
        WatchlistFragment fragment = new WatchlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false);

        doInitialization();

        return binding.getRoot();
    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlist = new ArrayList<>();
        loadWatchlist();

    }

    private void loadWatchlist() {
        binding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    binding.setIsLoading(false);
                    if (!watchlist.isEmpty()) {
                        watchlist.clear();
                    }
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchlist, new WatchlistListener() {
                        @Override
                        public void onTVShowClicked(TVShow tvShow) {
                            Intent intent = new Intent(getContext(), TVShowDetailsActivity.class);
                            intent.putExtra("tvShow", tvShow);
                            startActivity(intent);
                        }

                        @Override
                        public void removeTVShowFromWatchlist(TVShow tvShow, int position) {
                            CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
                            compositeDisposableForDelete.add(viewModel.removeTVShowFromWatchlist(tvShow)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        watchlist.remove(position);
                                        watchlistAdapter.notifyItemRemoved(position);
                                        watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
                                        compositeDisposableForDelete.dispose();
                                    }));
                        }
                    });
                    binding.watchlistRecyclerView.setAdapter(watchlistAdapter);
                    binding.watchlistRecyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATED) {
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }
    }
}