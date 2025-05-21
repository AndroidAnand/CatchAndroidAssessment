// File: ListScreen.kt
package com.anand.catchandroidassessment.view

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anand.catchandroidassessment.model.DataItem
import com.anand.catchandroidassessment.repository.DefaultDataRepository
import com.anand.catchandroidassessment.util.MainViewModelFactory
import com.anand.catchandroidassessment.viewmodel.MainViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navController: NavController
) {
    // — 1) Get Context once at the edge
    val context = LocalContext.current

    // — 2) Create repo *once* per recomposition when Context changes
    val repo = remember(context) {
        DefaultDataRepository(context)
    }

    // — 3) Instantiate our VM with the repo
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(repo)
    )

    // — 4) Observe UI state
    val uiState by viewModel.uiState.collectAsState()

    // — 5) Pull-to-refresh state (no args)
    val pullState = rememberPullToRefreshState()

    // — 6) Scroll state for the LazyColumn (optional but recommended)
    val listState = rememberLazyListState()

    // — 7) Trigger initial load once
    LaunchedEffect(viewModel) {
        viewModel.loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Catch") })
        }
    ) { innerPadding ->
        // Wrap our content in PullToRefreshBox
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh    = { viewModel.loadData() },
            state        = pullState,
            modifier     = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.error.isNotEmpty() -> {
                    // Error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                uiState.data.isEmpty() && !uiState.isLoading -> {
                    // Empty state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No data available")
                    }
                }
                else -> {
                    // Success (or loading with existing data)
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.data) { item ->
                            ListItem(
                                item = item,
                                onClick = {
                                    val encoded = listOf(
                                        item.title,
                                        item.subtitle,
                                        item.content
                                    ).joinToString("/") { text ->
                                        URLEncoder.encode(text, StandardCharsets.UTF_8.name())
                                    }
                                    navController.navigate("detail/$encoded")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListItem(item: DataItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = item.subtitle,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
    }
}
