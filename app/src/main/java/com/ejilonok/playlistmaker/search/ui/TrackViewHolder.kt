package com.ejilonok.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
//TODO Зависимость от glide лучше перенести с ui слоя?
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.main.ui.common.GraphicUtils
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.ejilonok.playlistmaker.databinding.TrackCardBinding

class TrackViewHolder(private var binding: TrackCardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model : Track) {
        binding.let {
            it.trackName.text = model.trackName
            it.artistName.text = model.artistName
            it.trackTime.text =
                model.trackTime

            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.cover_placeholder)
                .centerCrop()
                .transform(RoundedCorners(GraphicUtils.dpToPx(2.0f, it.root)))
                .into(it.trackCover)
        }
    }
}