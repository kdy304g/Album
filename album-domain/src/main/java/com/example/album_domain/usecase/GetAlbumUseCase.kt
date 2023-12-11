package com.example.album_domain.usecase

import com.example.album_domain.model.Album
import com.example.album_domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
){
    operator fun invoke(albumId: Long): Album {
        return repository.getAlbum(albumId)
    }
}