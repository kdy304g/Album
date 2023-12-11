package com.example.album_core.di

import com.example.album_domain.repository.AlbumRepository
import com.example.album_domain.usecase.GetAlbumsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun getAlbumsUseCase(repository: AlbumRepository) = GetAlbumsUseCase(repository)
}