package com.example.album_ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.album_domain.model.Album
import com.example.album_ui.home.ui.component.HomeAlbumItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("Range", "MutableCollectionMutableState")
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetail: (Long) -> Unit,
    onShowSnackBar: suspend (String, String) -> Boolean
) {

    val albums = remember { mutableStateListOf<Album>() }

    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_MEDIA_AUDIO,
        )
    )

    LaunchedEffect(multiplePermissionsState.allPermissionsGranted) {
        if (multiplePermissionsState.allPermissionsGranted) {
            albums.addAll(viewModel.getAlbums())
        } else {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = 15.dp),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(albums.size) { index ->
                val album = albums[index]
                HomeAlbumItem(
                    modifier = Modifier
                        .clickable {
                            onNavigateToDetail(album.id)
                        },
                    album = album
                )
            }
        }
    }
}