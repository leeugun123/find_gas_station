package org.techtown.find_gas_station.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.techtown.find_gas_station.Repository.SetRepository

class SetViewModelFactory(private val repository: SetRepository) : ViewModelProvider.Factory {
    fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}