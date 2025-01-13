package com.ejilonok.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private var binding : ActivityPlayerBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

        if ((application as PlaylistMakerApplication).actualTrack == null) return
        binding?.let {
            val track = (application as PlaylistMakerApplication).actualTrack as Track

            it.playerTrackName.text = track.trackName
            it.playerArtistName.text = track.artistName
            it.playerTrackTimeTv.text = track.getTrackTimeString()
            Glide.with(it.root)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.cover_placeholder)
                .centerCrop()
                .transform(RoundedCorners(GraphicUtils.dpToPx(8.0f, it.root)))
                .into(it.playerTrackCover)

            it.playerTrackName.isSelected = true
            it.playerArtistName.isSelected = true

            if (track.collectionName.isEmpty()) {
                it.collectionGroup.visibility = View.GONE
            } else {
                it.playerCollectionNameTv.text = track.collectionName
                it.playerCollectionNameTv.isSelected = true
                it.collectionGroup.visibility = View.VISIBLE
            }

            if (track.releaseDate.isEmpty()) {
                it.yearGroup.visibility = View.GONE
            } else {
                it.yearGroup.visibility = View.VISIBLE
                it.playerYearTv.text = track.releaseDate!!.take(4)
            }

            if (track.primaryGenreName.isEmpty()) {
                it.genreGroup.visibility = View.GONE
            } else {
                it.genreGroup.visibility = View.VISIBLE
                it.playerGenreTv.text = track.primaryGenreName
            }

            if (track.country.isEmpty()) {
                it.countryGroup.visibility = View.GONE
            } else {
                it.countryGroup.visibility = View.VISIBLE
                it.playerCountryTv.text = track.country
            }

            it.playerBackButton.setOnClickListener {
                finish()
            }

            setContentView(it.root)
        }
    }

}