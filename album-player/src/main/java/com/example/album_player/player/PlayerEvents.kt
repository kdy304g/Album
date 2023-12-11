package com.example.album_player.player

import com.example.album_domain.model.Track

interface PlayerEvents {
    fun onPlayPauseClick()
    fun onPreviousClick()
    fun onNextClick()
    fun onTrackClick(index: Int, tracks: List<Track>)
    fun onSeekBarPositionChanged(position: Long)
}