package com.example.mytaskmanager.TaskTestCase

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.example.mytaskmanager.database.TaskManager
import com.example.mytaskmanager.database.TaskManagerDao
import com.example.mytaskmanager.database.TaskManagerDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskCaseTests {

    private lateinit var taskManagerDao: TaskManagerDao
    private lateinit var taskManagerDatabase: TaskManagerDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        taskManagerDatabase = Room.inMemoryDatabaseBuilder(
            context, TaskManagerDatabase::class.java
        ).build()
        taskManagerDao = taskManagerDatabase.taskManagerDao()
    }


    @Test
    fun returnListOfTasksAvaliable() = runTest {
        val taskOne = TaskManager(
            id = 1,
            title = "The only limit to our realization of tomorrow is our doubts of today.",
            description = "The only limit to our realization of tomorrow is our doubts of today.",
            priority = "High",
            taskStatus = "Pending",
            entryDate = "12/12/2023"
        )
        val taskTwo = TaskManager(
            id = 2,
            title = "The only limit to our realization of tomorrow is our doubts of today.",
            description = "The only limit to our realization of tomorrow is our doubts of today.",
            priority = "High",
            taskStatus = "Pending",
            entryDate = "14/09/2023"
        )

        taskManagerDao.addNewTask(taskEntities = taskOne)
        taskManagerDao.addNewTask(taskEntities = taskTwo)
        val taskDaoValidation = taskManagerDao.getAllTasks().first()
        assertTrue(taskDaoValidation.contains(taskOne))
        assertEquals(2, taskDaoValidation.size)
    }

    @Test
    fun returnIfAllTasksAreDeleted() = runTest {
        val taskOne = TaskManager(
            id = 1,
            title = "The only limit to our realization of tomorrow is our doubts of today.",
            description = "The only limit to our realization of tomorrow is our doubts of today.",
            priority = "High",
            taskStatus = "Pending",
            entryDate = "12/12/2023"
        )
        val taskTwo = TaskManager(
            id = 2,
            title = "The only limit to our realization of tomorrow is our doubts of today.",
            description = "The only limit to our realization of tomorrow is our doubts of today.",
            priority = "High",
            taskStatus = "Pending",
            entryDate = "14/09/2023"
        )

        taskManagerDao.addNewTask(taskEntities = taskOne)
        taskManagerDao.addNewTask(taskEntities = taskTwo)
        taskManagerDao.deleteTasks()
        assertTrue(taskManagerDao.getAllTasks().first().size == 0)
    }


    @Test
    fun returnCountOfTaskByStatus() = runTest {
        val taskOne = TaskManager(
            id = 1,
            title = "The only limit to our realization of tomorrow is our doubts of today.",
            description = "The only limit to our realization of tomorrow is our doubts of today.",
            priority = "High",
            taskStatus = "Pending",
            entryDate = "12/12/2023"
        )
        val taskTwo = TaskManager(
            id = 2,
            title = "The only limit to our realization of tomorrow is our doubts of today.",
            description = "The only limit to our realization of tomorrow is our doubts of today.",
            priority = "High",
            taskStatus = "Completed",
            entryDate = "14/09/2023"
        )
        val taskThree = TaskManager(
            id = 3,
            title = "The only limit to our realization of tomorrow is our doubts of today.",
            description = "The only limit to our realization of tomorrow is our doubts of today.",
            priority = "High",
            taskStatus = "Completed",
            entryDate = "14/09/2023"
        )

        taskManagerDao.addNewTask(taskEntities = taskOne)
        taskManagerDao.addNewTask(taskEntities = taskTwo)
        taskManagerDao.addNewTask(taskEntities = taskThree)
        val countOfPendingTasks = taskManagerDao.getCount("Pending").first()
        val countOfCompletedTasks = taskManagerDao.getCount("Completed").first()
        assertEquals(1, countOfPendingTasks)
        assertEquals(2, countOfCompletedTasks)
    }

    @After
    fun closeDb() {
        taskManagerDatabase.close()
    }
}