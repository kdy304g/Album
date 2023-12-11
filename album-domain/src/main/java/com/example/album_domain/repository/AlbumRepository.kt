package com.example.album_domain.repository

import com.example.album_domain.model.Album

interface AlbumRepository {
    fun getAlbums(): List<Album>
    fun getAlbum(albumId: Long): Album
}