package com.anand.catchandroidassessment.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anand.catchandroidassessment.repository.DataRepository
import com.anand.catchandroidassessment.viewmodel.MainViewModel

class MainViewModelFactory(
    private val repo: DataRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(repo) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
}