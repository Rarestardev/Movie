package com.rarestardev.movie.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rarestardev.movie.R;
import com.rarestardev.movie.databinding.ItemContainerEpisodesBinding;
import com.rarestardev.movie.model.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

    List<Episode> episodes;
    private LayoutInflater layoutInflater;

    public EpisodesAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemContainerEpisodesBinding itemContainerEpisodesBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_episodes, parent, false
        );

        return new EpisodeViewHolder(itemContainerEpisodesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        holder.bindEpisode(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder {

        ItemContainerEpisodesBinding binding;

        public EpisodeViewHolder(@NonNull ItemContainerEpisodesBinding itemContainerEpisodesBinding) {
            super(itemContainerEpisodesBinding.getRoot());
            this.binding = itemContainerEpisodesBinding;
        }

        public void bindEpisode(Episode episode) {
            String episodeNumber = episode.getEpisode();
            if (episodeNumber.length() == 1) {
                episodeNumber = "0".concat(episodeNumber);
            }

            binding.setEpisodeName(episode.getName());
            binding.setEpisodeAirDate(episode.getAirDate());
            binding.setEpisode(episode.getEpisode());
            binding.rootLayout.setOnClickListener(view ->
                    Toast.makeText(view.getContext(), "No link", Toast.LENGTH_SHORT).show());
        }
    }
}
