package com.anand.catchandroidassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.anand.catchandroidassessment.navigation.AppNavigation
import com.anand.catchandroidassessment.ui.theme.CatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Read data.json from assets folder
        val jsonString = assets.open("data.json").bufferedReader().use { it.readText() }

        setContent {
            CatchTheme {
                AppNavigation()
            }
        }
    }
}
