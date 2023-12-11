package com.example.album_ui.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.album_domain.model.Album
import com.example.album_ui.detail.component.Controller
import com.example.album_ui.detail.component.Header
import com.example.album_ui.detail.component.TrackItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val album = viewModel.album

    DetailScreen(
        modifier = modifier,
        album = album,
    )
}

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    album: Album,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Header(album)
        Divider(modifier = Modifier.padding(vertical = 15.dp))
        Controller(album)
        Divider(modifier = Modifier.padding(vertical = 15.dp))
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
        ) {
            items(album.tracks.size) { index ->
                TrackItem(index = index, album = album)
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}