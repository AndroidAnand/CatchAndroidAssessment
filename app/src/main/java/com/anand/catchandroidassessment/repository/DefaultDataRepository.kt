// File: DefaultDataRepository.kt
package com.anand.catchandroidassessment.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.anand.catchandroidassessment.util.UrlConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class DefaultDataRepository(
    private val context: Context,
    private val fileName: String = UrlConstants.FILE_NAME
) : DataRepository {

    // build the full URL from the constants
    private val url: String
        get() = UrlConstants.BASE_URL + UrlConstants.ORG_PATH + fileName

    override suspend fun fetchJson(): String = withContext(Dispatchers.IO) {
        val cm = context.getSystemService(ConnectivityManager::class.java)
            ?: throw IllegalStateException("No ConnectivityManager")
        val hasNet = cm.activeNetwork
            ?.let(cm::getNetworkCapabilities)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false

        if (hasNet) {
            URL(url).readText()
        } else {
            context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }
        }
    }
}
