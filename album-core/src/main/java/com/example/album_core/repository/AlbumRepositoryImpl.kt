package com.example.album_core.repository

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.album_domain.model.Album
import com.example.album_domain.model.Track
import com.example.album_domain.repository.AlbumRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): AlbumRepository {

    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ALBUM,
    )
    @SuppressLint("Range")
    override fun getAlbums(): List<Album> {
        val albumMap = mutableMapOf<Long, Album>()
        val trackMap = mutableMapOf<Long, MutableList<Track>>()

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )

        while (cursor?.moveToNext() == true) {
            val albumTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val artWorkUri = Uri.parse("content://media/external/audio/albumart")
            val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
            val albumArt = ContentUris.withAppendedId(artWorkUri, albumId)
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val track = Track.Builder()
                .trackName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)))
                .trackUrl(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)))
                .trackImage(albumArt.toString())
                .artistName(artist)
                .build()

            albumMap.putIfAbsent(albumId, Album(
                id = albumId,
                title = albumTitle,
                artist = artist,
                albumArt = albumArt.toString(),
                tracks = listOf()
            ))

            trackMap.getOrPut(albumId) {
                mutableListOf()
            }.add(track)

        }
        cursor?.close()
        val albums = albumMap.map {
            Album(
                id = it.value.id,
                title = it.value.title,
                artist = it.value.artist,
                albumArt = it.value.albumArt,
                tracks = trackMap.getOrDefault(it.key, listOf())
            )
        }
        return albums
    }

    override fun getAlbum(albumId: Long): Album {
        val album = getAlbums().first {
            it.id == albumId
        }
        return album
    }
}