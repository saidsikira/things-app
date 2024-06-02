package com.ssikira.things.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ssikira.things.data.Filter
import com.ssikira.things.data.Item
import com.ssikira.things.data.Project
import com.ssikira.things.data.ThingsDatabase
import com.ssikira.things.data.ThingsRepository
import com.ssikira.things.data.markCompleted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThingsViewModel(private val repository: ThingsRepository) : ViewModel() {

    private val _filter = MutableStateFlow<Filter>(Filter.Inbox)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _items = _filter.flatMapLatest { filter ->
        when (filter) {
            is Filter.Inbox -> repository.getItemsInInbox()
            is Filter.Logbook -> repository.getItemsInLogbook()
            is Filter.Project -> repository.getItemsByProject(filter.projectId)
            is Filter.Today -> repository.getItemsByDate(filter.today)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val items: StateFlow<List<Item>> = _items

    private val _projects = MutableStateFlow(emptyList<Project>())
    val projects: StateFlow<List<Project>> = _projects

    init {
        viewModelScope.launch {
            repository.getAllProjects().collect { projects ->
                _projects.update { projects }
            }
        }
    }

    fun setFilter(filter: Filter) {
        _filter.value = filter
    }

    fun insertItem(item: Item) {
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }

    fun insertProject(project: Project) {
        viewModelScope.launch {
            repository.insertProject(project)
        }
    }

    fun markCompleted(item: Item) {
        viewModelScope.launch {
            repository.markCompleted(item)
        }
    }

    fun getProjectName(projectId: Int): String {
        return _projects.value.find { it.id == projectId }?.title ?: "Unknown Project"
    }
}

class ItemsViewModelFactory(private val repository: ThingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}