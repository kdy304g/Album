package com.example.album

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.album.ui.theme.AlbumTheme
import com.example.album.ui.theme.PurpleGrey80
import com.example.album_player.player.AlbumPlayer
import com.example.album_ui.AlbumApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("Range")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AlbumPlayer.connect(applicationContext)

        setContent {
            AlbumTheme {
                AlbumApp(
                    windowSizeClass = calculateWindowSizeClass(this),
                    backgroundColor = PurpleGrey80
                )
            }
        }
    }


}