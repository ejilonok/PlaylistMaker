package com.ejilonok.playlistmaker

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.databinding.TrackCardBinding

class TrackViewHolder(private var binding: TrackCardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model : Track) {
        binding.let {
            it.trackName.text = model.trackName
            it.artistName.text = model.artistName
            it.trackTime.text =
                model.getTrackTimeString()

            Glide.with(itemView)
                .load(if (model.artworkUrl100.isNullOrEmpty()) "" else model.artworkUrl100)
                .placeholder(R.drawable.cover_placeholder)
                .centerCrop()
                .transform(RoundedCorners(GraphicUtils.dpToPx(2.0f, it.root)))
                .into(it.trackCover)
        }
    }
}