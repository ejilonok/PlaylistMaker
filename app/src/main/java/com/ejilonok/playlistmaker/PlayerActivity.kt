package com.ejilonok.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private var binding : ActivityPlayerBinding? = null
    private var track : Track? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

        track = (application as PlaylistMakerApplication).actualTrack

        setContentView(binding?.root)
        bind()
    }

    private fun bind() {
        if (track == null) return
        val track = track as Track

        binding?.let {

            /* tested by
            val track = Gson().fromJson("{\"trackId\":111222333," +
                    "\"trackName\":\"Hello\"," +
                    "\"artistName\":\"Artist\"," +
                    "\"trackTimeMillis\":1000000," + /*"\"artworkUrl100\": \"\"," + "\"collectionName\": \"\"," + */
                    "\"releaseDate\":\"1999-87987-768768\"," +
                    "\"primaryGenreName\":\"Pop\"," +
                    "\"country\":\"USA\"}", Track::class.java) */


            it.playerTrackName.text = track.trackName
            it.playerTrackName.isSelected = true            // to start animation
            it.playerArtistName.text = track.artistName
            it.playerArtistName.isSelected = true           // to start animation
            it.playerTrackTimeTv.text = track.getTrackTimeString()

            bindCover()
            bindCollection()
            bindReleaseDate()
            bindGenre()
            bindCountry()

            it.playerBackButton.setOnClickListener {
                finish()
            }
        }
    }


    private fun bindCover() {
        binding?.let {
            Glide.with(it.root)
                .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: "")
                .placeholder(R.drawable.cover_placeholder)
                .centerCrop()
                .transform(RoundedCorners(GraphicUtils.dpToPx(8.0f, it.root)))
                .into(it.playerTrackCover)
        }
    }

    private fun bindCollection() {
        binding?.let {
            if (track?.collectionName.isNullOrEmpty()) {
                it.collectionGroup.visibility = View.GONE
            } else {
                it.playerCollectionNameTv.text = track!!.collectionName
                it.playerCollectionNameTv.isSelected = true
                it.collectionGroup.visibility = View.VISIBLE
            }
        }
    }

    private fun bindReleaseDate() {
        binding?.let {
            if (track?.releaseDate.isNullOrEmpty()) {
                it.yearGroup.visibility = View.GONE
            } else {
                it.yearGroup.visibility = View.VISIBLE
                it.playerYearTv.text = track!!.releaseDate!!.take(4)
            }
        }
    }

    private fun bindGenre() {
        binding?.let {
            if (track?.primaryGenreName.isNullOrEmpty()) {
                it.genreGroup.visibility = View.GONE
            } else {
                it.genreGroup.visibility = View.VISIBLE
                it.playerGenreTv.text = track!!.primaryGenreName!!
            }
        }
    }

    private fun bindCountry() {
        binding?.let {
            if (track?.country.isNullOrEmpty()) {
                it.countryGroup.visibility = View.GONE
            } else {
                it.countryGroup.visibility = View.VISIBLE
                it.playerCountryTv.text = track!!.country!!
            }
        }
    }
}