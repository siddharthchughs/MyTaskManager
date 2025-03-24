package com.example.mytaskmanager.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytaskmanager.home.TasksLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedTaskViewModel @Inject constructor(
    private val tasksLocalDataSource: TasksLocalDataSource
) : ViewModel() {

    fun updateTaskStatus(id:Int){
        viewModelScope.launch {
            tasksLocalDataSource.updateTaskStatus(taskId = id)
        }
    }

    fun deleteTask(id:Int){
        viewModelScope.launch {
            tasksLocalDataSource.deleteByTaskID(taskId = id)
        }
    }
}