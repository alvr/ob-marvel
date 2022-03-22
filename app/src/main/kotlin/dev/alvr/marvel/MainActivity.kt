package dev.alvr.marvel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import dev.alvr.marvel.navigation.MarvelNavigator
import dev.alvr.marvel.ui.base.Marvel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Marvel {
                ProvideWindowInsets {
                    MarvelNavigator()
                }
            }
        }
    }
}
