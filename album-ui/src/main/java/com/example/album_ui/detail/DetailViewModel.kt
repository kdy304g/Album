package com.example.album_ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.album_domain.usecase.GetAlbumUseCase
import com.example.album_ui.detail.navigation.DetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAlbumUseCase: GetAlbumUseCase,
) : ViewModel() {
    private val detailArgs: DetailArgs = DetailArgs(savedStateHandle)
    val album = getAlbumUseCase(detailArgs.albumId)
}