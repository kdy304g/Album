package com.example.album_ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.example.album_ui.AlbumAppState
import com.example.album_ui.detail.navigation.detailScreen
import com.example.album_ui.detail.navigation.navigateToDetail
import com.example.album_ui.home.navigation.homeRoute
import com.example.album_ui.home.navigation.homeScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AlbumNavHost(
    appState: AlbumAppState,
    modifier: Modifier = Modifier,
    startDestination: String = homeRoute,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(
            onNavigateToDetail = { albumId ->
                navController.navigateToDetail(albumId = albumId)
            },
            onShowSnackBar = onShowSnackbar
        )
        detailScreen(
            onNavigateBack = navController::popBackStack
        )
    }
}