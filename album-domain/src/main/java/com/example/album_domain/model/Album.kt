package com.example.album_domain.model

data class Album(
    val id: Long,
    val title: String,
    val artist: String,
    val albumArt: String,
    val tracks: List<Track>
)
