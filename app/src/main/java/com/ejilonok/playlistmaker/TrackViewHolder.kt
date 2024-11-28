package com.ejilonok.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackCover: ImageView = itemView.findViewById(R.id.track_cover)

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            itemView.context.resources.displayMetrics).toInt()
    }
    fun bind(model : Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime

//        val newWidth = itemView.width - dpToPx(66.0f + 44.0f)
//        trackName.width = newWidth
//        artistName.width = newWidth - trackTime.width

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2.0f)))
            .into(trackCover)
    }
}