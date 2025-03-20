package com.ejilonok.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.ejilonok.playlistmaker.databinding.TrackCardBinding

class TrackAdapter(private var tracks : List<Track> = arrayListOf(), private val trackClickListener: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = TrackCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            trackClickListener(tracks[position])
        }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}