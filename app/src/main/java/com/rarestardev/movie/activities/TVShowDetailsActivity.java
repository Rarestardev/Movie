package com.rarestardev.movie.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rarestardev.movie.R;
import com.rarestardev.movie.adapters.SeasonsAdapter;
import com.rarestardev.movie.adapters.ImageSliderAdapter;
import com.rarestardev.movie.databinding.ActivityTvshowDetailsBinding;
import com.rarestardev.movie.model.TVShow;
import com.rarestardev.movie.responses.TVShowDetailsResponse;
import com.rarestardev.movie.utilities.TempDataHolder;
import com.rarestardev.movie.viewmodels.TVShowDetailsViewModel;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding binding;

    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private TVShow tvShow;
    private Boolean isTVShowAvailableInWatchlist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);

        doInitialization();
    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        binding.imageBack.setOnClickListener(v -> this.finish());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    private void checkTVShowInWatchlist() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTVShowAvailableInWatchlist = true;
                    binding.imageWatchlist.setImageResource(R.drawable.baseline_bookmark_added_24);
                    compositeDisposable.dispose();
                })
        );
    }

    private void getTVShowDetails() {
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowDetailsResponse -> {
                    if (tvShowDetailsResponse.getTvShowsDetails() != null) {
                        if (tvShowDetailsResponse.getTvShowsDetails().getPictures() != null) {
                            loadImageSlider(tvShowDetailsResponse.getTvShowsDetails().getPictures());
                        }
                        binding.setTvShowImageURL(
                                tvShowDetailsResponse.getTvShowsDetails().getImagePath()
                        );
                        binding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowDetailsResponse.getTvShowsDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        binding.textReadMore.setOnClickListener(v -> {
                            if (binding.textReadMore.getText().toString().equals(getString(R.string.readMore))) {
                                binding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                binding.textDescription.setEllipsize(null);
                                binding.textReadMore.setText(R.string.read_less);
                            } else {
                                binding.textDescription.setMaxLines(4);
                                binding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                binding.textReadMore.setText(R.string.readMore);
                            }
                        });
                        binding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailsResponse.getTvShowsDetails().getRating())
                                )
                        );
                        if (tvShowDetailsResponse.getTvShowsDetails().getGenres() != null) {
                            binding.setGenre(tvShowDetailsResponse.getTvShowsDetails().getGenres()[0]);
                        } else {
                            binding.setGenre("N/A");
                        }
                        binding.setRuntime(tvShowDetailsResponse.getTvShowsDetails().getRuntime() + "Min");
                        binding.buttonWebsite.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowsDetails().getUrl()));
                            startActivity(intent);
                        });

                        setEpisodeTvShow(tvShowDetailsResponse);

                        binding.imageWatchlist.setOnClickListener(v -> {
                            CompositeDisposable compositeDisposable = new CompositeDisposable();
                            if (isTVShowAvailableInWatchlist) {
                                compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            isTVShowAvailableInWatchlist = false;
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            binding.imageWatchlist.setImageResource(R.drawable.baseline_bookmark_border_24);
                                            Toast.makeText(getApplicationContext(), "Removed from watchlist", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        })
                                );
                            } else {
                                compositeDisposable.add(tvShowDetailsViewModel.addToWatchlist(tvShow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            binding.imageWatchlist.setImageResource(R.drawable.baseline_bookmark_added_24);
                                            Toast.makeText(TVShowDetailsActivity.this, "Added To watchlist", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        })
                                );
                            }
                        });
                        loadBasicTVShowDetails();
                    }
                }
        );
    }

    private void setEpisodeTvShow(TVShowDetailsResponse tvShowDetailsResponse) {
        SeasonsAdapter seasonsAdapter = new SeasonsAdapter(tvShowDetailsResponse.getTvShowsDetails().getEpisodes(),TVShowDetailsActivity.this);
        binding.recyclerViewEpisode.setLayoutManager(new LinearLayoutManager(TVShowDetailsActivity.this));
        binding.recyclerViewEpisode.setAdapter(seasonsAdapter);
    }

    private void loadImageSlider(String[] sliderImages) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(sliderImages);
        binding.recyclerView.setAdapter(imageSliderAdapter);
        binding.recyclerView.setHasFixedSize(true);
    }

    private void loadBasicTVShowDetails() {
        binding.setTvShowName(tvShow.getName());
        binding.setNetworkCountry(
                tvShow.getNetwork() + " (" + tvShow.getCountry() + ")"
        );
        binding.setStatus(tvShow.getStatus());
        binding.setStartDate(tvShow.getStartDate());
    }
}