package com.example.album_domain.model

data class Track(
    val trackId: Int = 0,
    val trackName: String = "",
    val trackUrl: String = "",
    val trackImage: String = "",
    val artistName: String = "",
) {
    class Builder {
        private var trackId: Int = 0
        private lateinit var trackName: String
        private lateinit var trackUrl: String
        private lateinit var trackImage: String
        private lateinit var artistName: String

        fun trackId(trackId: Int) = apply { this.trackId = trackId }
        fun trackName(trackName: String) = apply { this.trackName = trackName }
        fun trackUrl(trackUrl: String) = apply { this.trackUrl = trackUrl }
        fun trackImage(trackImage: String) = apply { this.trackImage = trackImage }
        fun artistName(artistName: String) = apply { this.artistName = artistName }

        fun build(): Track {
            return Track(
                trackId,
                trackName,
                trackUrl,
                trackImage,
                artistName,
            )
        }
    }
}