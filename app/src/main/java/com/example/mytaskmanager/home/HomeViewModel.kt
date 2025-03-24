package com.example.mytaskmanager.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytaskmanager.database.TaskManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TaskUIState {
    data object Loading : TaskUIState
    data class TaskLoaded(
        val taskLists: List<Tasks>
    ) : TaskUIState

    data object EmptyState : TaskUIState
}

data class Tasks(
    val id:String,
    val title: String,
    val description: String,
    val taskPriority: String,
    val taskStatus: String = "Pending",
    val date: String
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tasksLocalDataSource: TasksLocalDataSource,
) : ViewModel() {

    val statusSelected = MutableStateFlow("")
    val count = MutableStateFlow(0)

    fun setTaskStatus(taskStatus: String) {
        statusSelected.value = taskStatus
    }

    val taskUIState:Flow<TaskUIState> =
        statusSelected.flatMapLatest { selectedStatus ->
        if (selectedStatus.isEmpty()) {
            tasksLocalDataSource.getAllTasks()
        } else {
            tasksLocalDataSource.getTaskByStatus(taskStatus = selectedStatus)
        }
    }.mapLatest {
        if (it.isEmpty()) {
            TaskUIState.EmptyState
        } else {
            transform(it)
    }

    }

    private fun transform(listOftask: List<TaskManager>): TaskUIState {
        return TaskUIState.TaskLoaded(
            taskLists = listOftask.filter {
                when(statusSelected.value){
                    "Pending" -> it.taskStatus == "Pending"
                    "Completed" -> it.taskStatus == "Completed"
                    else -> true
                }
            }.map {
                transform(it)
            }
        )
    }

    private fun transform(taskManager: TaskManager): Tasks {
        return Tasks(
            id = taskManager.id.toString(),
            title = taskManager.title,
            description = taskManager.description,
            taskPriority = taskManager.priority,
            taskStatus = taskManager.taskStatus,
            date = taskManager.entryDate
        )
    }


    fun clearAllCompletedTasks(){
        viewModelScope.launch {
            tasksLocalDataSource.deleteAllCompleted()
        }
    }

    fun count(){
        viewModelScope.launch {
            tasksLocalDataSource.getCunt(statusSelected.value).mapLatest {
            count.value  = it
          }
        }
    }
}



//    private fun generateRandomPriority(): String {
//        val priorities = listOf("High", "Medium", "Low")
//        return priorities.random()
//    }
//
//    private fun generateDummyTasks(count: Int = 5): List<TaskState> {
//        return (1..count).map { index ->
//            TaskState(
//                title = "Generic Task $index",
//                description = "This is a generic description for task $index.",
//                taskPriority = generateRandomPriority(),
//                date = LocalDate.now().toString()
//            )
//        }
//    }


