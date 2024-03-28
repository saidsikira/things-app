package com.ssikira.things.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ssikira.things.data.Item
import com.ssikira.things.data.Project
import com.ssikira.things.data.ThingsDatabase
import com.ssikira.things.data.markCompleted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThingsViewModel(private val db: ThingsDatabase) : ViewModel() {
    private val _items = MutableStateFlow(emptyList<Item>())
    private val _projects = MutableStateFlow(emptyList<Project>())

    val items = _items.asStateFlow()
    val projects = _projects.asStateFlow()

    init {
        getAll()
    }

    private fun getAll() {
        viewModelScope.launch {
            db.itemsDao().getAll().flowOn(Dispatchers.IO).collect { items ->
                _items.update { items }
            }
        }

        viewModelScope.launch {
            db.projectsDao().getAllProjects().flowOn(Dispatchers.IO).collect { projects ->
                _projects.update { projects }
            }
        }
    }

    fun insertItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            db.itemsDao().insertItem(item)
        }
    }

    fun insertProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            db.projectsDao().insertProject(project)
        }
    }

    fun markCompleted(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            db.itemsDao().markCompleted(item)
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