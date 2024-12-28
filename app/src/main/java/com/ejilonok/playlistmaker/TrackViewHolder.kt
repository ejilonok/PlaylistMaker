package com.ejilonok.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.databinding.TrackCardBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(var binding: TrackCardBinding) : RecyclerView.ViewHolder(binding.root) {
    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            binding.root.context.resources.displayMetrics).toInt()
    }
    fun bind(model : Track) {
        binding.let {
            it.trackName.text = model.trackName
            it.artistName.text = model.artistName
            it.trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)

            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.cover_placeholder)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2.0f)))
                .into(it.trackCover)
        }
    }
}