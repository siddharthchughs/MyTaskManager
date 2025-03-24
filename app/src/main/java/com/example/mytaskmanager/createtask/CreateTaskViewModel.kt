package com.example.mytaskmanager.createtask

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytaskmanager.database.TaskManager
import com.example.mytaskmanager.home.TasksLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed interface CreateTaskUIState {
    data object Loading : CreateTaskUIState
    data object Loaded : CreateTaskUIState
}

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val tasksLocalDataSource: TasksLocalDataSource
) : ViewModel() {


    val createTaskUIState: Flow<CreateTaskUIState> = flowOf(
        CreateTaskUIState.Loaded
    )

    val taskTitleText = mutableStateOf("")
    val descriptionText = mutableStateOf("")
    val dateText = mutableStateOf("")
    val selectedOption = mutableStateOf("")

    fun onTaskTitleChange(title: String) {
        taskTitleText.value = title
    }

    fun onDescriptionChange(description: String) {
        descriptionText.value = description
    }

    fun onDateSelected(date: String) {
        dateText.value = date
    }

    fun onPrioritySelect(prioritySelected: String) {
        selectedOption.value = prioritySelected
        Timber.i("Selected Option: ${selectedOption.value}")

    }

    fun createTask() {
        viewModelScope.launch {
            tasksLocalDataSource.addTask(
                TaskManager(
                    title = taskTitleText.value,
                    description = descriptionText.value,
                    priority = selectedOption.value,
                    taskStatus = "Pending",
                    entryDate = dateText.value
                )
            )
        }
    }
}