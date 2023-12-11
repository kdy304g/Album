package com.example.album_ui.home

import androidx.lifecycle.ViewModel
import com.example.album_domain.model.Album
import com.example.album_domain.usecase.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {
    fun getAlbums(): List<Album> = getAlbumsUseCase()

}