package com.example.buurterij.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buurterij.data.ForagingSpotRepository

class ForagingViewModelFactory(
    private val repository: ForagingSpotRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ForagingViewModel(repository) as T
}
