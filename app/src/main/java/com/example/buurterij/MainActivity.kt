package com.example.buurterij

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.buurterij.data.AppDatabase
import com.example.buurterij.data.ForagingSpotRepository
import com.example.buurterij.ui.ForagingMapScreen
import com.example.buurterij.ui.ForagingViewModel
import com.example.buurterij.ui.ForagingViewModelFactory
import com.example.buurterij.ui.theme.BuurterijTheme
import java.io.File
import org.osmdroid.config.Configuration

class MainActivity : AppCompatActivity() {
    private val viewModel: ForagingViewModel by viewModels {
        ForagingViewModelFactory(
            ForagingSpotRepository(
                dao = AppDatabase.getInstance(applicationContext).foragingSpotDao(),
                customPlantTypeDao = AppDatabase.getInstance(applicationContext).customPlantTypeDao(),
                spotPhotoDao = AppDatabase.getInstance(applicationContext).spotPhotoDao(),
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
                ForagingMapScreen(viewModel = viewModel)
            }
        }
    }
}
