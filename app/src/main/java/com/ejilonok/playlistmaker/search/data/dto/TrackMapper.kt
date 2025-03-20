package com.ejilonok.playlistmaker.search.data.dto

import com.ejilonok.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun map(trackDto : TrackDto) : Track {
        return Track(trackDto.trackId,
            trackDto.trackName,
            trackDto.artistName,
            formatter.format(trackDto.trackTimeMillis),
            trackDto.artworkUrl100 ?: "",
            trackDto.collectionName ?: "",
            trackDto.releaseDate ?: "",
            trackDto.primaryGenreName ?: "",
            trackDto.country ?: "",
            trackDto.previewUrl)
    }

    private val formatter by lazy  { SimpleDateFormat("mm:ss", Locale.getDefault()) }
}
