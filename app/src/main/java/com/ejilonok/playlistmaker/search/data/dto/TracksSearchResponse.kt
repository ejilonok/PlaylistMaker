package com.ejilonok.playlistmaker.search.data.dto

data class TracksSearchResponse (val resultCount : Int,
                                 val results : List<TrackDto>) : Response()
