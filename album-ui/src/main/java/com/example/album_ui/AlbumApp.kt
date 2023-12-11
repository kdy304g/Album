package com.example.album_ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.album_player.player.AlbumPlayer
import com.example.album_ui.component.AlbumMiniPlayer
import com.example.album_ui.component.AlbumExpandedPlayer
import com.example.album_ui.navigation.AlbumNavHost
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AlbumApp(
    windowSizeClass: WindowSizeClass,
    backgroundColor: Color,
    appState: AlbumAppState = rememberAlbumAppState(
        windowSizeClass = windowSizeClass
    )
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = appState.navController
    var canPop by remember { mutableStateOf(false) }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
            canPop = controller.previousBackStackEntry != null
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    val fullScreenState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    val onBottomTabClick: () -> Unit = { scope.launch { fullScreenState.show() } }
    val selectedTrack by AlbumPlayer.selectedTrackState.collectAsState(null)
    val playerState by AlbumPlayer.playerState.collectAsState()

    ModalBottomSheetLayout(
        sheetContent = {
            selectedTrack?.let {
                AlbumExpandedPlayer(
                    modifier = Modifier.background(backgroundColor),
                    selectedTrack = it,
                    playerEvents = AlbumPlayer,
                    playbackState = AlbumPlayer.playbackState,
                    playerStates = playerState
                )
            }
        },
        sheetState = fullScreenState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetElevation = 8.dp
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    backgroundColor = backgroundColor,
                    title = {
                        Text(
                            text = "Album App",
                            color = Color.White
                        )
                    },
                    navigationIcon = if (canPop) {
                        {
                            IconButton(
                                onClick = { appState.navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }
                    } else {
                        null
                    }
                )
            }
        ) {
            Box(
                Modifier.fillMaxSize()
            ) {
                AlbumNavHost(
                    appState = appState,
                    onShowSnackbar = { message, action ->
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = SnackbarDuration.Short
                        ) == SnackbarResult.ActionPerformed
                    }
                )

                AnimatedVisibility(
                    modifier = Modifier
                        .shadow(elevation = 20.dp)
                        .align(Alignment.BottomCenter),
                    visible = selectedTrack != null,
                    enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight })
                ) {
                    selectedTrack?.let { track ->
                        AlbumMiniPlayer(
                            modifier = Modifier.background(backgroundColor),
                            selectedTrack = track,
                            playerEvents = AlbumPlayer,
                            onBottomTabClick = onBottomTabClick,
                            playerStates = playerState
                        )
                    }
                }
            }

            BackHandler {
                if (fullScreenState.currentValue == ModalBottomSheetValue.Expanded) {
                    scope.launch {
                        fullScreenState.hide()
                    }
                } else {
                    appState.navController.navigateUp()
                }
            }
        }
    }
}