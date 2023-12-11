package com.example.album_player.di

import com.example.album_player.AudioManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@EntryPoint
@InstallIn(SingletonComponent::class)
interface AlbumEntryPoint {
    fun audioManager(): AudioManager
}