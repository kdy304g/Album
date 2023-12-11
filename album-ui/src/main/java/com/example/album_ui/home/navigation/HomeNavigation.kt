package com.example.album_ui.home.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.album_ui.home.HomeRoute

const val homeRoute = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.homeScreen(
    onNavigateToDetail: (Long) -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean
) {
    composable(
        route = homeRoute
    ) {
        HomeRoute(
            onNavigateToDetail = onNavigateToDetail,
            onShowSnackBar = onShowSnackBar
        )
    }
}