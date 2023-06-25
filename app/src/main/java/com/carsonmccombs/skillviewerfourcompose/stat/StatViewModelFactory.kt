package com.carsonmccombs.skillviewerfourcompose.stat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StatViewModelFactory(private val dao: StatDao): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatViewModel(dao) as T
    }
}