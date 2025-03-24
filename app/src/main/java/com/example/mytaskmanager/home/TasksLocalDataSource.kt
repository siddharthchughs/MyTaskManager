package com.example.mytaskmanager.home

import com.example.mytaskmanager.database.TaskManager
import com.example.mytaskmanager.database.TaskManagerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject

interface TasksLocalDataSource {
    fun getAllTasks(): Flow<List<TaskManager>>
    fun getTaskByStatus(taskStatus: String): Flow<List<TaskManager>>
    suspend fun addTask(taskManager: TaskManager)
    fun getCunt(taskStatus: String): Flow<Int>
    suspend fun generateDummyTasks(dummyFlowTask: Flow<List<TaskManager>>)
    suspend fun deleteAllTasks()
    suspend fun deleteAllCompleted()
    suspend fun updateTaskStatus(taskId: Int)
    suspend fun deleteByTaskID(taskId: Int)
}

class TasksLocalDataSourceImpl @Inject constructor(
    private val taskManagerDao: TaskManagerDao
) : TasksLocalDataSource {

    override fun getAllTasks(): Flow<List<TaskManager>> =
        taskManagerDao.getAllTasks().flowOn(Dispatchers.IO).conflate()

    override fun getTaskByStatus(taskStatus: String): Flow<List<TaskManager>> =
        taskManagerDao.getTasksByStatus(taskStatus = taskStatus).flowOn(Dispatchers.IO).conflate()

    override suspend fun addTask(taskManager: TaskManager) =
        taskManagerDao.addNewTask(taskManager)

    override fun getCunt(taskStatus: String): Flow<Int> = taskManagerDao.getCount(taskStatus = taskStatus)

    override suspend fun generateDummyTasks(dummyFlowTask: Flow<List<TaskManager>>) {
        val priorities = listOf("High", "Medium", "Low")
        val taskStatus = listOf("Completed", "Pending")
        for (i in 1..50) {
            val randomPriority = priorities.random()
            val randomtaskStatus = taskStatus.random()
            taskManagerDao.addNewTask(
                TaskManager(
                    title = "Task ${i}",
                    description = "Description ${i}",
                    priority = randomPriority,
                    taskStatus = randomtaskStatus,
                    entryDate = LocalDate.now().toString()
                )
            )
        }
    }

    override suspend fun deleteAllTasks() {
        taskManagerDao.deleteTasks()
    }

    override suspend fun deleteAllCompleted() = taskManagerDao.deleteCompletedTask()
    override suspend fun updateTaskStatus(taskId: Int) {
        taskManagerDao.updateTaskStatus(id = taskId)
    }

    override suspend fun deleteByTaskID(taskId: Int) {
        taskManagerDao.deleteByTaskID(id = taskId)
    }
}