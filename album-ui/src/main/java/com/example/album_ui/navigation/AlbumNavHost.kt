package com.example.album_ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.example.album_ui.AlbumAppState
import com.example.album_ui.detail.navigation.detailScreen
import com.example.album_ui.home.navigation.homeRoute
import com.example.album_ui.home.navigation.homeScreen

@Composable
fun AlbumNavHost(
    appState: AlbumAppState,
    modifier: Modifier = Modifier,
    startDestination: String = homeRoute,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val navController = appState.navController
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen()
        detailScreen()
    }
}