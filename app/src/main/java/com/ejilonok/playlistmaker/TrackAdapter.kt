package com.ejilonok.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ejilonok.playlistmaker.databinding.TrackCardBinding

class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {
    val tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = TrackCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}