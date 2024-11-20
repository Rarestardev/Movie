package com.rarestardev.movie.tab_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rarestardev.movie.activities.ProfileActivity;
import com.rarestardev.movie.activities.SignInActivity;
import com.rarestardev.movie.activities.TVShowDetailsActivity;
import com.rarestardev.movie.adapters.TVShowsAdapter;
import com.rarestardev.movie.databinding.FragmentHomeBinding;
import com.rarestardev.movie.model.TVShow;
import com.rarestardev.movie.utilities.Constants;
import com.rarestardev.movie.utilities.SecurePreferences;
import com.rarestardev.movie.viewmodels.MostPopularTVShowsViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private final List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private MostPopularTVShowsViewModel viewModel;
    private SecurePreferences securePreferences;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String mParam1;
    String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    @NonNull
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        doInitialization();

        SetupUserAccount();

        return binding.getRoot();
    }

    private void SetupUserAccount() {
        securePreferences = new SecurePreferences();
        String username = securePreferences.getSecureString(getContext(), Constants.SHARED_PREF_NAME,Constants.SHARED_PREF_KEY_USERNAME);
        String photoUrl = securePreferences.getSecureString(getContext(), Constants.SHARED_PREF_NAME,Constants.SHARED_PREF_KEY_PHOTO);

        if (!username.isEmpty() && !photoUrl.isEmpty()){
            binding.setUsername(username);
            Picasso.get().load(photoUrl).into(binding.profileImageView);
        }

        binding.profileImageView.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            intent.putExtra("Username",username);
            intent.putExtra("Photo",photoUrl);
            startActivity(intent);
        });
    }

    private void doInitialization (){
        binding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows, tvShow -> {
            Intent intent = new Intent(getContext() , TVShowDetailsActivity.class);
            intent.putExtra("tvShow",tvShow);
            startActivity(intent);
        });
        binding.tvShowRecyclerView.setAdapter(tvShowsAdapter);
        binding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.tvShowRecyclerView.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePages){
                        currentPage += 1;
                        getMostPopularTVShows();
                    }
                }
            }
        });

        getMostPopularTVShows();
    }

    private void getMostPopularTVShows (){
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(getViewLifecycleOwner(),mostPopularTVShowsResponse -> {
            toggleLoading();
            if (mostPopularTVShowsResponse != null){
                totalAvailablePages = mostPopularTVShowsResponse.getTotalPages();
                if (mostPopularTVShowsResponse.getTvShows() != null){
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTVShowsResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount,tvShows.size());
                }
            }
        });
    }

    private void toggleLoading(){
        if (currentPage == 1){
            binding.setIsLoading(binding.getIsLoading() == null || !binding.getIsLoading());
        }else {
            binding.setIsLoadingMore(binding.getIsLoadingMore() == null || !binding.getIsLoadingMore());
        }
    }
}