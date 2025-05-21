// File: MainViewModel.kt
package com.anand.catchandroidassessment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anand.catchandroidassessment.model.DataItem
import com.anand.catchandroidassessment.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainViewModel(
    private val repo: DataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            try {
                val json = repo.fetchJson()
                val items: List<DataItem> = Json { ignoreUnknownKeys = true }
                    .decodeFromString(json)
                _uiState.value = UiState(data = items)
            } catch (t: Throwable) {
                _uiState.value = UiState(error = t.localizedMessage ?: "Unknown error")
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val data: List<DataItem> = emptyList(),
        val error: String = ""
    )
}
