package com.example.album

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AlbumApp : Application() {
    companion object {
        private lateinit var application: AlbumApp
        fun getInstance(): AlbumApp = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this

        Glide.with(applicationContext)
            .applyDefaultRequestOptions(
                RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            )
    }
}