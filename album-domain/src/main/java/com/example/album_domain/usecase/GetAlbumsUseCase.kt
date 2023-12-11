package com.example.album_domain.usecase

import com.example.album_domain.model.Album
import com.example.album_domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    operator fun invoke(): List<Album> {
        return repository.getAlbums()
    }
}