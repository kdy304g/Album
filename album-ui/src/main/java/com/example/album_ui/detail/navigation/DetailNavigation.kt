package com.example.album_ui.detail.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.album_ui.detail.DetailRoute
import java.net.URLDecoder

internal const val albumIdArg = "albumId"
const val detailRoute = "detail"

internal class DetailArgs(val albumId: Long) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                albumId = checkNotNull(savedStateHandle[albumIdArg]),
            )
}

fun NavController.navigateToDetail(navOptions: NavOptions? = null, albumId: Long) {
    this.navigate("$detailRoute?albumId=$albumId")
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.detailScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = "$detailRoute?albumId={$albumIdArg}",
        arguments = listOf(
            navArgument(albumIdArg) { type = NavType.LongType}
        )
    ) {
        DetailRoute(
            onNavigateBack = onNavigateBack
        )
    }
}