package com.example.album_ui.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.album_ui.detail.DetailRoute

const val detailRoute = "detail"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(detailRoute, navOptions)
}

fun NavGraphBuilder.detailScreen(

) {
    composable(
        route = detailRoute
    ) {
        DetailRoute()
    }
}