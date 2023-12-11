package com.example.album_core.di

import android.app.Application
import android.content.Context
import com.example.album_core.repository.AlbumRepositoryImpl
import com.example.album_domain.repository.AlbumRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideAlbumRepositoryImpl(repository: AlbumRepositoryImpl): AlbumRepository
}