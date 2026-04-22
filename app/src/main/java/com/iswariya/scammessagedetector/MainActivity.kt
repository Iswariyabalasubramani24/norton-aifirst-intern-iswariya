package com.iswariya.scammessagedetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.iswariya.scammessagedetector.ui.ScamDetectionApp
import com.iswariya.scammessagedetector.ui.theme.ScamMessageDetectorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScamMessageDetectorTheme {
                ScamDetectionApp()
            }
        }
    }
}
