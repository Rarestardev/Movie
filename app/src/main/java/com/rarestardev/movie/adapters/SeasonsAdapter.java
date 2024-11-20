package com.rarestardev.movie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rarestardev.movie.R;
import com.rarestardev.movie.databinding.ItemContainerSeasonBinding;
import com.rarestardev.movie.model.Episode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.SeasonViewHolder> {

    List<Episode> episodes;
    private LayoutInflater layoutInflater;
    Context context;

    public SeasonsAdapter(List<Episode> episodes, Context context) {
        this.episodes = new ArrayList<>(new HashSet<>(episodes));
        this.context = context;
    }

    @NonNull
    @Override
    public SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSeasonBinding itemContainerSeasonBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_season, parent, false
        );
        return new SeasonViewHolder(itemContainerSeasonBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonViewHolder holder, int position) {
        holder.bindSeason(episodes.get(position));

        holder.itemContainerSeasonBinding.seasonClickLayout.setOnClickListener(view -> {
            if (holder.itemContainerSeasonBinding.recyclerView.getVisibility() == View.GONE) {
                holder.itemContainerSeasonBinding.recyclerView.setVisibility(View.VISIBLE);
                holder.itemContainerSeasonBinding.icon.setRotation(90.0f);
                EpisodesAdapter episodesAdapter = new EpisodesAdapter(episodes);
                holder.itemContainerSeasonBinding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                holder.itemContainerSeasonBinding.recyclerView.setAdapter(episodesAdapter);
            } else {
                holder.itemContainerSeasonBinding.recyclerView.setVisibility(View.GONE);
                holder.itemContainerSeasonBinding.icon.setRotation(0.0f);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }


    public static class SeasonViewHolder extends RecyclerView.ViewHolder {

        ItemContainerSeasonBinding itemContainerSeasonBinding;

        public SeasonViewHolder(ItemContainerSeasonBinding itemContainerSeasonBinding) {
            super(itemContainerSeasonBinding.getRoot());
            this.itemContainerSeasonBinding = itemContainerSeasonBinding;
        }

        public void bindSeason(Episode episode) {
            String season = episode.getSeason();
            if (season.length() == 1) {
                season = "0".concat(season);
            }

            itemContainerSeasonBinding.setSeason("Season : " + season);
        }
    }
}
