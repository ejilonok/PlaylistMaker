package com.ejilonok.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private var binding : ActivityPlayerBinding? = null
    private var track : Track? = null
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val playerRunnable = Runnable { updatePlayerInfo() }
    private var savedPosition = DEFAULT_POSITION

    companion object {
        private const val STATE_PAUSED = 0
        private const val STATE_PLAY = 1
        private const val STATE_STOP = 2

        private const val DELAY_UPDATE = 250L

        private const val TAG_PLAYER_STATE = "MEDIA_PLAYER_STATE"
        private const val TAG_PLAYER_POSITION = "MEDIA_PLAYER_POSITION"

        private const val DEFAULT_STATE = STATE_PAUSED
        private const val DEFAULT_POSITION = 0
    }

    private var currentState = STATE_PAUSED
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

        track = (application as PlaylistMakerApplication).actualTrack

        setContentView(binding?.root)
        binding?.playButton?.isEnabled = false

        loadPlayerData(savedInstanceState)
        bind()
    }

    override fun onPause() {
        super.onPause()

        if (mediaPlayer.isPlaying) {
            changeState()
        }
    }

    private fun loadPlayerData(savedInstanceState: Bundle?) {
        currentState = savedInstanceState?.getInt(TAG_PLAYER_STATE) ?: DEFAULT_STATE
        savedPosition = savedInstanceState?.getInt(TAG_PLAYER_POSITION) ?: DEFAULT_POSITION
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
        outState.putInt( TAG_PLAYER_STATE , currentState )
        outState.putInt( TAG_PLAYER_POSITION, mediaPlayer.currentPosition )
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)

        loadPlayerData(savedInstanceState)
    }

    private fun bind() {
        if (track == null) return
        val track = track as Track

        binding?.let { binding ->

            binding.playerTrackName.text = track.trackName
            binding.playerTrackName.isSelected = true            // to start animation
            binding.playerArtistName.text = track.artistName
            binding.playerArtistName.isSelected = true           // to start animation
            binding.playerTrackTimeTv.text = track.getTrackTimeString()

            bindCover()
            bindCollection()
            bindReleaseDate()
            bindGenre()
            bindCountry()

            binding.playerBackButton.setOnClickListener {
                finish()
            }

            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.setOnPreparedListener {
                binding.playButton.isEnabled = true
                mediaPlayer.seekTo(savedPosition)
                if (currentState == STATE_PLAY) changeState()
            }
            binding.playButton.setOnClickListener { changeState() }
            mediaPlayer.setOnCompletionListener{ currentState = STATE_STOP }
            mediaPlayer.prepareAsync()
        }
    }

    private fun bindCover() {
        binding?.let {
            Glide.with(it.root)
                .load(track?.getTrackArtworkUrl512() ?: "")
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

    private fun MediaPlayer.getCurrentTimeString() : String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this.currentPosition)
    }
    private fun updatePlayerInfo() {
        when (currentState) {
            STATE_PLAY -> {
                handler.postDelayed(playerRunnable, DELAY_UPDATE)
            }
            STATE_STOP -> {
                changeState()
            }
        }

        binding?.currentTimeTv?.text = mediaPlayer.getCurrentTimeString()
    }

    private fun changeState() {
        when (currentState) {
            STATE_PLAY -> {
                currentState = STATE_PAUSED
                mediaPlayer.pause()
                binding?.playButton?.setImageResource(R.drawable.play_button)
            }
            STATE_PAUSED -> {
                currentState = STATE_PLAY
                mediaPlayer.start()
                binding?.playButton?.setImageResource(R.drawable.pause_button)
            }
            STATE_STOP -> {
                currentState = STATE_PAUSED
                mediaPlayer.seekTo(0)
                binding?.playButton?.setImageResource(R.drawable.play_button)
            }
        }
        updatePlayerInfo()
    }
}