package com.ssikira.things.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ssikira.things.data.Item
import com.ssikira.things.data.ThingsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThingsViewModel(private val db: ThingsDatabase) : ViewModel() {
    private val _items = MutableStateFlow(emptyList<Item>())
    val items = _items.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            db.itemsDao().getAll().flowOn(Dispatchers.IO).collect { items ->
                _items.update { items }
            }
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            db.itemsDao().insertItem(item)
        }
    }
}

class ItemsViewModelFactory(private val database: ThingsDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThingsViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}