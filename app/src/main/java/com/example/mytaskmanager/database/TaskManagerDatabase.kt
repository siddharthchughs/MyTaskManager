package com.example.mytaskmanager.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "task_manager_table")
data class TaskManager(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "task_title")
    val title: String,
    @ColumnInfo(name = "task_description")
    val description: String,
    @ColumnInfo(name = "task_status")
    val taskStatus: String,
    @ColumnInfo(name = "task_priority")
    val priority: String,
    @ColumnInfo(name = "task_created_on")
    val entryDate: String,
)

@Dao
interface TaskManagerDao {
    @Query(
        """
        SELECT * FROM task_manager_table ORDER BY task_priority ASC
    """
    )
    fun getAllTasks(): Flow<List<TaskManager>>

    @Query(
        """
        SELECT * FROM task_manager_table where id = :Id
    """
    )
    fun getTaskById(Id: String): TaskManager

    @Query(
        """
        SELECT * FROM task_manager_table where task_status = :taskStatus
    """
    )
    fun getTasksByStatus(taskStatus: String):  Flow<List<TaskManager>>

    @Query(
        """
        SELECT COUNT(*) FROM task_manager_table where task_status = :taskStatus
    """
    )
    fun getCount(taskStatus: String): Flow<Int>

    @Query(
        """
        UPDATE task_manager_table SET task_status = 'Completed' where id = :id
    """
    )
    suspend fun updateTaskStatus(id: Int)

    @Query("DELETE FROM task_manager_table where id = :id")
    suspend fun deleteByTaskID(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewTask(taskEntities: TaskManager)

    @Query("""DELETE FROM task_manager_table""")
    suspend fun deleteTasks()

    @Query("""DELETE FROM task_manager_table where task_status = 'Completed' """)
    suspend fun deleteCompletedTask()
}

@Database(entities = [TaskManager::class], version = 1, exportSchema = false)
abstract class TaskManagerDatabase : RoomDatabase() {
    abstract fun taskManagerDao(): TaskManagerDao
}
