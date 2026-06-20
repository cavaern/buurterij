package com.example.buurterij.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.buurterij.R

private val SplashBackground = Color(0xFFF7F1E2)

@Composable
fun SplashScreen() {
    Image(
        painter = painterResource(R.drawable.splash_logo),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground),
    )
}
