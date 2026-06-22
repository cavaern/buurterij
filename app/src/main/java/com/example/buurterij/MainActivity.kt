package com.example.buurterij

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.buurterij.data.AppDatabase
import com.example.buurterij.data.ForagingSpotRepository
import com.example.buurterij.ui.ForagingMapScreen
import com.example.buurterij.ui.ForagingViewModel
import com.example.buurterij.ui.ForagingViewModelFactory
import com.example.buurterij.ui.SplashScreen
import com.example.buurterij.ui.theme.BuurterijTheme
import java.io.File
import kotlinx.coroutines.delay
import org.osmdroid.config.Configuration

private const val SPLASH_DURATION_MS = 2500L

class MainActivity : AppCompatActivity() {
    private val viewModel: ForagingViewModel by viewModels {
        ForagingViewModelFactory(
            ForagingSpotRepository(
                dao = AppDatabase.getInstance(applicationContext).foragingSpotDao(),
                customPlantTypeDao = AppDatabase.getInstance(applicationContext).customPlantTypeDao(),
                spotPhotoDao = AppDatabase.getInstance(applicationContext).spotPhotoDao(),
                journalEntryDao = AppDatabase.getInstance(applicationContext).journalEntryDao(),
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().apply {
            userAgentValue = packageName
            osmdroidBasePath = File(cacheDir, "osmdroid")
            osmdroidTileCache = File(osmdroidBasePath, "tiles")
        }

        enableEdgeToEdge()
        setContent {
            BuurterijTheme {
                var showSplash by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    delay(SPLASH_DURATION_MS)
                    showSplash = false
                }
                if (showSplash) {
                    SplashScreen()
                } else {
                    ForagingMapScreen(viewModel = viewModel)
                }
            }
        }
    }
}
